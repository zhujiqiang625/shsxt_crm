package com.shsxt.crm.controller;

import com.shsxt.crm.base.BaseController;
import com.shsxt.crm.base.BaseQuery;
import com.shsxt.crm.base.ResultInfo;
import com.shsxt.crm.model.Module;
import com.shsxt.crm.service.ModuleService;
import com.shsxt.crm.service.PermissionService;
import com.shsxt.crm.vo.ModuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("module")
public class LinkManController extends BaseController{
    @Autowired
    private ModuleService moduleService;
    @Autowired
    PermissionService permissionService;


    @RequestMapping("index")
    public String index() {
        return "module";

    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> list(BaseQuery query) {
        Map<String, Object> result = moduleService.selectForPage(query);
        return result;
    }

    @RequestMapping("add")
    @ResponseBody
    public ResultInfo add(Module module) {
         moduleService.add(module);
         return success("添加成功");

    }

    @RequestMapping("update")
    @ResponseBody
    public ResultInfo update(Module module) {
        moduleService.update(module);
        return success("修改成功");
    }

    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo delete(String ids) {
        moduleService.delete(ids);
        return success("删除成功");

    }
    @RequestMapping("find_module_by_grade")
    @ResponseBody
    public List<Module> findModuleByGrade(Integer grade) {
        return moduleService.findModuleByGrade(grade);

    }
    @RequestMapping("relate_permission")

    public String relatePermission(Integer roleId, Model model) {
        List<ModuleVO> modules = moduleService.findAll(roleId);
        model.addAttribute("modules", modules);
        model.addAttribute("roleId", roleId);
        return "relate_permission";
    }

    @RequestMapping("dorelate")
    @ResponseBody
    public ResultInfo dorelate(Integer roleId, Integer moduleId, boolean checked){
        permissionService.addDoRelate(roleId,moduleId,checked);
        return success("操作成功");
    }




}
