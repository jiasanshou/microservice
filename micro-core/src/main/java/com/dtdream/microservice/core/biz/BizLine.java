package com.dtdream.microservice.core.biz;

import com.dtdream.microservice.core.biz.processors.AbstractHandler;
import com.dtdream.microservice.core.biz.processors.AbstractInitializer;
import com.dtdream.microservice.core.biz.processors.AbstractValidator;
import com.dtdream.microservice.core.biz.processors.Handler;
import com.dtdream.microservice.core.biz.processors.Initializer;
import com.dtdream.microservice.core.biz.processors.Validator;
import com.dtdream.microservice.core.biz.processors.strategy.MultiInputStrategy;
import com.dtdream.microservice.core.biz.processors.strategy.ProcessStrategy;
import com.dtdream.microservice.core.common.exception.MicroServiceException;
import com.dtdream.microservice.core.common.exception.ErrorMsg;
import com.dtdream.microservice.core.disruptor.Data;
import com.lmax.disruptor.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 高性能业务线组件<br>
 * 主要特点：
 * <li>可以把多线程调用转换为单线程调用</LI>
 * <LI>提高业务逻辑复用性</LI>
 * <LI>并发处理部分业务逻辑</LI>
 * 业务分成三个部分
 * <LI>初始化器</LI>
 * <LI>验证器</LI>
 * <LI>处理器(1个)</LI>
 * 初始化器和验证器存在多个时是并发执行，不保证顺序。<br>
 * 初始化器全部执行完毕后执行验证器，全部验证器执行完成后执行处理器<br>
 * 即：总体有序，部分无序<br>
 * 处理策略:
 * <LI>单写同步(初始化器和验证器总数不能超过3个)</LI>
 * <LI>单写异步(初始化器和验证器总数不能超过3个)</LI>
 * <LI>多写同步(默认，初始化器和验证器总数不能超过3个)</LI>
 * <LI>多写异步(初始化器和验证器总数不能超过3个)</LI>
 * <LI>当前线程</LI>
 * 同步即调用process方法处理后等待处理结果<br>
 * 注意：<br><p>
 * 1.在服务容器(Tomcat/Jetty等)中，一般都是多线程调用，所以不应该使用单写策略。<br>
 * 但是可以在使用多写策略后使用单写策略。<br>
 * 经典的服务场景为：“多写”->[handler中“当前线程”]->单写<br>
 * 单写/多写时可采用的结构：<br>
 * <LI>0/1/2/3个初始化器/验证器+handler</LI>
 * <LI>2个初始化器+1个验证器+handler</LI>
 * <LI>1个初始化器+2个验证器+handler</LI>
 * 可在任意处理器中创建新的业务线(BizLine)<br></p>
 * <P>2.start方法只能调用一次，所有处理器必须在start之前注册完毕</P>
 * Created by 张三丰 on 2016-09-28.
 */
public class BizLine {
    public static final int PROCESSOR_LIMIT = 3;
    private List<Initializer> initializers = new ArrayList<Initializer>();
    private List<Validator> validators = new ArrayList<Validator>();
    private Handler hander;
    private ProcessStrategy processStrategy;
    private static final Logger LOGGER = LoggerFactory.getLogger(BizLine.class);

    private BizLine(ProcessStrategy processStrategy) {
        this.processStrategy = processStrategy;
    }

    public static BizLine create() {
        return new BizLine(new MultiInputStrategy());
    }

    public static BizLine create(ProcessStrategy processStrategy) {
        return new BizLine(processStrategy);
    }

    public BizLine register(AbstractHandler hander) {
        if (this.hander != null) {
            throw new MicroServiceException(ErrorMsg.HANDLER_REGISTED_ALREADY);
        }
        this.hander = hander;
        return this;
    }

    public BizLine register(AbstractInitializer initializer) {
        checkLimit();
        initializers.add(initializer);
        return this;
    }

    private void checkLimit() {
        if (initializers.size() + validators.size() >= PROCESSOR_LIMIT) {
            throw new MicroServiceException(ErrorMsg.EXCEED_LIMIT);
        }
    }

    public BizLine register(AbstractValidator validator) {
        checkLimit();
        validators.add(validator);
        return this;
    }

    public void start() {
        processStrategy.start(initializers, validators, hander);
    }

    public void process(final Data data) {
        processStrategy.process(data);
    }

    public void close() throws TimeoutException {
        processStrategy.close();
    }
}
