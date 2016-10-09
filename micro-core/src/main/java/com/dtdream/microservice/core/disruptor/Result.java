package com.dtdream.microservice.core.disruptor;

import java.util.concurrent.CountDownLatch;

/**
 * Created by 张三丰 on 2016-09-28.
 */
public class Result<E extends Data> {

    private CountDownLatch latch = new CountDownLatch(1);
    private E eventCopy;

    private Result() {
    }

    public static  <E extends Data> Result<E> create() {
        return new Result<E>();
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public E getEventCopy() {
        return eventCopy;
    }

    public void setEventCopy(E eventCopy) {
        this.eventCopy = eventCopy;
    }

    public void waitForFinish() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            //temp ignore
        }
    }
}
