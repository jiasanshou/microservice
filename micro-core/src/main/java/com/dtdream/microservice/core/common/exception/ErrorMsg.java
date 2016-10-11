package com.dtdream.microservice.core.common.exception;

/**
 * Created by 张三丰 on 2016-09-29.
 */
public enum ErrorMsg {
    CREATE_INSTANCE_ERROR("创建数据异常"),
    SERVICE_RUNNING("服务已经启动"),
    SERVICE_NOT_RUNNING("服务未启动"),
    EXCEED_LIMIT("最多注册3个处理器"),
    SYS_ERROR("系统异常"),
    PROCESS_WRONG("请使用BizLineUtil.process方法"),
    HANDLER_REGISTED_ALREADY("Handler已注册"),;
    public String getMsg() {
        return msg;
    }
    private String msg;
    public void setMsg(String msg) {
        this.msg = msg;
    }
    ErrorMsg(String msg) {
        this.msg = msg;
    }
}
