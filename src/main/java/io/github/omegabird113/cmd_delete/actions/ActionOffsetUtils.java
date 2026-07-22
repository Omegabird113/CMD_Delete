package io.github.omegabird113.cmd_delete.actions;

import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public final class ActionOffsetUtils {
    private ActionOffsetUtils() {
    }

    @Contract(pure = true)
    public static int getOffset(@NonNull NavAction action) {
        return action.offset.value;
    }

    @Contract(pure = true)
    public static boolean isMoveAction(@NonNull NavAction action) {
        return action.type == NavAction.Type.MOVE;
    }

    @Contract(pure = true)
    public static boolean isOverrideAction(@NonNull NavAction action) {
        return action.override;
    }

    @Contract(pure = true)
    public static boolean isOverrideEditAction(@NonNull NavAction action) {
        return switch (action) {
            case OVR_COPY, OVR_CUT, OVR_PASTE, OVR_SELECT_ALL -> true;
            default -> false;
        };
    }
}
