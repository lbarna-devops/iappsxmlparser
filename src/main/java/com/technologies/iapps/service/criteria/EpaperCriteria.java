package com.technologies.iapps.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.technologies.iapps.domain.Epaper} entity. This class is used
 * in {@link com.technologies.iapps.web.rest.EpaperResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /epapers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EpaperCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter newspaperName;

    private IntegerFilter width;

    private IntegerFilter height;

    private IntegerFilter dpi;

    private StringFilter uploadtime;

    private StringFilter filename;

    private Boolean distinct;

    public EpaperCriteria() {}

    public EpaperCriteria(EpaperCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.newspaperName = other.newspaperName == null ? null : other.newspaperName.copy();
        this.width = other.width == null ? null : other.width.copy();
        this.height = other.height == null ? null : other.height.copy();
        this.dpi = other.dpi == null ? null : other.dpi.copy();
        this.uploadtime = other.uploadtime == null ? null : other.uploadtime.copy();
        this.filename = other.filename == null ? null : other.filename.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EpaperCriteria copy() {
        return new EpaperCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNewspaperName() {
        return newspaperName;
    }

    public StringFilter newspaperName() {
        if (newspaperName == null) {
            newspaperName = new StringFilter();
        }
        return newspaperName;
    }

    public void setNewspaperName(StringFilter newspaperName) {
        this.newspaperName = newspaperName;
    }

    public IntegerFilter getWidth() {
        return width;
    }

    public IntegerFilter width() {
        if (width == null) {
            width = new IntegerFilter();
        }
        return width;
    }

    public void setWidth(IntegerFilter width) {
        this.width = width;
    }

    public IntegerFilter getHeight() {
        return height;
    }

    public IntegerFilter height() {
        if (height == null) {
            height = new IntegerFilter();
        }
        return height;
    }

    public void setHeight(IntegerFilter height) {
        this.height = height;
    }

    public IntegerFilter getDpi() {
        return dpi;
    }

    public IntegerFilter dpi() {
        if (dpi == null) {
            dpi = new IntegerFilter();
        }
        return dpi;
    }

    public void setDpi(IntegerFilter dpi) {
        this.dpi = dpi;
    }

    public StringFilter getUploadtime() {
        return uploadtime;
    }

    public StringFilter uploadtime() {
        if (uploadtime == null) {
            uploadtime = new StringFilter();
        }
        return uploadtime;
    }

    public void setUploadtime(StringFilter uploadtime) {
        this.uploadtime = uploadtime;
    }

    public StringFilter getFilename() {
        return filename;
    }

    public StringFilter filename() {
        if (filename == null) {
            filename = new StringFilter();
        }
        return filename;
    }

    public void setFilename(StringFilter filename) {
        this.filename = filename;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EpaperCriteria that = (EpaperCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(newspaperName, that.newspaperName) &&
            Objects.equals(width, that.width) &&
            Objects.equals(height, that.height) &&
            Objects.equals(dpi, that.dpi) &&
            Objects.equals(uploadtime, that.uploadtime) &&
            Objects.equals(filename, that.filename) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, newspaperName, width, height, dpi, uploadtime, filename, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EpaperCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (newspaperName != null ? "newspaperName=" + newspaperName + ", " : "") +
            (width != null ? "width=" + width + ", " : "") +
            (height != null ? "height=" + height + ", " : "") +
            (dpi != null ? "dpi=" + dpi + ", " : "") +
            (uploadtime != null ? "uploadtime=" + uploadtime + ", " : "") +
            (filename != null ? "filename=" + filename + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
