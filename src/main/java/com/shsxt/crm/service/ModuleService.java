package com.shsxt.crm.service;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.shsxt.crm.base.BaseQuery;
import com.shsxt.crm.constant.ModuleGrade;
import com.shsxt.crm.dao.ModuleDao;
import com.shsxt.crm.exception.ParamException;
import com.shsxt.crm.model.Module;
import com.shsxt.crm.util.AssertUtil;
import com.shsxt.crm.vo.ModuleVO;
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
public class ModuleService {
    @Autowired
    private ModuleDao moduleDao;

    public Map<String, Object> selectForPage(BaseQuery query) {
        PageBounds pageBounds = query.buildPageBounds();
        PageList<Module> modulePaceList = moduleDao.selectForPage(pageBounds);
        Map<String, Object> map = new HashMap<>();
        map.put("rows", modulePaceList);
        map.put("total", modulePaceList.getPaginator().getTotalCount());
        return map;
    }

    public void add(Module module) {
        checkParams(module);
        Integer grade = module.getGrade();
        if (grade != ModuleGrade.root.getGrade()) {
            AssertUtil.intIsNotEmpty(module.getParentId(), "请选择上一级");
        }

        String treePath = buildTreePath(grade, module.getParentId());
        module.setTreePath(treePath);
        Module moduleByPermisson = moduleDao.finByOptVaue(module.getOptValue());
        AssertUtil.isTrue(moduleByPermisson != null, "该权限值已存在，请重新输入");
        module.setCreateDate(new Date());
        module.setUpdateDate(new Date());
        module.setIsValid(1);
        moduleDao.add(module);

    }


    public void update(Module module) {
        Integer id = module.getId();
        AssertUtil.intIsNotEmpty(id, "请选择记录进行修改");
        checkParams(module);

        if (module.getGrade() != ModuleGrade.root.getGrade()) {
            AssertUtil.intIsNotEmpty(module.getParentId(), "请选择上一级");
        }
        Module moduleDaoById = moduleDao.findById(id);
        AssertUtil.notNull(moduleDaoById, "记录不存在");
        if (module.getParentId() != null && module.getParentId() != moduleDaoById.getParentId()) {
            String s = buildTreePath(module.getGrade(), module.getParentId());
            module.setTreePath(s);
        }
        if (!module.getOptValue().equals(moduleDaoById.getOptValue())) {
            Module moduleByPermisson = moduleDao.finByOptVaue(module.getOptValue());
            AssertUtil.isTrue(moduleByPermisson != null, "该权限值已存在，请重新输入");
        }
        BeanUtils.copyProperties(module, moduleDaoById);
        moduleDao.update(moduleDaoById);
    }

    public void delete(String ids) {
        AssertUtil.isNotEmpty(ids, "请选择记录进行删除");
        moduleDao.deleteBatch(ids);

    }




    private void checkParams(Module module) {
        String moduleName = module.getModuleName();
        AssertUtil.isNotEmpty(moduleName, "请输入模块名称");
        Integer orders = module.getOrders();
        AssertUtil.intIsNotEmpty(orders, "请输入排序");
        String url = module.getUrl();
        AssertUtil.isNotEmpty(url, "请输入访问路径或者方法");
        String optValue = module.getOptValue();
        AssertUtil.isNotEmpty(optValue, "请输入操作权限");
        Integer grade = module.getGrade();
        AssertUtil.intIsNotEmpty(grade, "请输入层级关系");

    }

    private String buildTreePath(Integer grade, Integer parentId) {
        if (grade == ModuleGrade.root.getGrade())
            return null;
        String treePath = "";
        Module parentModule = moduleDao.findById(parentId);
        AssertUtil.notNull(parentModule, "该父级不存，请重新选择");
        if (StringUtils.isNoneBlank(parentModule.getTreePath())) {
            treePath = parentModule.getTreePath() + parentModule.getId() + ",";
        } else {
            treePath = "," + parentModule.getId() + ",";
        }
        return treePath;
    }

    public List<Module> findModuleByGrade(Integer grade) {

        if (grade == null || grade < 0) {
            throw new ParamException("请选择层级");
        }
        return moduleDao.findModuleByGrade(grade);
    }

    public List<ModuleVO> findAll(Integer roleId) {
        List<ModuleVO>moduleList= moduleDao.findAll();
        List<Integer> roleModuleIds = moduleDao.findByRoleId(roleId);
        if(roleModuleIds==null||roleModuleIds.size()<1){
            return moduleList;
        }
        for (ModuleVO moduleVO:moduleList){
            if(roleModuleIds.contains(moduleVO.getId())){
                moduleVO.setChecked(true);
            }
        }
        return moduleList;

    }
}
