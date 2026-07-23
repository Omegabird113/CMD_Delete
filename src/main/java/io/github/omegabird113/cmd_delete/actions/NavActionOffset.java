package io.github.omegabird113.cmd_delete.actions;

public enum NavActionOffset {
    LEFT(-1),
    RIGHT(1),
    UP(-1),
    DOWN(1),
    INVALID(0);

    private final int value;

    NavActionOffset(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
