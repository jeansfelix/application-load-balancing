package com.alb.server.model;

public class Arguments {
    private int port;
    private String serverChrome;
    private String serverOther;

    public int getPort() {
	return port;
    }

    public void setPort(int port) {
	this.port = port;
    }

    public String getServerChrome() {
	return serverChrome;
    }

    public void setServerChrome(String serverChrome) {
	this.serverChrome = serverChrome;
    }

    public String getServerOther() {
	return serverOther;
    }

    public void setServerOther(String serverOther) {
	this.serverOther = serverOther;
    }

}
