package com.technologies.iapps.web.rest.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class AppInfo {

    @JacksonXmlProperty(localName = "newspaperName")
    private String newspaperName;

    @JacksonXmlProperty(localName = "version")
    private String version;

    public String getNewspaperName() {
        return newspaperName;
    }

    public void setNewspaperName(String newspaperName) {
        this.newspaperName = newspaperName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
