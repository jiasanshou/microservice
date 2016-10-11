package com.dtdream.microservice.restful.api;

import com.dtdream.microservice.restful.common.RestfulResult;
import com.dtdream.microservice.restful.common.exception.RestfulError;
import com.dtdream.microservice.restful.common.exception.RestfulException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 张三丰 on 2016-10-08.
 */
public abstract class RestfulApi implements Api {
    private static Map<String, RestfulApi> apiMap = new ConcurrentHashMap<>();

    public RestfulApi() {
        super();
        String supportedPath = supportedPath();
        if (apiMap.containsKey(supportedPath)) {
            throw new RestfulException(RestfulError.PATH_REPEAT);
        }
        apiMap.put(supportedPath, this);
    }

    protected abstract String supportedPath();

    public static RestfulApi getApi(String path) {
        RestfulApi restfulApi = apiMap.get(path);
        if (restfulApi == null) {
            throw new RestfulException(RestfulError.UNSUPPORTED_PATH);
        }
        return restfulApi;
    }

    public void get(RestfulResult result, HttpServletRequest request, HttpServletResponse response) {
        unSupport();
    }

    private void unSupport() {
        throw new RestfulException(RestfulError.UNSUPPORTED_METHOD);
    }

    public void post(RestfulResult result, HttpServletRequest request, HttpServletResponse response) {
        unSupport();
    }

    public void put(RestfulResult result, HttpServletRequest request, HttpServletResponse response) {
        unSupport();
    }

    public void delete(RestfulResult result, HttpServletRequest request, HttpServletResponse response) {
        unSupport();
    }
}
