package com.alb.server;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.proxy.ProxyServlet;
import org.eclipse.jetty.util.ssl.SslContextFactory;

public class Proxy extends ProxyServlet {
    private static final long serialVersionUID = 8881768845514682064L;
    
    private static String DEFAULT_PROXYTO_CHROME = "http://localhost:8080";
    private static String DEFAULT_PROXYTO_OTHER = "http://localhost:8081";

    private String serverChrome;
    private String serverOther;
    
    public Proxy() {
	super();
	this.serverChrome = DEFAULT_PROXYTO_CHROME;
	this.serverOther = DEFAULT_PROXYTO_OTHER;
    }
    
    public Proxy(String serverChrome, String serverOther) {
	super();
	this.serverChrome = serverChrome;
	this.serverOther = serverOther;
    }
    
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
	StringBuilder builder = new StringBuilder(proxyTo);
	
	builder.append(request.getRequestURI());

	String queryString = request.getQueryString();
	if (queryString != null) 
	    builder.append("?").append(queryString);
	
	URI uri = URI.create(builder.toString());

	return uri;
    }

    protected String chooseDestination(final HttpServletRequest request) {
	String userAgent = request.getHeader("user-agent");

	String regexChromeBrowser = "Mozilla(.*)Chrome([0-9]*).*Safari\\/([0-9]*\\.([0-9])*)$";

	if (userAgent.matches(regexChromeBrowser))
	    return serverChrome;

	return serverOther;
    }

}
