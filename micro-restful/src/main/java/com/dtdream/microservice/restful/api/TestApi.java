package com.dtdream.microservice.restful.api;

import com.dtdream.microservice.restful.common.RestfulResult;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 张三丰 on 2016-10-08.
 */
@Component
public class TestApi extends RestfulApi {
    @Override
    protected String supportedPath() {
        return "test";
    }

    @Override
    public void get(RestfulResult result, HttpServletRequest request, HttpServletResponse response) {
        result.put("test", "test");
        result.put("thread", Thread.currentThread().getName());
    }

    @Override
    public void post(RestfulResult result, HttpServletRequest request, HttpServletResponse response) {
        result.put("test", "testpost");
        result.put("thread", Thread.currentThread().getName());
    }

    @Override
    public void put(RestfulResult result, HttpServletRequest request, HttpServletResponse response) {
        result.put("test", "testput");
        result.put("thread", Thread.currentThread().getName());
    }

    @Override
    public void delete(RestfulResult result, HttpServletRequest request, HttpServletResponse response) {
        result.put("test", "testdelete");
        result.put("thread", Thread.currentThread().getName());
    }
}
