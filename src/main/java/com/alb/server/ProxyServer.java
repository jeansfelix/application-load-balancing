package com.alb.server;

import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.proxy.ConnectHandler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;

public class ProxyServer {
    private static final String PATH_TO_KEYSTORE = "certificate/alb.jks";
    private static final String KEYSTORE_PASSWORD = "123456";
    
    public static void initServer(int port) throws Exception, InterruptedException {
	Server server = new Server();

	ServerConnector httpsConnector = createConnectorHTTPS(server, port);
	server.addConnector(httpsConnector);

	ConnectHandler proxy = new ConnectHandler();
	server.setHandler(proxy);
	ServletContextHandler context = new ServletContextHandler(proxy, "/", ServletContextHandler.SESSIONS);

	ServletHolder proxyServlet = new ServletHolder(Proxy.class);
	context.addServlet(proxyServlet, "/*");

	server.start();
	server.join();
    }
    
    private static ServerConnector createConnectorHTTPS(Server server, int port) {
	final HttpConfiguration httpConfiguration = new HttpConfiguration();
	httpConfiguration.setSecureScheme("https");
	httpConfiguration.setSecurePort(port);

	final SslContextFactory sslContextFactory = new SslContextFactory(PATH_TO_KEYSTORE);
	sslContextFactory.setKeyStorePassword(KEYSTORE_PASSWORD);

	final HttpConfiguration httpsConfiguration = new HttpConfiguration(httpConfiguration);
	httpsConfiguration.addCustomizer(new SecureRequestCustomizer());

	final ServerConnector httpsConnector = new ServerConnector(server,
		new SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.asString()),
		new HttpConnectionFactory(httpsConfiguration));
	httpsConnector.setPort(port);

	return httpsConnector;
    }
}