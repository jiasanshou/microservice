package com.dtdream.microservice.core.biz.processors;

import com.dtdream.microservice.core.disruptor.Data;
import com.lmax.disruptor.EventHandler;

/**
 * Created by 张三丰 on 2016-09-28.
 */
public interface AsynProcessor extends EventHandler<Data> {
}
