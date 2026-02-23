package com.translated.lara;

import com.translated.lara.authentication.AccessKey;

/**
 * @deprecated Use {@link AccessKey} instead.
 */
@Deprecated
public class Credentials extends AccessKey {

    public Credentials(String accessKeyId, String accessKeySecret) {
        super(accessKeyId, accessKeySecret);
    }

    /**
     * @deprecated use {@link #getId()} instead
     * @return String
     */
    @Deprecated
    public String getAccessKeyId() {
        return id;
    }

    /**
     * @deprecated use {@link #getSecret()} instead
     * @return String
     */
    @Deprecated
    public String getAccessKeySecret() {
        return secret;
    }
}
