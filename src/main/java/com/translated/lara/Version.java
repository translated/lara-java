package com.translated.lara;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Version {

    private static final String version = read();

    private static String read() {
        InputStream stream = Version.class.getResourceAsStream("lara-java.properties");
        if (stream != null) {
            try {
                Properties properties = new Properties();
                properties.load(stream);

                return properties.getProperty("version");
            } catch (IOException e) {
                return null;
            } finally {
                try {
                    stream.close();
                } catch (Exception e) {
                    // ignore
                }
            }
        } else {
            return null;
        }
    }

    public static String get() {
        return version;
    }

}
