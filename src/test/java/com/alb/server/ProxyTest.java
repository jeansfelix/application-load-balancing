package com.alb.server;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class ProxyTest {
    
    @Test
    public void testChooseDestination_requestFromLinuxChromeBrowser_mustBeReturnChromeServer() throws Exception {
	String expectedChromeServer = "http://localhost:8080";
	
	HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);

	String chromeUserAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/000.00 (KHTML, like Gecko) Chrome/00.0.0000.00 Safari/000.00";
	Mockito.when(httpServletRequest.getHeader("user-agent")).thenReturn(chromeUserAgent);
	
	Proxy proxy = new Proxy();
	
	String proxyDestination = proxy.chooseDestination(httpServletRequest);
	
	Assert.assertEquals(expectedChromeServer, proxyDestination);
    }
    
    @Test
    public void testChooseDestination_requestFromLinuxMidoriBrowser_mustBeReturnOtherServer() throws Exception {
	String expectedChromeServer = "http://localhost:8081";
	
	HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);

	String midoriUserAgent = "Mozilla/5.0 (X11; Linux) AppleWebKit/000.00 (KHTML, like Gecko) Chrome/00.0.0000.00 Safari/000.00 Midori/0.0";
	Mockito.when(httpServletRequest.getHeader("user-agent")).thenReturn(midoriUserAgent);
	
	Proxy proxy = new Proxy();
	
	String proxyDestination = proxy.chooseDestination(httpServletRequest);
	
	Assert.assertEquals(expectedChromeServer, proxyDestination);
    }
    
    @Test
    public void testChooseDestination_requestFromLinuxFirefoxBrowser_mustBeReturnOtherServer() throws Exception {
	String expectedChromeServer = "http://localhost:8081";
	
	HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);

	String midoriUserAgent = "Mozilla/5.0 (X11; U; Linux x86_64; en-US; rv:0.0.0.00) Gecko/00000000 Firefox/0.0.0.00";
	Mockito.when(httpServletRequest.getHeader("user-agent")).thenReturn(midoriUserAgent);
	
	Proxy proxy = new Proxy();
	
	String proxyDestination = proxy.chooseDestination(httpServletRequest);
	
	Assert.assertEquals(expectedChromeServer, proxyDestination);
    }
    
    @Test
    public void testCreateRewrittenURI_RequestWithPathAndQueryToProxyFromChrome_MustBeReturnURIToChromeServerCorrectly() throws Exception {
	String chromeServer = "http://localhost:8080";
	
	HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);

	String queryString = "name=John";
	Mockito.when(httpServletRequest.getQueryString()).thenReturn(queryString);
	
	String requestURI = "/greeting";
	Mockito.when(httpServletRequest.getRequestURI()).thenReturn(requestURI);
	
	Proxy proxy = new Proxy();
	
	URI uri = proxy.createRewrittenURI(httpServletRequest, chromeServer);
	
	Assert.assertEquals("http://localhost:8080/greeting?name=John", uri.toString());
    }
    
    @Test
    public void testCreateRewrittenURI_RequestWithoutPathAndQueryGreetingToProxyFromChrome_MustBeReturnURIToChromeServerCorrectly() throws Exception {
	String chromeServer = "http://localhost:8080";
	
	HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);

	String queryString = null;
	Mockito.when(httpServletRequest.getQueryString()).thenReturn(queryString);
	
	String requestURI = "";
	Mockito.when(httpServletRequest.getRequestURI()).thenReturn(requestURI);
	
	Proxy proxy = new Proxy();
	
	URI uri = proxy.createRewrittenURI(httpServletRequest, chromeServer);
	
	Assert.assertEquals("http://localhost:8080", uri.toString());
    }
    
    
}
