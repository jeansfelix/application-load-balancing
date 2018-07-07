package com.alb.server.model;

public class ServerConfig {
    private Integer port;
    private String chromeBrowserTarget;
    private String otherBrowserTarget;

    public ServerConfig() {
    }

    public ServerConfig(Integer port, String chromeBrowserTarget, String otherBrowserTarget) {
        this.port = port;
        this.chromeBrowserTarget = chromeBrowserTarget;
        this.otherBrowserTarget = otherBrowserTarget;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getChromeBrowserTarget() {
        return chromeBrowserTarget;
    }

    public void setChromeBrowserTarget(String chromeBrowserTarget) {
        this.chromeBrowserTarget = chromeBrowserTarget;
    }

    public String getOtherBrowserTarget() {
        return otherBrowserTarget;
    }

    public void setOtherBrowserTarget(String otherBrowserTarget) {
        this.otherBrowserTarget = otherBrowserTarget;
    }
}