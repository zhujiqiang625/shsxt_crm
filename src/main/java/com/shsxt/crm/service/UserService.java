package com.shsxt.crm.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shsxt.crm.dao.UserDao;
import com.shsxt.crm.dao.UserRoleDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.shsxt.crm.dto.UserQuery;
import com.shsxt.crm.exception.ParamException;
import com.shsxt.crm.model.User;
import com.shsxt.crm.model.UserRole;
import com.shsxt.crm.util.AssertUtil;
import com.shsxt.crm.util.MD5Util;
import com.shsxt.crm.util.UserIDBase64;
import com.shsxt.crm.vo.UserLoginIdentity;
import com.shsxt.crm.vo.UserVO;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserRoleDao userRoleDao;

	@Autowired
	private PermissionService permissionService;

	/**
	 * 登录
	 * @param userName
	 * @param password
	 */
	public Object[] login(String userName, String password) {
//		Map<String, Object> result = new HashMap<>();
		// 基本参数验证
		if(StringUtils.isBlank(userName)) {
			throw new ParamException("请输入用户名");
//			result.put("resultCode", 0);
//			result.put("resultMessage", "请输入用户名");
//			return result;
		}

		if(StringUtils.isBlank(password)) {
//			result.put("resultCode", 0);
//			result.put("resultMessage", "请输入密码");
//			return result;
			throw new ParamException("请输入密码");
		}
		// 根据userName查询数据
		User user = userDao.findByUserName(userName.trim());

		// 对结果进行判定
		if (user == null) {
			throw new ParamException("用户名或密码错误");
//			result.put("resultCode", 0);
//			result.put("resultMessage", "用户名或密码错误");
//			return result;
		}
		// 密码验证 MD5加密
		String md5Pwd = MD5Util.md5Method(password);
		if (!md5Pwd.equals(user.getPassword())) {
			throw new ParamException("用户名或密码错误");
//			result.put("resultCode", 0);
//			result.put("resultMessage", "用户名或密码错误");
//			return result;
		}

		// 构建登录实体
		UserLoginIdentity userLoginIdentity = buildLoginIdentity(user);

		// 返回
//		result.put("resultCode", 1);
//		result.put("resultMessage", "登录成功");
//		result.put("result", userLoginIdentity);
//		ResultInfo result = new ResultInfo(Constant.SUCCESS_CODE, "登录成功", userLoginIdentity);

		// 获取登录用户的权限
		List<String> permissions = permissionService.findUserPermissions(user.getId());

		return new Object[]{userLoginIdentity, permissions};
	}

	public List<UserVO> findCutomerManager() {
		List<UserVO> customerManagers = userDao.findCutomerManager();
		return customerManagers;
	}

	public Map<String, Object> findAll(UserQuery query) {
		PageBounds pageBounds = new PageBounds(1, 10);
		PageList<User> users = userDao.selectForPage(query, pageBounds);
		//		for(User user : users) {
		//			//
		//		}
		Map<String, Object> result = new HashMap<>();
		result.put("rows", users);
		result.put("total", users.getPaginator().getTotalCount());
		return result;
	}

	/**
	 * 分页查询
	 * @param query
	 * @return
	 */
	public Map<String, Object> selectForPage(UserQuery query) {

		PageList<User> users = userDao.selectForPage(query, query.buildPageBounds());
//		for(User user : users) {
//			List<Integer> roleIds = new ArrayList<>();
//			List<RoleVO> roles = user.getRoles();
//			for(RoleVO roleVo : roles) {
//				roleIds.add(roleVo.getId());
//			}
//			user.setRoleIds(roleIds);
//		}
		Map<String, Object> result = new HashMap<>();
		result.put("rows", users);
		result.put("total", users.getPaginator().getTotalCount());
		return result;
	}

	/**
	 * 添加用户
	 * @param user
	 */
	public void add(User user) {
		// 非空验证 用户名 密码 真实姓名 电话 邮箱 角色
		checkParams(user);
		String userName = user.getUserName();
		// 用户名唯一验证
		User userByUserName = userDao.findByUserName(userName);
		AssertUtil.isTrue(userByUserName != null, "该用户名已存在");
		// 邮箱、手机号唯一验证 TODO
		String password = user.getPassword();
		// 密码加密
		password = MD5Util.md5Method(password);
		user.setPassword(password);
		// 插入数据库
		userDao.insert(user);

		// 关联角色
		relateRole(user);
	}

	/**
	 * 更新
	 * @param user
	 */
	public void update(User user) {
		Integer id = user.getId();
		AssertUtil.intIsNotEmpty(id, "请选择一条记录进行修改");
		checkParams(user);
		User userFromDB = userDao.findById(user.getId());
		if (!userFromDB.getUserName().equals(user.getUserName())) {
			// 用户名唯一验证
			User userByUserName = userDao.findByUserName(user.getUserName());
			AssertUtil.isTrue(userByUserName != null, "该用户名已存在");
		}
		// TODO 手机号以及邮箱唯一验证
		// 更新
		userDao.update(user);

		// 先删除已有的角色
		userRoleDao.deleteByUserId(id);

		// 在插入选择的角色
		relateRole(user);
	}

	/**
	 * 删除
	 * @param ids
	 */
	public void deleteBatch(String ids) {
		AssertUtil.isNotEmpty(ids, "请选择删除的记录");
		userDao.deleteBatch(ids);
		// 删除user_role
	}

	/**
	 * 构建登录信息
	 * @param user
	 * @return
	 */
	private static UserLoginIdentity buildLoginIdentity(User user) {
		UserLoginIdentity userLoginIdentity = new UserLoginIdentity();
		userLoginIdentity.setUserIdString(UserIDBase64.encoderUserID(user.getId()));
		userLoginIdentity.setRealName(user.getTrueName());
		userLoginIdentity.setUserName(user.getUserName());
		return userLoginIdentity;
	}

	/**
	 * 基本参数验证
	 * @param user
	 */
	private static void checkParams(User user) {
		String userName = user.getUserName();
		AssertUtil.isNotEmpty(userName, "请输入用户名");
		String password = user.getPassword();
		AssertUtil.isNotEmpty(password, "请输入密码");
		String realName = user.getTrueName();
		AssertUtil.isNotEmpty(realName, "请输入真实姓名");
		String phone = user.getPhone();
		AssertUtil.isNotEmpty(phone, "请输入手机号");
		String email = user.getEmail();
		AssertUtil.isNotEmpty(email, "请输入邮箱");

	}

	/**
	 * 修改密码
	 * @param userId
	 * @param oldPassword
	 * @param newPassword
	 * @param confirmPassword
	 */
	public void updatePassword(int userId, String oldPassword, String newPassword,
							   String confirmPassword) {
		// 基本参数校验
		AssertUtil.isTrue(userId == 0, "请重新登陆");
		AssertUtil.isTrue(StringUtils.isBlank(oldPassword), "请输入旧密码");
		AssertUtil.isTrue(StringUtils.isBlank(newPassword), "请输入新密码");
		AssertUtil.isTrue(StringUtils.isBlank(confirmPassword), "请输入确认密码");
		AssertUtil.isTrue(!newPassword.equals(confirmPassword), "新密码和确认密码不一致, 请重新输入");

		// 查询用户判断旧密码输入是否正确
		User user = userDao.findById(userId);
		AssertUtil.notNull(user, "该用户不存在或已被注销");
		String password = MD5Util.md5Method(oldPassword);
		AssertUtil.isTrue(!password.equals(user.getPassword()), "旧密码输入错误, 请重新输入");
		// 更新
		String newPwd = MD5Util.md5Method(newPassword);
		int mnt = userDao.updatePassword(user.getId(), newPwd);
		AssertUtil.isTrue(mnt == 0, "更新失败, 请重试");
	}

	/**
	 * 用户关联角色
	 * @param user
	 */
	private void relateRole(User user) {
		List<Integer> roleIds = user.getRoleIds();
		List<UserRole> userRoles = new ArrayList<>();
		if (roleIds == null || roleIds.isEmpty()) {
			return;
		}
		for(Integer roleId : roleIds) {
			UserRole userRole = new UserRole();
			userRole.setRoleId(roleId);
			userRole.setUserId(user.getId());
			userRoles.add(userRole);
		}
		userRoleDao.insertBatch(userRoles);
	}


}
