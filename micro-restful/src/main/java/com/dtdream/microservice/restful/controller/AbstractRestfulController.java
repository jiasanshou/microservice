package com.dtdream.microservice.restful.controller;

import com.dtdream.microservice.core.biz.BizLine;
import com.dtdream.microservice.core.common.util.BizLineUtil;
import com.dtdream.microservice.restful.api.RestfulApi;
import com.dtdream.microservice.restful.common.RestfulResult;
import com.dtdream.microservice.restful.common.exception.RestfulError;
import com.dtdream.microservice.restful.common.exception.RestfulException;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * Created by 张三丰 on 2016-10-08.
 */
public abstract class AbstractRestfulController implements RestfulController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRestfulController.class);
    private BizLine BIZ_LINE = BizLineUtil.createSameThreadBizLine(new BizLineUtil.SameThreadCallBack() {
        public void doInSameThread(Object... params) {
            RestfulResult param = (RestfulResult) params[0];
            HttpServletRequest param1 = (HttpServletRequest) params[1];
            HttpServletResponse param2 = (HttpServletResponse) params[2];
            RequestMethod param3 = (RequestMethod) params[3];
            Object param4 = params[4];
            processDirectly(param, param1, param2, param3, (RestfulApi) param4);
        }
    });

    private Set<RequestMethod> methods = Sets.newHashSet(needSyncMethods());

//    @PostConstruct
//    public void init(){
//        BIZ_LINE.register(new SingleWriterHandler());
//        BIZ_LINE.start();
//    }

    public RequestMethod[] needSyncMethods() {
        return new RequestMethod[]{RequestMethod.PUT, RequestMethod.POST, RequestMethod.DELETE};
    }

    private RestfulApi getApi(String path) {
        return RestfulApi.getApi(path);
    }

    protected void process(String path, RestfulResult result, HttpServletRequest request,
                           HttpServletResponse response, RequestMethod method) {
        try {
            RestfulApi api = getApi(path);
            if (methods.contains(method)) {
                BizLineUtil.processInSameThread(BIZ_LINE, result, request, response, method, api);
            } else {
                processDirectly(result, request, response, method, api);
            }
        } catch (RestfulException e) {
            LOGGER.error("", e);
            result.put("code", e.getRestfulError().name());
            result.put("msg", e.getRestfulError().getMsg());
        } catch (Exception e) {
            LOGGER.error("", e);
            result.put("code", RestfulError.SYS_ERROR.name());
            result.put("msg", RestfulError.SYS_ERROR.getMsg());
        }
    }

    private void processDirectly(RestfulResult result, HttpServletRequest request, HttpServletResponse response, RequestMethod method, RestfulApi api) {
        switch (method) {
            case GET:
                api.get(result, request, response);
                break;
            case POST:
                api.post(result, request, response);
                break;
            case PUT:
                api.put(result, request, response);
                break;
            case DELETE:
                api.delete(result, request, response);
                break;
            default:
                throw new RestfulException(RestfulError.UNSUPPORTED_METHOD);
        }
    }
}
