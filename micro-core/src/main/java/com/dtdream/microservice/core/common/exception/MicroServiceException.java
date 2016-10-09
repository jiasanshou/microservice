package com.dtdream.microservice.core.common.exception;

/**
 * Created by 张三丰 on 2016-09-28.
 */
public class MicroServiceException extends RuntimeException {
    public MicroServiceException(Throwable cause) {
        super(cause);
    }
    public MicroServiceException(ErrorMsg message) {
        super(message.getMsg());
    }

    public MicroServiceException(ErrorMsg message, Throwable cause) {
        super(message.getMsg(), cause);
    }

}
