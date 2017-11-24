package com.egojit.easyweb.common.base;

import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.session.InvalidSessionException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by egojit on 2017/11/24.
 */
public class BaseWebController extends BaseController {
    @Override
    public Object exception(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        request.setAttribute("ex", exception);
        if (null != request.getHeader("X-Requested-With")
                && request.getHeader("X-Requested-With").equalsIgnoreCase("XMLHttpRequest")) {
            request.setAttribute("requestHeader", "ajax");
            BaseResult result=new BaseResult(BaseResultCode.EXCEPTION,exception.getMessage());
            return result;
        }else {
            // shiro没有权限异常
            if (exception instanceof UnauthorizedException) {
                return "/403";
            }
            // shiro会话已过期异常
            if (exception instanceof InvalidSessionException) {
                return "/500";
            }
            return "/500";
        }
    }
}
