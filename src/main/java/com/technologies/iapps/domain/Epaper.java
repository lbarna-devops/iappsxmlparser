package com.technologies.iapps.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A Epaper.
 */
@Entity
@Table(name = "epaper")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Epaper implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "newspaper_name")
    private String newspaperName;

    @Column(name = "width")
    private Integer width;

    @Column(name = "height")
    private Integer height;

    @Column(name = "dpi")
    private Integer dpi;

    @Column(name = "uploadtime")
    private String uploadtime;

    @Column(name = "filename")
    private String filename;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Epaper id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNewspaperName() {
        return this.newspaperName;
    }

    public Epaper newspaperName(String newspaperName) {
        this.setNewspaperName(newspaperName);
        return this;
    }

    public void setNewspaperName(String newspaperName) {
        this.newspaperName = newspaperName;
    }

    public Integer getWidth() {
        return this.width;
    }

    public Epaper width(Integer width) {
        this.setWidth(width);
        return this;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return this.height;
    }

    public Epaper height(Integer height) {
        this.setHeight(height);
        return this;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getDpi() {
        return this.dpi;
    }

    public Epaper dpi(Integer dpi) {
        this.setDpi(dpi);
        return this;
    }

    public void setDpi(Integer dpi) {
        this.dpi = dpi;
    }

    public String getUploadtime() {
        return this.uploadtime;
    }

    public Epaper uploadtime(String uploadtime) {
        this.setUploadtime(uploadtime);
        return this;
    }

    public void setUploadtime(String uploadtime) {
        this.uploadtime = uploadtime;
    }

    public String getFilename() {
        return this.filename;
    }

    public Epaper filename(String filename) {
        this.setFilename(filename);
        return this;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Epaper)) {
            return false;
        }
        return id != null && id.equals(((Epaper) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Epaper{" +
            "id=" + getId() +
            ", newspaperName='" + getNewspaperName() + "'" +
            ", width=" + getWidth() +
            ", height=" + getHeight() +
            ", dpi=" + getDpi() +
            ", uploadtime='" + getUploadtime() + "'" +
            ", filename='" + getFilename() + "'" +
            "}";
    }
}
