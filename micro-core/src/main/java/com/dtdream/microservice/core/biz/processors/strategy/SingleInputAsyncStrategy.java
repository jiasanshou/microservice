package com.dtdream.microservice.core.biz.processors.strategy;

import com.dtdream.microservice.core.biz.processors.Handler;
import com.dtdream.microservice.core.biz.processors.Initializer;
import com.dtdream.microservice.core.biz.processors.Validator;
import com.dtdream.microservice.core.disruptor.Data;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.List;

/**
 * Created by 张三丰 on 2016-09-30.
 */
public class SingleInputAsyncStrategy extends DisruptorStrategySupport {
    public void start(List<Initializer> initializers, List<Validator> validators, Handler hander) {
        doStart(initializers, validators, hander, ProducerType.SINGLE);
    }

    public void process(final Data data) {
        doProcess(data);
    }
}
