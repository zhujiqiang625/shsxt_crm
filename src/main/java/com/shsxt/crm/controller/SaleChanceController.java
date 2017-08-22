package com.shsxt.crm.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.shsxt.crm.annotation.RequirePermissions;
import com.shsxt.crm.annotation.SystemLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shsxt.crm.base.BaseController;
import com.shsxt.crm.base.ResultInfo;
import com.shsxt.crm.dto.SaleChanceDto;
import com.shsxt.crm.dto.SaleChanceQuery;
import com.shsxt.crm.model.SaleChance;
import com.shsxt.crm.service.SaleChanceService;
import com.shsxt.crm.util.CookieUtil;

@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {
	
	@Autowired
	private SaleChanceService saleChanceService;
	
	@RequestMapping("index")
	public String index(Integer state) {
		if (state==null)
		return "sale_chance";
		else {
			return "sale_chance_assignment";
		}
	}
	
	@RequestMapping("list")
	@ResponseBody
	public Map<String, Object> list(SaleChanceQuery query ) {
		Map<String, Object> result = saleChanceService.selectForPage(query);
		return result;
	}
	@RequestMapping("add")
	@ResponseBody
	@RequirePermissions(permission="101001")

	public ResultInfo add(SaleChanceDto saleChanceDto, HttpServletRequest request) {
		String userName = CookieUtil.getCookieValue(request, "userName");
		saleChanceService.add(saleChanceDto, userName);
		return success("添加成功");
	}
	
	@RequestMapping("update")
	@ResponseBody
	@RequirePermissions(permission="101002")
	public ResultInfo update(SaleChance saleChance) {
		saleChanceService.update(saleChance);
		return success("修改成功");
	}
	
	@RequestMapping("delete")
	@ResponseBody
	@SystemLog("SaleChanceControlle删除")
	public ResultInfo delete(String ids) {
		saleChanceService.delete(ids);
		return success("删除成功");
	}
}
