package com.springboot.controllers;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "server")
public class ApplicationConfig {
    private String serverName;
    private String serverPort;
    private String serverContext;
    private String serverUrl;

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getServerContext() {
        return serverContext;
    }

    public void setServerContext(String serverContext) {
        this.serverContext = serverContext;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    @Override
    public String toString() {
        return "ApplicationConfig{" +
                "serverName='" + serverName + '\'' +
                ", serverPort='" + serverPort + '\'' +
                ", serverContext='" + serverContext + '\'' +
                ", serverUrl='" + serverUrl + '\'' +
                '}';
    }
}
