package com.dtdream.microservice.core.biz.processors.strategy;

import com.dtdream.microservice.core.common.exception.MicroServiceException;

/**
 * Created by 张三丰 on 2016-09-30.
 */
public abstract class AbstractStrategy implements ProcessStrategy {
    protected void processException(Exception e) {
        if (e instanceof MicroServiceException) {
            throw (MicroServiceException) e;
        } else {
            throw new MicroServiceException(e);
        }
    }
}
