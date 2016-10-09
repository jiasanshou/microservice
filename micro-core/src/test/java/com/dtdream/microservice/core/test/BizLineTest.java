package com.dtdream.microservice.core.test;

import com.dtdream.microservice.core.biz.BizLine;
import com.dtdream.microservice.core.biz.processors.AbstractHandler;
import com.dtdream.microservice.core.biz.processors.strategy.CurrentThreadStrategy;
import com.dtdream.microservice.core.biz.processors.strategy.MultiInputAsyncStrategy;
import com.dtdream.microservice.core.biz.processors.strategy.SingleInputAsyncStrategy;
import com.dtdream.microservice.core.disruptor.Data;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.TimeoutException;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by 张三丰 on 2016-09-29.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BizLineTest {

    public static final int N_THREADS = 3;
    ExecutorService executorService = Executors.newFixedThreadPool(N_THREADS);
    public static final int SIZE = 200 * 10000;

    @Test
    public void test1() throws InterruptedException, TimeoutException {
        final BizLine bizLine = BizLine.create(new MultiInputAsyncStrategy());
        bizLine.register(new AbstractHandler() {
            public void handler(Data data) throws Exception {
//                System.out.println(Thread.currentThread().getName());
                data.put("11", "11");
            }
        });
        bizLine.start();
        int size = SIZE;
        final CountDownLatch latch = new CountDownLatch(size);
        long s = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            executorService.execute(new Runnable() {
                public void run() {
                    Data data = Data.create();
                    data.put("time", 0L);
                    bizLine.process(data);
                    latch.countDown();
                }
            });
        }
        latch.await();
        bizLine.close();
        long e = System.currentTimeMillis();
        float l = (e - s) / 1000F;
        System.out.println("微服务框架（多生产者）" + (size / l) / 10000 + "万每秒");
    }

    @Test
    public void test2() throws InterruptedException, TimeoutException {
        final BizLine bizLine = BizLine.create(new SingleInputAsyncStrategy());
        bizLine.register(new AbstractHandler() {
            public void handler(Data data) throws Exception {
//                System.out.println(Thread.currentThread().getName());
                data.put("11", "11");
            }
        });
        bizLine.start();
        int size = SIZE;
        long s = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            Data data = Data.create();
            data.put("time", 0L);
            bizLine.process(data);
        }
        bizLine.close();
        long e = System.currentTimeMillis();
        float l = (e - s) / 1000F;
        System.out.println("微服务框架（单生产者）" + (size / l) / 10000 + "万每秒");
    }
    @Test
    public void test3() throws InterruptedException, TimeoutException {
        final BizLine bizLine = BizLine.create(new CurrentThreadStrategy());
        bizLine.register(new AbstractHandler() {
            public void handler(Data data) throws Exception {
//                System.out.println(Thread.currentThread().getName());
                data.put("11", "11");
            }
        });
        bizLine.start();
        int size = SIZE;
        long s = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            Data data = Data.create();
            data.put("time", 0L);
            bizLine.process(data);
        }
        bizLine.close();
        long e = System.currentTimeMillis();
        float l = (e - s) / 1000F;
        System.out.println("微服务框架（当前线程）" + (size / l) / 10000 + "万每秒");
    }
    @Test
    public void testDisruptor() throws InterruptedException, TimeoutException {
//        System.out.println(Runtime.getRuntime().availableProcessors());
        final Disruptor<SimpleEvent> disruptor = new Disruptor<SimpleEvent>(new SimpleEventFactory(), 1024,
                Executors.newCachedThreadPool(), ProducerType.SINGLE, new BlockingWaitStrategy());
        disruptor.handleEventsWith(new EventHandler<SimpleEvent>() {
            public void onEvent(SimpleEvent event, long sequence, boolean endOfBatch) throws Exception {
                event.data++;
            }
        });
        disruptor.start();
        int size = SIZE;
        long s = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            disruptor.publishEvent(new EventTranslator<SimpleEvent>() {
                public void translateTo(SimpleEvent event, long sequence) {
                    event.data = 0;
                }
            });
        }
        disruptor.shutdown(20, TimeUnit.SECONDS);
        long e = System.currentTimeMillis();
        float l = (e - s) / 1000F;
        System.out.println("原生框架(单生产者)" + (size / l) / 10000 + "万每秒");
    }

    @Test
    public void testDisruptorM() throws InterruptedException, TimeoutException {
        final Disruptor<SimpleEvent> disruptor = new Disruptor<SimpleEvent>(new SimpleEventFactory(), 1024,
                Executors.newCachedThreadPool(), ProducerType.MULTI, new BlockingWaitStrategy());
        disruptor.handleEventsWith(new EventHandler<SimpleEvent>() {
            public void onEvent(SimpleEvent event, long sequence, boolean endOfBatch) throws Exception {
                event.data++;
            }
        });
        disruptor.start();
        final CountDownLatch latch = new CountDownLatch(SIZE);
        long s = System.currentTimeMillis();
        for (int i = 0; i < SIZE; i++) {
            executorService.execute(new Runnable() {
                public void run() {
                    disruptor.publishEvent(new EventTranslator<SimpleEvent>() {
                        public void translateTo(SimpleEvent event, long sequence) {
                            event.data = 0;
                        }
                    });
                    latch.countDown();
                }
            });
        }
        latch.await();
        disruptor.shutdown(20, TimeUnit.SECONDS);
        long e = System.currentTimeMillis();
        float l = (e - s) / 1000F;
        System.out.println("原生框架(多生产者)" + (SIZE / l) / 10000 + "万每秒");
    }
    static class SimpleEvent {
        public long data;
    }

    static class SimpleEventFactory implements EventFactory<SimpleEvent> {

        public SimpleEvent newInstance() {
            return new SimpleEvent();
        }
    }
}
