package com.alb.server;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.proxy.ProxyServlet;
import org.eclipse.jetty.util.ssl.SslContextFactory;

public class Proxy extends ProxyServlet {
    private static String DEFAULT_PROXYTO_CHROME = "http://localhost:8080";
    private static String DEFAULT_PROXYTO_OTHER = "http://localhost:8081";

    @Override
    protected HttpClient newHttpClient() {
	SslContextFactory sslFactory = new SslContextFactory();
	sslFactory.setTrustAll(true);
	return new HttpClient(sslFactory);
    }

    @Override
    protected String rewriteTarget(final HttpServletRequest request) {
	
	URI rewrittenURI = URI.create(DEFAULT_PROXYTO_OTHER + request.getRequestURI() + "?" + request.getQueryString());
	
	if (validateDestination(rewrittenURI.getHost(), rewrittenURI.getPort())) {
	    return rewrittenURI.toString();
	}
	
	return null;
    }

}
