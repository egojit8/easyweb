package com.egojit.easyweb.common.base;

import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.session.InvalidSessionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by egojit on 2017/10/25.
 */
public abstract class BaseController {
    private final static Logger _log = LoggerFactory.getLogger(BaseController.class);

    /**
     * 统一异常处理
     * @param request
     * @param response
     * @param exception
     */
    public abstract Object exception(HttpServletRequest request, HttpServletResponse response, Exception exception);

    /**
     * 统一异常处理
     *
     * @param request
     * @param response
     * @param exception
     */
    @ExceptionHandler
    public Object exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        _log.error("统一异常处理：", exception);
       return this.exception(request,response,exception);
    }
}
