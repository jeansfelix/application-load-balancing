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

public class Application {
    private static int DEFAULT_HTTPS_SERVER_PORT = 8443;

    private static final String PATH_TO_KEYSTORE = "certificate/alb.jks";
    private static final String KEYSTORE_PASSWORD = "123456";

    public static void main(String... args) throws Exception {
	initServer();
    }

    protected static void initServer() throws Exception, InterruptedException {
	Server server = new Server();

	ServerConnector httpsConnector = createConnectorHTTPS(server);
	server.addConnector(httpsConnector);

	ConnectHandler proxy = new ConnectHandler();
	server.setHandler(proxy);
	ServletContextHandler context = new ServletContextHandler(proxy, "/", ServletContextHandler.SESSIONS);

	ServletHolder proxyServlet = new ServletHolder(Proxy.class);
	context.addServlet(proxyServlet, "/*");

	server.start();
	server.join();
    }

    protected static ServerConnector createConnectorHTTPS(Server server) {
	final HttpConfiguration httpConfiguration = new HttpConfiguration();
	httpConfiguration.setSecureScheme("https");
	httpConfiguration.setSecurePort(DEFAULT_HTTPS_SERVER_PORT);

	final SslContextFactory sslContextFactory = new SslContextFactory(PATH_TO_KEYSTORE);
	sslContextFactory.setKeyStorePassword(KEYSTORE_PASSWORD);

	final HttpConfiguration httpsConfiguration = new HttpConfiguration(httpConfiguration);
	httpsConfiguration.addCustomizer(new SecureRequestCustomizer());
	
	final ServerConnector httpsConnector = new ServerConnector(server,
		new SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.asString()),
		new HttpConnectionFactory(httpsConfiguration));
	httpsConnector.setPort(DEFAULT_HTTPS_SERVER_PORT);
	
	return httpsConnector;
    }
}
