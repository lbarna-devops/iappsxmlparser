package com.technologies.iapps.web.rest.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class GetPages {

    @JacksonXmlProperty(isAttribute = true)
    private int editionDefId;

    @JacksonXmlProperty(isAttribute = true)
    private String publicationDate;

    public int getEditionDefId() {
        return editionDefId;
    }

    public void setEditionDefId(int editionDefId) {
        this.editionDefId = editionDefId;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }
}
