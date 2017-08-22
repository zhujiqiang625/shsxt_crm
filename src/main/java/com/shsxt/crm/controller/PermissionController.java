package com.shsxt.crm.controller;

import com.shsxt.crm.base.BaseController;
import com.shsxt.crm.base.ResultInfo;
import com.shsxt.crm.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.transform.Result;

@RequestMapping("permission")
public class PermissionController extends BaseController{
    @Autowired
    PermissionService permissionService;
    @RequestMapping("addDoRelate")
    @ResponseBody
    public ResultInfo addDoRelate(Integer roleId, Integer moduleId, boolean checked){
        permissionService.addDoRelate(roleId,moduleId,checked);
        return success("操作成功");
    }
}
