package com.alb.server;

public class Application {
    private static int DEFAULT_HTTPS_SERVER_PORT = 8443;

    public static void main(String... args) throws Exception {
	ProxyServer.initServer(DEFAULT_HTTPS_SERVER_PORT);
    }

}
