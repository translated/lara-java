package com.translated.lara.translator;

import com.google.gson.annotations.JsonAdapter;
import com.translated.lara.net.json.PermissionMaskTypeAdapter;

import java.util.Objects;

/**
 * Resource permissions in read, write, export, share order; a dash means absent.
 *
 * <p>Known values are exposed as constants, while values added by newer API versions retain
 * their original string through {@link #fromValue(String)}.</p>
 */
@JsonAdapter(PermissionMaskTypeAdapter.class)
public final class PermissionMask {
    public static final PermissionMask READ = new PermissionMask("r---");
    public static final PermissionMask READ_WRITE = new PermissionMask("rw--");
    public static final PermissionMask READ_EXPORT = new PermissionMask("r-e-");
    public static final PermissionMask READ_SHARE = new PermissionMask("r--s");
    public static final PermissionMask READ_WRITE_EXPORT = new PermissionMask("rwe-");
    public static final PermissionMask READ_WRITE_SHARE = new PermissionMask("rw-s");
    public static final PermissionMask READ_EXPORT_SHARE = new PermissionMask("r-es");
    public static final PermissionMask READ_WRITE_EXPORT_SHARE = new PermissionMask("rwes");

    private final String value;

    private PermissionMask(String value) {
        this.value = value;
    }

    /**
     * Creates a permission from its API value, preserving values introduced by newer API versions.
     */
    public static PermissionMask fromValue(String value) {
        Objects.requireNonNull(value, "value");
        if (READ.value.equals(value)) return READ;
        if (READ_WRITE.value.equals(value)) return READ_WRITE;
        if (READ_EXPORT.value.equals(value)) return READ_EXPORT;
        if (READ_SHARE.value.equals(value)) return READ_SHARE;
        if (READ_WRITE_EXPORT.value.equals(value)) return READ_WRITE_EXPORT;
        if (READ_WRITE_SHARE.value.equals(value)) return READ_WRITE_SHARE;
        if (READ_EXPORT_SHARE.value.equals(value)) return READ_EXPORT_SHARE;
        if (READ_WRITE_EXPORT_SHARE.value.equals(value)) return READ_WRITE_EXPORT_SHARE;
        return new PermissionMask(value);
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
        if (!(object instanceof PermissionMask)) return false;
        PermissionMask permission = (PermissionMask) object;
        return value.equals(permission.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
