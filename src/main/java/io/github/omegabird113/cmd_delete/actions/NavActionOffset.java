package io.github.omegabird113.cmd_delete.actions;

import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public enum NavActionOffset {
    LEFT(-1),
    RIGHT(1),
    UP(-1),
    DOWN(1),
    INVALID(0);

    public final int value;

    NavActionOffset(int value) {
        this.value = value;
    }

    @Contract(pure = true)
    public static int get(@NonNull NavAction action) {
        return action.offset.value;
    }
}
