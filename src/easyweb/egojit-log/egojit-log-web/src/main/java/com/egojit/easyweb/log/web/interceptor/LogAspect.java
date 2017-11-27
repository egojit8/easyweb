package com.egojit.easyweb.log.web.interceptor;
import com.egojit.easyweb.log.model.SysLog;
import com.egojit.easyweb.log.web.utils.LogUtils;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 日志记录AOP实现 Created by ZhangShuegojit on 2017/3/14.
 */
@Aspect
@Component
public class LogAspect {

	private static Logger _log = LoggerFactory.getLogger(LogAspect.class);

	// 开始时间
	private long startTime = 0L;
	// 结束时间
	private long endTime = 0L;


	@Before("execution(* *..controller..*.*(..))")
	public void doBeforeInServiceLayer(JoinPoint joinPoint) {
		_log.debug("doBeforeInServiceLayer");
		startTime = System.currentTimeMillis();
	}

	@After("execution(* *..controller..*.*(..))")
	public void doAfterInServiceLayer(JoinPoint joinPoint) {
		_log.debug("doAfterInServiceLayer");
	}

	@Around("execution(* *..controller..*.*(..))")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		// 获取request
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
		HttpServletRequest request = servletRequestAttributes.getRequest();
		SysLog log=new SysLog();
		// 从注解中获取操作名称、获取响应结果
		Object result = pjp.proceed();
		Signature signature = pjp.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		Method method = methodSignature.getMethod();
		log.setTitle("未知");
		if (method.isAnnotationPresent(ApiOperation.class)) {
			ApiOperation logA = method.getAnnotation(ApiOperation.class);
			log.setTitle(logA.value());

		}

//		if (method.isAnnotationPresent(RequiresPermissions.class)) {
//			RequiresPermissions requiresPermissions = method.getAnnotation(RequiresPermissions.class);
//			String[] permissions = requiresPermissions.value();
//			if (permissions.length > 0) {
//				upmsLog.setPermissions(permissions[0]);
//			}
//		}

		LogUtils.saveLog(request,log);
		endTime = System.currentTimeMillis();
		_log.debug("doAround>>>result={},耗时：{}", result, endTime - startTime);
//		upmsLog.setBasePath(RequestUtil.getBasePath(request));
//		upmsLog.setIp(RequestUtil.getIpAddr(request));
//		upmsLog.setMethod(request.getMethod());
//		if (request.getMethod().equalsIgnoreCase("GET")) {
//			upmsLog.setParameter(request.getQueryString());
//		} else {
//			upmsLog.setParameter(ObjectUtils.toString(request.getParameterMap()));
//		}
//		upmsLog.setLogId(StringUtil.guid());
//		upmsLog.setResult(ObjectUtils.toString(result));
//		upmsLog.setSpendTime((int) (endTime - startTime));
//		upmsLog.setStartTime(startTime);
//		upmsLog.setUri(request.getRequestURI());
//		upmsLog.setUrl(ObjectUtils.toString(request.getRequestURL()));
//		upmsLog.setUserAgent(request.getHeader("User-Agent"));
//		upmsLog.setUsername(ObjectUtils.toString(request.getUserPrincipal()));
//		upmsApiService.insertUpmsLogSelective(upmsLog);
		return result;
	}

}
