package com.dtdream.microservice.restful.api;

import com.dtdream.microservice.restful.common.RestfulResult;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 张三丰 on 2016-09-30.
 */
@Component
public interface Api {
    /**处理get请求
     * @param result
     * @param request
     * @param response
     */
    void get(RestfulResult result, HttpServletRequest request, HttpServletResponse response);

    /**
     * 处理post请求
     * @param result
     * @param request
     * @param response
     */
    void post(RestfulResult result, HttpServletRequest request, HttpServletResponse response);

    /**
     * 处理put请求
     * @param result
     * @param request
     * @param response
     */
    void put(RestfulResult result, HttpServletRequest request, HttpServletResponse response);

    /**
     * 处理delete请求
     * @param result
     * @param request
     * @param response
     */
    void delete(RestfulResult result, HttpServletRequest request, HttpServletResponse response);
}
