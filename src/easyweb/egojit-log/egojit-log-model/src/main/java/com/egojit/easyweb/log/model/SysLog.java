package com.egojit.easyweb.log.model;

import com.egojit.easyweb.common.base.BaseEntity;
import com.egojit.easyweb.common.utils.StringUtils;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

@Table(name = "sys_log")
public class SysLog extends BaseEntity {


    /**
     * 日志类型
     */
    private String type;

    /**
     * 日志标题
     */
    private String title;



    /**
     * 操作IP地址
     */
    @Column(name = "remote_addr")
    private String remoteAddr;

    /**
     * 用户代理
     */
    @Column(name = "user_agent")
    private String userAgent;

    /**
     * 请求URI
     */
    @Column(name = "request_uri")
    private String requestUri;

    /**
     * 操作方式
     */
    private String method;

    /**
     * 操作提交的数据
     */
    private String params;

    /**
     * 异常信息
     */
    private String exception;



    // 日志类型（1：接入日志；2：错误日志）
    public static final String TYPE_ACCESS = "1";
    public static final String TYPE_EXCEPTION = "2";

    /**
     * 获取日志类型
     *
     * @return type - 日志类型
     */
    public String getType() {
        return type;
    }

    /**
     * 设置日志类型
     *
     * @param type 日志类型
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取日志标题
     *
     * @return title - 日志标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置日志标题
     *
     * @param title 日志标题
     */
    public void setTitle(String title) {
        this.title = title;
    }





    /**
     * 获取操作IP地址
     *
     * @return remote_addr - 操作IP地址
     */
    public String getRemoteAddr() {
        return remoteAddr;
    }

    /**
     * 设置操作IP地址
     *
     * @param remoteAddr 操作IP地址
     */
    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    /**
     * 获取用户代理
     *
     * @return user_agent - 用户代理
     */
    public String getUserAgent() {
        return userAgent;
    }

    /**
     * 设置用户代理
     *
     * @param userAgent 用户代理
     */
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    /**
     * 获取请求URI
     *
     * @return request_uri - 请求URI
     */
    public String getRequestUri() {
        return requestUri;
    }

    /**
     * 设置请求URI
     *
     * @param requestUri 请求URI
     */
    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    /**
     * 获取操作方式
     *
     * @return method - 操作方式
     */
    public String getMethod() {
        return method;
    }

    /**
     * 设置操作方式
     *
     * @param method 操作方式
     */
    public void setMethod(String method) {
        this.method = method;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }
    /**
     * 设置操作提交的数据
     *
     * @param params 操作提交的数据
     */
    /**
     * 设置请求参数
     * @param paramMap
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void setParams(Map paramMap){
        if (paramMap == null){
            return;
        }
        StringBuilder params = new StringBuilder();
        for (Map.Entry<String, String[]> param : ((Map<String, String[]>)paramMap).entrySet()){
            params.append(("".equals(params.toString()) ? "" : "&") + param.getKey() + "=");
            String paramValue = (param.getValue() != null && param.getValue().length > 0 ? param.getValue()[0] : "");
            params.append(StringUtils.abbr(StringUtils.endsWithIgnoreCase(param.getKey(), "password") ? "" : paramValue, 100));
        }
        this.params = params.toString();
    }

    /**
     * 获取异常信息
     *
     * @return exception - 异常信息
     */
    public String getException() {
        return exception;
    }

    /**
     * 设置异常信息
     *
     * @param exception 异常信息
     */
    public void setException(String exception) {
        this.exception = exception;
    }
}