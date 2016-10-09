package com.dtdream.microservice.core.biz.processors.strategy;

import com.dtdream.microservice.core.biz.processors.EmptyHanlder;
import com.dtdream.microservice.core.biz.processors.Handler;
import com.dtdream.microservice.core.biz.processors.Initializer;
import com.dtdream.microservice.core.biz.processors.Validator;
import com.dtdream.microservice.core.disruptor.Data;
import com.dtdream.microservice.core.disruptor.Result;
import com.google.common.base.Preconditions;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.TimeoutException;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by 张三丰 on 2016-09-30.
 */
public abstract class DisruptorStrategySupport extends AbstractStrategy {
    protected Disruptor<Data> disruptor;
    protected ExecutorService executor;

    protected void doStart(List<Initializer> initializers, List<Validator> validators, Handler hander, ProducerType single) {
        WaitStrategy strategy = initializers.isEmpty() && validators.isEmpty() ?
                new BlockingWaitStrategy() : new YieldingWaitStrategy();
        executor = Executors.newCachedThreadPool();
        disruptor = new Disruptor<Data>(Data.create(), 1024,
                executor, single, strategy);
        EventHandler<Data>[] initHandlers = initializers.toArray(new EventHandler[0]);
        EventHandler<Data>[] valiHandlers = validators.toArray(new EventHandler[0]);
        if (hander == null) {
            hander = new EmptyHanlder();
        }
        if (!initializers.isEmpty() && !validators.isEmpty()) {
            disruptor.handleEventsWith(initHandlers).then(valiHandlers).then(hander);
        } else if (!validators.isEmpty()) {
            disruptor.handleEventsWith(valiHandlers).then(hander);
        } else if (!initializers.isEmpty()) {
            disruptor.handleEventsWith(initHandlers).then(hander);
        } else {
            disruptor.handleEventsWith(hander);
        }
        disruptor.start();
    }

    protected Result<Data> doProcess(final Data data) {
        Preconditions.checkNotNull(data);
        Result<Data> result = Result.create();
        data.setResult(result);
        disruptor.publishEvent(new EventTranslatorOneArg<Data, Result<Data>>() {
            public void translateTo(Data event, long sequence, Result<Data> re) {
                event.clear();
                event.putAll(data);
                event.setResult(re);
                event.setLatch(re.getLatch());
            }
        }, result);
        return result;
    }

    public void close() throws TimeoutException {
        disruptor.shutdown(10, TimeUnit.SECONDS);
        executor.shutdown();
    }
}
