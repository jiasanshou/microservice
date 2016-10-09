package com.dtdream.microservice.restful.common.exception;

/**
 * Created by 张三丰 on 2016-09-30.
 */
public enum RestfulError {
    SYS_ERROR("系统异常"),
    UNSUPPORTED_METHOD("不支持的请求方法"),
    PATH_REPEAT("API路径重复"),
    UNSUPPORTED_PATH("不支持的请求路径"),
    ;
    private String msg;

    RestfulError(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
