package com.shsxt.crm.service;

import com.shsxt.crm.constant.DevResult;
import com.shsxt.crm.dao.CusDevPlanDao;
import com.shsxt.crm.model.CusDevPlan;
import com.shsxt.crm.util.AssertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CusDevPlanService {
    @Autowired
    CusDevPlanDao cusDevPlanDao;
    @Autowired
    SaleChanceService saleChanceService;

    public Map<String,Object> findBySaleChanceId(Integer saleChanceId)
    {
        AssertUtil.intIsNotEmpty(saleChanceId,"请选择营销机会进行操作");
        List<CusDevPlan> cusDevPlanList=cusDevPlanDao.findBySaleChanceId(saleChanceId);
        Map<String,Object> result =new HashMap<>() ;
        result.put("rows",cusDevPlanList);
        return result;

    }
    public void add(CusDevPlan cusDevPlan)
    {
        checkState(cusDevPlan);
        cusDevPlanDao.insert(cusDevPlan);
        saleChanceService.updateDevResult(cusDevPlan.getSaleChanceId(), DevResult.DEVING.getDevResult());
    }

    public void update(CusDevPlan cusDevPlan)
    {
        checkState(cusDevPlan);
        cusDevPlanDao.update(cusDevPlan);
    }
    public void delete(Integer id)
    {
        AssertUtil.intIsNotEmpty(id, "请选择记录进行删除");
        cusDevPlanDao.delete(id);
    }

    public void checkState(CusDevPlan cusDevPlan)
    {
        Integer saleChanceId = cusDevPlan.getSaleChanceId();
        AssertUtil.intIsNotEmpty(saleChanceId, "请选择营销机会进行操作");
        Date planDate = cusDevPlan.getPlanDate();
        AssertUtil.notNull(planDate, "请选择计划日期");
        String planItem = cusDevPlan.getPlanItem();
        AssertUtil.isNotEmpty(planItem, "请输入计划内容");
        String exeAffect = cusDevPlan.getExeAffect();
        AssertUtil.isNotEmpty(exeAffect, "请输入执行效果");

    }
}
