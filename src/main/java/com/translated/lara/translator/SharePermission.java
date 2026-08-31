package com.translated.lara.translator;

import com.google.gson.annotations.JsonAdapter;
import com.translated.lara.net.json.SharePermissionTypeAdapter;

import java.util.Objects;

/**
 * Permission granted by a resource share.
 *
 * <p>Known values are exposed as constants, while values added by newer API versions retain
 * their original string through {@link #fromValue(String)}.</p>
 */
@JsonAdapter(SharePermissionTypeAdapter.class)
public final class SharePermission {
    public static final SharePermission READ = new SharePermission("read");
    public static final SharePermission READ_WRITE = new SharePermission("read_write");

    private final String value;

    private SharePermission(String value) {
        this.value = value;
    }

    /**
     * Creates a permission from its API value, preserving values introduced by newer API versions.
     */
    public static SharePermission fromValue(String value) {
        Objects.requireNonNull(value, "value");
        if (READ.value.equals(value)) return READ;
        if (READ_WRITE.value.equals(value)) return READ_WRITE;
        return new SharePermission(value);
    }

    /**
     * Returns the permission value exactly as represented by the API.
     */
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof SharePermission)) return false;
        SharePermission permission = (SharePermission) object;
        return value.equals(permission.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
