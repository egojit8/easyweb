package com.egojit.easyweb.common.base;

import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.session.InvalidSessionException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by egojit on 2017/11/24.
 */
public class BaseApiController extends BaseController {

    @Override
    public Object exception(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        BaseResult result=new BaseResult(BaseResultCode.EXCEPTION,exception.getMessage());
        return result;
    }
}
