package com.shsxt.crm.controller;

import com.shsxt.crm.base.BaseController;
import com.shsxt.crm.base.BaseQuery;
import com.shsxt.crm.base.ResultInfo;
import com.shsxt.crm.model.Module;
import com.shsxt.crm.model.Role;
import com.shsxt.crm.service.ModuleService;
import com.shsxt.crm.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("role")
public class RoleContrller extends BaseController{
    @Autowired
    private RoleService roleService;

    @RequestMapping("index")
    public String index() {
        return "role";

    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> list(BaseQuery query) {
        Map<String, Object> result = roleService.selectForPage(query);
        return result;
    }

    @RequestMapping("add")
    @ResponseBody
    public ResultInfo add(Role role) {
        roleService.add(role);
        return success("添加成功");

    }

    @RequestMapping("update")
    @ResponseBody
    public ResultInfo update(Role role) {
        roleService.update(role);
        return success("修改成功");
    }

    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo delete(String ids) {
        roleService.delete(ids);
        return success("删除成功");
    }
    @RequestMapping("find_all")
    @ResponseBody
    public List<Role> findAll() {
        List<Role> result = roleService.findAll();
        return result;
    }



}
