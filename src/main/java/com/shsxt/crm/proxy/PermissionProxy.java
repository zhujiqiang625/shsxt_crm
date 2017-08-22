package com.shsxt.crm.proxy;

import com.shsxt.crm.annotation.RequirePermissions;
import com.shsxt.crm.constant.Constant;
import com.shsxt.crm.exception.AuthException;
import com.shsxt.crm.exception.LoginException;
import com.shsxt.crm.service.PermissionService;
import com.shsxt.crm.util.LoginUserUtil;
import jdk.nashorn.internal.scripts.JO;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.List;

@Component
@Aspect
public class PermissionProxy {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpSession session;
    @Autowired
    private PermissionService permissionService;

    @Pointcut("execution(* com.*.*.controller.*.*(..))")
    public void pointcut(){

    }
    @Before("pointcut()")
    public void perMethod(JoinPoint joinPoint){
        String  uri=request.getRequestURI();
        if("/index".equals(uri)||"/user/login".equals(uri)){
            return;
        }
        List<String> userPermissions = buildPermission();
        MethodSignature methodSignature= (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        RequirePermissions requirePermissions=method.getAnnotation(RequirePermissions.class);
        if(requirePermissions==null){
            return;
        }
        if(!userPermissions.contains(requirePermissions.permission()));
        {
            throw new AuthException(-1,"权限认证失败");
        }


    }

    private List<String> buildPermission() {
        Integer userId= LoginUserUtil.releaseUserIdFromCookie(request);
        if(userId==null){
            throw new LoginException(201, "请登录");
        }
        List<String> userPermissions= (List<String>) session.getAttribute(Constant.USER_PERMISSION_SESSION_KEY);
        if(userPermissions!=null){
            return userPermissions;
        }
        List<String> userPermissions1 = permissionService.findUserPermissions(userId);
        if(userPermissions==null||userPermissions.size()<1){
            throw new AuthException(-1,"权限认证失败");
        }
        session.setAttribute(Constant.USER_PERMISSION_SESSION_KEY,userPermissions);
        return userPermissions;



    }

}
