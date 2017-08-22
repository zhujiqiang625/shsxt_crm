package com.shsxt.crm.service;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.shsxt.crm.base.BaseQuery;
import com.shsxt.crm.constant.ModuleGrade;
import com.shsxt.crm.dao.ModuleDao;
import com.shsxt.crm.dao.RoleDao;
import com.shsxt.crm.model.Module;
import com.shsxt.crm.model.Role;
import com.shsxt.crm.util.AssertUtil;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class RoleService {
    @Autowired
    private RoleDao roleDao;

    public Map<String, Object> selectForPage(BaseQuery query) {
        PageBounds pageBounds = query.buildPageBounds();
        PageList<Role> modulePaceList = roleDao.selectForPage(pageBounds);
        Map<String, Object> map = new HashMap<>();
        map.put("rows", modulePaceList);
        map.put("total", modulePaceList.getPaginator().getTotalCount());
        return map;
    }

    public void add(Role role) {
        checkParams(role);
       Role role1= roleDao.findByName(role.getRoleName());
       AssertUtil.isTrue(role1!=null,"该角色已存在，请重新输入1");
       role.setCreateDate(new Date());
       role.setUpdateDate(new Date());
       roleDao.insert(role);
    }


    public void update(Role role) {
        AssertUtil.intIsNotEmpty(role.getId(),"请选择角色进行删除");
        Role roleById= roleDao.findById(role.getId());
        AssertUtil.notNull(roleById,"该角色不存在");
        if(!roleById.getRoleName().equals(role.getRoleName())){
            Role roleByName=roleDao.findByName(role.getRoleName());
            AssertUtil.notNull(roleByName!=null,"该角色已存在，请重新输入");
        }
        role.setUpdateDate(new Date());
        roleDao.update(role);
    }

    public void delete(String ids) {
        AssertUtil.isNotEmpty(ids, "请选择记录进行删除");
        roleDao.delete(ids);

    }

    private void checkParams(Role role) {
        String roleName = role.getRoleName();
        AssertUtil.isNotEmpty(roleName, "请输入模块名称");
        String roleRemark = role.getRoleRemark();
        AssertUtil.isNotEmpty(roleRemark, "请输入模块名称");
    }

    public Role findById(Integer roleId) {
        AssertUtil.intIsNotEmpty(roleId, "请选择角色");
        Role role = roleDao.findById(roleId);
        AssertUtil.notNull(role, "该角色不存在");
        return role;
    }


    public List<Role> findAll() {
        return roleDao.findAll();
    }
}
