package com.dtdream.microservice.core.biz.processors;

import com.dtdream.microservice.core.disruptor.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 张三丰 on 2016-09-28.
 */
public abstract class AbstractInitializer implements Initializer {
    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractInitializer.class);

    public void onEvent(Data event, long sequence, boolean endOfBatch) throws Exception {
        if (event.hasError()) {
            return;
        }
        init(event);
    }
}
