package com.technologies.iapps.web.rest.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "epaperRequest")
public class EpaperRequest {

    @JacksonXmlProperty(localName = "deviceInfo")
    private DeviceInfo deviceInfo;

    @JacksonXmlProperty(localName = "getPages")
    private GetPages getPages;

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public GetPages getGetPages() {
        return getPages;
    }

    public void setGetPages(GetPages getPages) {
        this.getPages = getPages;
    }
}
