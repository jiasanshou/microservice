package com.dtdream.microservice.core.common.exception;

/**
 * Created by 张三丰 on 2016-09-28.
 */
public class MicroServiceException extends RuntimeException {
    private ErrorMsg msg;
    public MicroServiceException(Throwable cause) {
        super(cause);
    }
    public MicroServiceException(ErrorMsg message) {
        super(message.getMsg());
        msg = message;
    }

    public MicroServiceException(ErrorMsg message, Throwable cause) {
        super(message.getMsg(), cause);
        msg = message;
    }

    public ErrorMsg getMsg() {
        return msg;
    }

    public void setMsg(ErrorMsg msg) {
        this.msg = msg;
    }
}
