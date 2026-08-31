package com.translated.lara.translator;

import java.util.Date;

public class ResourceShareEntry {
    private final String id;
    private final String name;
    private final String shareName;
    private final Date sharedAt;
    private final SharePermission permissions;

    public ResourceShareEntry(String id, String name, String shareName, Date sharedAt, SharePermission permissions) {
        this.id = id;
        this.name = name;
        this.shareName = shareName;
        this.sharedAt = sharedAt;
        this.permissions = permissions;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getShareName() { return shareName; }
    public Date getSharedAt() { return sharedAt; }
    public SharePermission getPermissions() { return permissions; }
}
