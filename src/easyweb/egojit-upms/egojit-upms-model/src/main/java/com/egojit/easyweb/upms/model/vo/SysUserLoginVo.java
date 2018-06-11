package com.egojit.easyweb.upms.model.vo;

import javax.persistence.Column;

/**
 * Description：登录参数
 * Auther：高露
 * Q Q:408365330
 * Company: 鼎斗信息技术有限公司
 * Time:2018-5-8
 */
public class SysUserLoginVo {

    /**
     * 登录名
     */
    private String loginName;

    /**
     * 密码
     */
    private String password;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
