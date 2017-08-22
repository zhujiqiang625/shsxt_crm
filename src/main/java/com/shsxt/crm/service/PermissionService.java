package com.shsxt.crm.service;

import com.shsxt.crm.dao.ModuleDao;
import com.shsxt.crm.dao.PermissionDao;
import com.shsxt.crm.model.Module;
import com.shsxt.crm.model.Permission;
import com.shsxt.crm.model.Role;
import com.shsxt.crm.util.AssertUtil;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PermissionService {
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ModuleDao moduleDao;
    //* 角色关联权限
    public void addDoRelate(Integer roleId, Integer moduleId, boolean checked) {
        AssertUtil.intIsNotEmpty(roleId,"请选择角色");
        AssertUtil.intIsNotEmpty(moduleId,"请选择类型");
        Module module = moduleDao.findById(moduleId);
        if(checked){
            bindPermission(roleId, moduleId, module);
        }else {
            // 先把自己解绑
            permissionDao.releaseModule(roleId, moduleId);
            // 解绑子模块
            releaseSonModules(module, roleId);
            // 解绑父模块
            releaseParentModule(roleId, module);
        }
        }
    // 解绑父模块
    private void releaseParentModule(Integer roleId, Module module) {

        String[] parentIds = module.getTreePath()
                .substring(1, module.getTreePath().lastIndexOf(",")).split(","); // ,1,2,
        for (int i=parentIds.length-1;i>=0;i--){//所有的父节点
            int fale=0;
            Module moduleDaoById = moduleDao.findById(Integer.parseInt(parentIds[i]));
            if(moduleDaoById==null)
                continue;
            String id=","+moduleDaoById.getId()+",";
        /*   if(moduleDaoById.getTreePath()==null)
                id=moduleDaoById.getId().toString();
            else id = moduleDaoById.getTreePath()+moduleDaoById.getId();*/
            List<Module>moduleList= moduleDao.findbyParentId(id);//父节点下的所有子节点
            for(Module module1:moduleList){
                Integer num = permissionDao.findByRoleIdModuleId(roleId, module1.getId());
                if(num>0){
                    fale=1;
                    break;
                }
            }
            if(fale==0) {
                permissionDao.releaseModule(roleId, moduleDaoById.getId());
            }
        }

    }
    // 解绑子模块
    private void releaseSonModules(Module module, Integer roleId) {
        String id;
        if(module.getTreePath()==null)
            id=module.getId().toString();
        else id = module.getTreePath()+module.getId();

        List<Module>moduleList= moduleDao.findbyParentId(id);
        for (int i=0;i<moduleList.size();i++){
            Module module1=moduleList.get(i);
            permissionDao.releaseModule(roleId, module1.getId());
        }
    }



    private void bindPermission(Integer roleId, Integer moduleId, Module module) {
        List<Permission> permissions=new ArrayList<>();

        build(roleId, moduleId, module.getOptValue(), permissions);
        //绑定父模块
        if(module.getParentId()!=null){
            Module moduleDaoById = moduleDao.findById(module.getParentId());
            if(moduleDaoById!=null)
                setParentPermission(roleId, module, permissions);
        }
        //绑定子模块
        bindSonModules(module, roleId, permissions);
        permissionDao.insertBatch(permissions);

    }
    //绑定子模块
    private void bindSonModules(Module module, Integer roleId, List<Permission> permissions) {
        String id=","+module.getId()+",";
/*
        if(module.getTreePath()==null)
            id=module.getId().toString();
        else id = module.getTreePath()+module.getId();
*/

       List<Module>moduleList= moduleDao.findbyParentId(id);
       for (Module module1:moduleList){
           build(roleId,module1.getId(),module1.getOptValue(),permissions);
       }
    }
    //绑定父模块
    private void setParentPermission(Integer roleId, Module module, List<Permission> permissions) {
        String[] parentIds = module.getTreePath()
                .substring(1, module.getTreePath().lastIndexOf(",")).split(","); // ,1,2,
        for (int i=0;i<parentIds.length;i++){
            Module moduleDaoById = moduleDao.findById(Integer.parseInt(parentIds[i]));
            Integer permission=permissionDao.findByRoleIdModuleId(roleId,moduleDaoById.getId());
            if(permission==0)
               build(roleId,moduleDaoById.getId(),moduleDaoById.getOptValue(),permissions);
        }
    }

    private void build(Integer roleId, Integer moduleId, String optValue, List<Permission> permissions) {
        Permission permission=new Permission();
        permission.setModuleId(moduleId);
        permission.setRoleId(roleId);
        permission.setAclValue(optValue);
        permissions.add(permission);
    }

    public List<String> findUserPermissions(Integer id) {
        return permissionDao.findUserPermissionsInteger(id);

    }
}
