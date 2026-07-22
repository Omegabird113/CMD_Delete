package io.github.omegabird113.cmd_delete.mappings;

import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

import java.util.Locale;

public enum Os {
    WINDOWS,
    LINUX,
    MAC;

    public static final @NonNull Os USING = getCurrent();
    public static final boolean IS_USING_MAC = USING == MAC;

    @Contract(pure = true)
    private static @NonNull Os getCurrent() {
        String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        if (os.contains("mac"))
            return MAC;
        else if (os.contains("win"))
            return WINDOWS;
        else
            return LINUX;
    }
}
