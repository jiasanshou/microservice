package com.dtdream.microservice.restful.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 张三丰 on 2016-10-08.
 */
public class MainServer {
    private static final Logger LOG = LoggerFactory.getLogger(MainServer.class);

    private static EmbeddedServer server;

    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    shutdown();
                } catch (Exception e) {
                    LOG.debug("Failed to shutdown", e);
                }
            }
        });
    }

    private static void shutdown() {
        server.stop();
    }

    public static void main(String[] args) throws Exception {
        String appPath = "";
        int appPort = 22000;
        server = EmbeddedServer.newServer(appPort, appPath);
        server.start();
    }
}
