package com.technologies.iapps.web.rest.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class OsInfo {

    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @JacksonXmlProperty(isAttribute = true)
    private String version;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
