package com.translated.lara.translator;

import java.util.Date;
import java.util.Objects;

public class Glossary {

    public enum Type {
        CSV_TABLE_UNI("csv/table-uni");
        
        private final String value;
        
        Type(String value) {
            this.value = value;
        }
        
        @Override
        public String toString() {
            return value;
        }
    }

    private final String id;
    private final Date createdAt;
    private final Date updatedAt;
    private final String name;
    private final String ownerId;

    public Glossary(String id, Date createdAt, Date updatedAt, String name, String ownerId) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.name = name;
        this.ownerId = ownerId;
    }

    public String getId() {
        return id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public String getName() {
        return name;
    }

    public String getOwnerId() {
        return ownerId;
    }

    @Override
    public String toString() {
        return "Glossary{id='" + id + "', name='" + name + "'}";
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Glossary)) return false;
        Glossary glossary = (Glossary) object;
        return Objects.equals(id, glossary.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
