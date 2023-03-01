package com.technologies.iapps.web.rest.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class ScreenInfo {

    @JacksonXmlProperty(isAttribute = true)
    private int width;

    @JacksonXmlProperty(isAttribute = true)
    private int height;

    @JacksonXmlProperty(isAttribute = true)
    private int dpi;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getDpi() {
        return dpi;
    }

    public void setDpi(int dpi) {
        this.dpi = dpi;
    }
}
