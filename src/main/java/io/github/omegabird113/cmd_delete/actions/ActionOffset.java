package io.github.omegabird113.cmd_delete.actions;

public enum ActionOffset {
    LEFT(-1),
    RIGHT(1),
    UP(-1),
    DOWN(1),
    INVALID(0);

    public final int value;

    ActionOffset(int value) {
        this.value = value;
    }
}
