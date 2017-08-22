package com.shsxt.crm.proxy;


import com.alibaba.fastjson.JSON;
import com.shsxt.crm.annotation.SystemLog;
import com.shsxt.crm.model.Log;
import com.shsxt.crm.service.LogService;
import com.shsxt.crm.util.LoginUserUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

public class SystemLogProxy {

    @Autowired
    private LogService logService;
    @Autowired
    private HttpServletRequest request;

    private static Logger logger = LoggerFactory.getLogger(SystemLogProxy.class);

    @Pointcut("@annotation(com.shsxt.crm.annotation.SystemLog)")
    public void pointcut(){

    }
    @Around("pointcut()&&@annotation(systemLog)")
    public Object aroundMethod(ProceedingJoinPoint pjp, SystemLog systemLog)
            throws Throwable {
        Log log=new Log();
        log.setCreateDate(new Date());
        log.setCreateMan(LoginUserUtil.releaseUserNameFromCookie(request));
        log.setDescription(systemLog.value());
        MethodSignature signature= (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        log.setMethod(method.toString());
        Map<String, String[]> parameterMap = request.getParameterMap();
        log.setParams(JSON.toJSONString(parameterMap));
        String remoteAddr = request.getRemoteAddr();
        log.setRequestIp(remoteAddr);
        long beforTime=new Date().getTime();
        Object obj=pjp.proceed();
        long endTime=new Date().getTime();
        logger.info("执行方法：{}, 执行时间：{}", method.getName(), endTime - beforTime);
        if(obj!=null){
            log.setResult(JSON.toJSONString(obj));
        }
        log.setType(0);
        logService.addLog(log);

        return obj;
    }

}
