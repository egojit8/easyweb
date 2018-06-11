package com.egojit.easyweb.upms.model.vo;

/**
 * Description：修改密码界面类
 * Auther：高露
 * Q Q:408365330
 * Company: 鼎斗信息技术有限公司
 * Time:2018-5-9
 */
public class SysUserEditPwdVo {
    /**
     * 登录名
     */
    private String loginName;

    /**
     * 旧密码
     */
    private String oldPwd;

    /**
     * 新密码
     */
    private String newPwd;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getOldPwd() {
        return oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }
}
