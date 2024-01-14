package org.trad.pcl.config;

public class LoggingConfig {
    private static boolean loggingEnabled = true;

    public static boolean isLoggingEnabled() {
        return loggingEnabled;
    }

    public static void setLoggingEnabled(boolean enabled) {
        loggingEnabled = enabled;
    }
}
