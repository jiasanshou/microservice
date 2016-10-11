package com.dtdream.microservice.core.biz.processors;

import com.dtdream.microservice.core.disruptor.Data;

/**
 * Created by 张三丰 on 2016-09-28.
 */
public interface Initializer extends AsynProcessor {
    void init(Data data) throws Exception;
}
