package com.dtdream.microservice.core.common.util;

import com.dtdream.microservice.core.biz.BizLine;
import com.dtdream.microservice.core.biz.processors.AbstractHandler;
import com.dtdream.microservice.core.biz.processors.strategy.MultiInputStrategy;
import com.dtdream.microservice.core.common.exception.ErrorMsg;
import com.dtdream.microservice.core.common.exception.MicroServiceException;
import com.dtdream.microservice.core.disruptor.Data;

/**
 * Created by 张三丰 on 2016-10-09.
 */
public class BizLineUtil {
    public static BizLine createSameThreadBizLine(final SameThreadCallBack callBack) {
        BizLine bizLine = BizLine.create(new MultiInputStrategy());
        bizLine.register(new AbstractHandler() {
            public void handler(Data data) throws Exception {
                if (!data.getBooleanValue("__single")) {
                    throw new MicroServiceException(ErrorMsg.PROCESS_WRONG);
                }
                Object[] params = (Object[]) data.get("params");
                callBack.doInSameThread(params);
            }
        });
        bizLine.start();
        return bizLine;
    }

    public static void processInSameThread(BizLine bizLine, Object... params) {
        Data data = Data.create();
        data.put("params", params);
        data.put("__single", true);
        bizLine.process(data);
    }
    public interface SameThreadCallBack{
        void doInSameThread(Object... params);
    }
}
