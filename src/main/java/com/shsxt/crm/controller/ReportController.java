package com.shsxt.crm.controller;

import com.shsxt.crm.base.BaseController;
import com.shsxt.crm.base.ResultInfo;
import com.shsxt.crm.dto.KhgxQuery;
import com.shsxt.crm.service.CustomerService;
import com.shsxt.crm.vo.CustomerGc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("report")
public class ReportController extends BaseController {
	
	@Autowired
	private CustomerService customerService;
	
	@RequestMapping("{type}")
	public String index(@PathVariable Integer type) {
		
		switch (type) {
		case 0:
			return "khgxfx";
		case 1:
			return "khgcfx";
		case 2:
			
			break;
		case 3:
			
			break;
		}
		
		return "khgxfx";
	}
	
	@RequestMapping("khgxfx")
	@ResponseBody
	public Map<String, Object> findKhgx(KhgxQuery query) {
		Map<String, Object> result = customerService.findKhgx(query);
		return result;
		//BeanFactory;
	}
	
	@RequestMapping("khgcfx")
	@ResponseBody
	public ResultInfo findKhgc() {
		List<CustomerGc> result = customerService.findKhgc();
		return success(result);
	}
	
	
}
