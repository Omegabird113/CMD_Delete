package io.github.omegabird113.cmd_delete.utils;

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
    public static @NonNull Os getCurrent() {
        return Os.get(System.getProperty("os.name").toLowerCase(Locale.ROOT));
    }

    @Contract("_ -> new")
    public static @NonNull Os get(@NonNull String osName) {
        if (osName.contains("mac"))
            return MAC;
        else if (osName.contains("win"))
            return WINDOWS;
        else
            return LINUX;
    }
}
