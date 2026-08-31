package com.translated.lara.translator;

import java.util.Date;
import java.util.Objects;

public class Styleguide {

    private final String id;
    private final String name;
    private final String content;
    private final String ownerId;
    private final Date createdAt;
    private final Date updatedAt;
    private final Date sharedAt;
    private final boolean isPersonal;

    public Styleguide(String id, String name, String content, String ownerId, Date createdAt, Date updatedAt, Boolean isPersonal) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.ownerId = ownerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.sharedAt = null;
        this.isPersonal = Boolean.TRUE.equals(isPersonal);
    }

    public Styleguide(String id, String name, String content, String ownerId, Date createdAt, Date updatedAt, Date sharedAt, Boolean isPersonal) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.ownerId = ownerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.sharedAt = sharedAt;
        this.isPersonal = Boolean.TRUE.equals(isPersonal);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public Date getSharedAt() {
        return sharedAt;
    }

    /**
     * The API sends is_personal: true and omits the key otherwise; it never sends false or null,
     * so an absent field means "not personal" and is reported as FALSE rather than null.
     */
    public Boolean getIsPersonal() {
        return Boolean.valueOf(isPersonal);
    }

    @Override
    public String toString() {
        return "Styleguide{id='" + id + "', name='" + name + "'}";
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Styleguide)) return false;
        Styleguide styleguide = (Styleguide) object;
        return Objects.equals(id, styleguide.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
