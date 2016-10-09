package com.dtdream.microservice.restful.common.exception;

/**
 * Created by 张三丰 on 2016-09-30.
 */
public class RestfulException extends RuntimeException {
    private RestfulError restfulError;
    public RestfulException(RestfulError error) {
        super(error.getMsg());
        this.restfulError = error;
    }

    public RestfulException(RestfulError error, Throwable e) {
        super(error.getMsg(), e);
        this.restfulError = error;
    }

    public RestfulError getRestfulError() {
        return restfulError;
    }
}
