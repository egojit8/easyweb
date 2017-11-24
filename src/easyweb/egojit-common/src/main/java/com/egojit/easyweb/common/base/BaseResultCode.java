package com.egojit.easyweb.common.base;

/**
 * Created by egojit on 2017/10/26.
 */
public enum BaseResultCode {

    SUCCESS(200, "调用接口成功"),
    EXCEPTION(500, "接口内部异常"),
    ARGUMENT(400, "接口参数不合法"),
    UNAUTHORIZED(401, "接口调用非法"),
    UNAUTH(403, "无权限"),
    ILLEGAL_PAGINATION(406, "分页参数非法");



    private BaseResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;

    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}