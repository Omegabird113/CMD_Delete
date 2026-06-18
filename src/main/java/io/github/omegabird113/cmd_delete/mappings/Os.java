package io.github.omegabird113.cmd_delete.mappings;

import java.util.Locale;

public enum Os {
    WINDOWS,
    LINUX,
    MAC;

    public static Os getCurrent() {
        String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        if (os.contains("mac"))
            return MAC;
        else if (os.contains("win"))
            return WINDOWS;
        else
            return LINUX;
    }
}
