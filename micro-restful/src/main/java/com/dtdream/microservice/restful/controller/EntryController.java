package com.dtdream.microservice.restful.controller;

import com.dtdream.microservice.restful.common.RestfulResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 张三丰 on 2016-09-30.
 */
@RestController
@RequestMapping("module")
public class EntryController extends AbstractRestfulController {

    public static final String TYPE = "type";
    public static final String RESOURCE_ID = "resourceId";

    @RequestMapping(method = RequestMethod.GET, value = "/{type}/{id}")
    public RestfulResult processGet(HttpServletRequest request, @PathVariable(TYPE) String bizType,
                           @PathVariable("id") String resourceId, HttpServletResponse response) {
        RestfulResult result = new RestfulResult();
        result.put(RESOURCE_ID, resourceId);
        process(bizType, result, request, response, RequestMethod.GET);
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{type}")
    public RestfulResult processGet(HttpServletRequest request, @PathVariable(TYPE) String bizType, HttpServletResponse response) {
        RestfulResult result = new RestfulResult();
        result.put(RESOURCE_ID, null);
        process(bizType, result, request, response, RequestMethod.GET);
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{type}")
    public RestfulResult processPost(HttpServletRequest request, @PathVariable(TYPE) String bizType, HttpServletResponse response) {
        RestfulResult result = new RestfulResult();
        result.put(RESOURCE_ID, null);
        process(bizType, result, request, response, RequestMethod.POST);
        return result;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{type}/{id}")
    public RestfulResult processPut(HttpServletRequest request, @PathVariable(TYPE) String bizType,
                           @PathVariable("id") String resourceId,
                           HttpServletResponse response) {
        RestfulResult result = new RestfulResult();
        result.put(RESOURCE_ID, resourceId);
        process(bizType, result, request, response, RequestMethod.PUT);
        return result;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{type}/{id}")
    public RestfulResult processDelete(HttpServletRequest request, @PathVariable(TYPE) String bizType,
                              @PathVariable("id") String resourceId,
                              HttpServletResponse response) {
        RestfulResult result = new RestfulResult();
        result.put(RESOURCE_ID, resourceId);
        process(bizType, result, request, response, RequestMethod.DELETE);
        return result;
    }
}
