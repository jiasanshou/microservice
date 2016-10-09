package com.dtdream.microservice.restful.server;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by 张三丰 on 2016-10-08.
 */
public class EmbeddedServer {
    public static final Logger LOG = LoggerFactory.getLogger(EmbeddedServer.class);

    private static final int DEFAULT_BUFFER_SIZE = 16192;

    protected final Server server = new Server();

    public EmbeddedServer(int port, String path) throws IOException {
        Connector connector = getConnector(port);
        server.addConnector(connector);

        WebAppContext application = getWebAppContext(path);
        server.setHandler(application);
    }

    protected WebAppContext getWebAppContext(String path) {
        WebAppContext application = new WebAppContext(path, "/");
        application.setClassLoader(Thread.currentThread().getContextClassLoader());
        return application;
    }

    public static EmbeddedServer newServer(int port, String path) throws IOException {
        return new EmbeddedServer(port, path);
    }

    protected Connector getConnector(int port) throws IOException {

        HttpConfiguration http_config = new HttpConfiguration();
        // this is to enable large header sizes when Kerberos is enabled with AD
        final int bufferSize = getBufferSize();
        http_config.setResponseHeaderSize(bufferSize);
        http_config.setRequestHeaderSize(bufferSize);

        ServerConnector connector = new ServerConnector(server, new HttpConnectionFactory(http_config));
        connector.setPort(port);
        connector.setHost("0.0.0.0");
        server.addConnector(connector);
        return connector;
    }

    protected Integer getBufferSize() {
        return DEFAULT_BUFFER_SIZE;
    }

    public void start() throws Exception {
        server.start();
        server.join();
    }

    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {
            LOG.warn("Error during shutdown", e);
        }
    }
}
