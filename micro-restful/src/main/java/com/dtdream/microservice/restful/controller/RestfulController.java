package com.dtdream.microservice.restful.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by 张三丰 on 2016-10-08.
 */
@Component
public interface RestfulController {
    /**
     * 那些方法的执行需要在同一线程中
     * @return
     */
    RequestMethod[] needSyncMethods();
}
