package com.dtdream.microservice.core.disruptor;

import com.alibaba.fastjson.JSONObject;
import com.lmax.disruptor.EventFactory;

import java.util.concurrent.CountDownLatch;

/**
 * Created by 张三丰 on 2016-09-28.
 */
public final class Data extends JSONObject  implements EventFactory<Data>,Cloneable{

    private Data() {
    }

    private Exception exception;
    private Result<Data> result;
    public static Data create() {
        return new Data();
    }

    private CountDownLatch latch;

    public CountDownLatch getLatch() {
        return latch;
    }

    public void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }

    public Exception getException() {
        return exception;
    }

    public boolean hasError() {
        return getException() != null;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public Result<Data> getResult() {
        return result;
    }

    public void setResult(Result<Data> result) {
        this.result = result;
    }

    public Data clone() {
        Data data = Data.create();
        data.setException(getException());
        data.setLatch(getLatch());
        data.setResult(getResult());
        data.putAll(this);
        return data;
    }

    @Override
    public void clear() {
        super.clear();
        setLatch(null);
    }

    public Data newInstance() {
        return Data.create();
    }
}
