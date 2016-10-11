package com.dtdream.microservice.core.biz.processors.strategy;

import com.dtdream.microservice.core.biz.processors.Handler;
import com.dtdream.microservice.core.biz.processors.Initializer;
import com.dtdream.microservice.core.biz.processors.Validator;
import com.dtdream.microservice.core.disruptor.Data;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by 张三丰 on 2016-09-30.
 */
public class CurrentThreadStrategy extends AbstractStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(CurrentThreadStrategy.class);
    private List<Initializer> initializers1;
    private List<Validator> validators1;
    private Handler handler1;

    public void start(List<Initializer> initializers, List<Validator> validators, Handler handler) {
        initializers1 = initializers;
        validators1 = validators;
        handler1 = handler;
    }

    public void process(Data data) {
        Preconditions.checkNotNull(data);
        try {
            for (Initializer initializer : initializers1) {
                initializer.init(data);
            }
            for (Validator validator : validators1) {
                validator.validate(data);
            }
            if (handler1 != null) {
                handler1.handler(data);
            }
        } catch (Exception e) {
            data.setException(e);
            LOGGER.error("", e);
            processException(e);
        }
    }

    public void close() {

    }
}
