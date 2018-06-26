package com.alb.server;

import org.eclipse.jetty.proxy.ConnectHandler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Application {
    
    
    public static void main(String... args) throws Exception {
	Server server = new Server(8443);

	ConnectHandler proxy = new ConnectHandler();
	server.setHandler(proxy);

	ServletContextHandler context = new ServletContextHandler(proxy, "/", ServletContextHandler.SESSIONS);
	ServletHolder proxyServlet = new ServletHolder(Proxy.class);
	context.addServlet(proxyServlet, "/*");

	server.start();
	server.join();
    }
}
