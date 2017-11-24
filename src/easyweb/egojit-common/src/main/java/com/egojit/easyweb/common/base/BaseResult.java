package com.egojit.easyweb.common.base;

import java.io.Serializable;

/**
 * Created by egojit on 2017/10/26.
 */
public class BaseResult implements Serializable {


    private static final long serialVersionUID = 1L;

    // 状态码：1成功，其他为失败
    public int code;

    // 成功为success，其他为失败原因
    public String message;

    // 数据结果集
    public Object data;

    public BaseResult(Object data){
        this(BaseResultCode.SUCCESS, data);
    }
    public BaseResult(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public BaseResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseResult(BaseResultCode resultCode, Object data) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data=data;
    }
    public boolean isSuccessful(){
        return BaseResultCode.SUCCESS.getCode() == this.code;
    }
    @Override
    public String toString() {
        return "BaseResult [code=" + code + ", message=" + message + ", data=" + data + "]";

    }

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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
