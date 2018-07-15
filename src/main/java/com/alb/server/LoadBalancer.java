package com.alb.server;

import java.net.URI;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.proxy.ProxyServlet;
import org.eclipse.jetty.util.URIUtil;
import org.eclipse.jetty.util.ssl.SslContextFactory;

public class LoadBalancer extends ProxyServlet {
    private static final long serialVersionUID = 8881768845514682064L;

    private static final List<String> REVERSE_PROXY_HEADERS;
    
    static {
        List<String> params = new LinkedList<String>();
        params.add("Location");
        params.add("Content-Location");
        params.add("URI");
        REVERSE_PROXY_HEADERS = Collections.unmodifiableList(params);
    }

    private String serverChrome;
    private String serverOther;

    public LoadBalancer(String serverChrome, String serverOther) {
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

    @Override
    protected String filterServerResponseHeader(HttpServletRequest request, Response serverResponse, String headerName,
            String headerValue) {

        if (REVERSE_PROXY_HEADERS.contains(headerName)) {
            URI locationURI = URI.create(headerValue).normalize();
            if (locationURI.isAbsolute() && isProxyTo(locationURI)) {
                StringBuilder newURI = URIUtil.newURIBuilder(request.getScheme(), request.getServerName(),
                        request.getServerPort());
                String component = locationURI.getRawPath();
                if (component != null)
                    newURI.append(component);
                component = locationURI.getRawQuery();
                if (component != null)
                    newURI.append('?').append(component);
                component = locationURI.getRawFragment();
                if (component != null)
                    newURI.append('#').append(component);
                return URI.create(newURI.toString()).normalize().toString();
            }
        }

        return headerValue;
    }

    private boolean isProxyTo(URI locationURI) {
        URI chromeURI = URI.create(serverChrome);
        URI otherURI = URI.create(serverOther);

        if (chromeURI.getHost().equals(locationURI.getHost()) && chromeURI.getScheme().equals(locationURI.getScheme())
                && chromeURI.getPort() == locationURI.getPort()) {
            return true;
        }

        if (otherURI.getHost().equals(locationURI.getHost()) && otherURI.getScheme().equals(locationURI.getScheme())
                && otherURI.getPort() == locationURI.getPort()) {
            return true;
        }

        return false;
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
