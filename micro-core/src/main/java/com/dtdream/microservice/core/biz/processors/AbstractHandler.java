package com.dtdream.microservice.core.biz.processors;

import com.dtdream.microservice.core.disruptor.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 张三丰 on 2016-09-28.
 */
public abstract class AbstractHandler implements Handler {
    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractHandler.class);

    public void onEvent(Data event, long sequence, boolean endOfBatch) throws Exception {
        try {
            if (event.hasError()) {
                return;
            }
            handler(event);
        } catch (Exception e) {
            LOGGER.error("handler",e);
        } finally {
            event.getResult().setEventCopy(event.clone());
            event.getLatch().countDown();
        }
    }
}
