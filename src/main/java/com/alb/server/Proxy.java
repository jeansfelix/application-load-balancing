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
	final String proxyTo = chooseDestination(request);
	final URI rewrittenURI = createRewrittenURI(request, proxyTo);

	if (validateDestination(rewrittenURI.getHost(), rewrittenURI.getPort())) {
	    return rewrittenURI.toString();
	}

	return null;
    }

    protected URI createRewrittenURI(final HttpServletRequest request, final String proxyTo) {
	String queryString = request.getQueryString();
	StringBuilder builder = new StringBuilder(proxyTo);
	
	builder.append(request.getRequestURI()).append("?").append(queryString);
	
	URI uri = URI.create(builder.toString());

	return uri;
    }

    protected String chooseDestination(final HttpServletRequest request) {
	String userAgent = request.getHeader("user-agent");

	String regex = "Mozilla(.*)Chrome([0-9]*).*Safari\\/([0-9]*\\.([0-9])*)$";

	if (userAgent.matches(regex))
	    return DEFAULT_PROXYTO_CHROME;

	return DEFAULT_PROXYTO_OTHER;
    }

}
