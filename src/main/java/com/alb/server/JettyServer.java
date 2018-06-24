package com.alb.server;

import org.eclipse.jetty.server.Server;

public class JettyServer {
    private static final int DEFAULT_SERVER_PORT_8080 = 8080;

    public static void main(String[] args) throws Exception {
	Server server = new Server(DEFAULT_SERVER_PORT_8080);

	server.setHandler(new HandleRequest());
	
	server.start();
	server.join();
    }
}
