package com.technologies.iapps.web.rest.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class DeviceInfo {

    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @JacksonXmlProperty(isAttribute = true)
    private String id;

    @JacksonXmlProperty(localName = "screenInfo")
    private ScreenInfo screenInfo;

    @JacksonXmlProperty(localName = "osInfo")
    private OsInfo osInfo;

    @JacksonXmlProperty(localName = "appInfo")
    private AppInfo appInfo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ScreenInfo getScreenInfo() {
        return screenInfo;
    }

    public void setScreenInfo(ScreenInfo screenInfo) {
        this.screenInfo = screenInfo;
    }

    public OsInfo getOsInfo() {
        return osInfo;
    }

    public void setOsInfo(OsInfo osInfo) {
        this.osInfo = osInfo;
    }

    public AppInfo getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(AppInfo appInfo) {
        this.appInfo = appInfo;
    }
}
