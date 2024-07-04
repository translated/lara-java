package com.translated.lara.translator;

import java.util.Date;
import java.util.Objects;

public class Memory {

    private final String id;
    private final Date createdAt;
    private final Date updatedAt;
    private final Date sharedAt;
    private final String name;
    private final String externalId;
    private final String secret;
    private final String ownerId;
    private final int collaboratorsCount;

    public Memory(String id, Date createdAt, Date updatedAt, Date sharedAt, String name, String externalId, String secret, String ownerId, int collaboratorsCount) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.sharedAt = sharedAt;
        this.name = name;
        this.externalId = externalId;
        this.secret = secret;
        this.ownerId = ownerId;
        this.collaboratorsCount = collaboratorsCount;
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

    public Date getSharedAt() {
        return sharedAt;
    }

    public String getName() {
        return name;
    }

    public String getExternalId() {
        return externalId;
    }

    public String getSecret() {
        return secret;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public int getCollaboratorsCount() {
        return collaboratorsCount;
    }

    @Override
    public String toString() {
        return "Memory{id='" + id + "', name='" + name + "'}";
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Memory)) return false;
        Memory memory = (Memory) object;
        return Objects.equals(id, memory.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
