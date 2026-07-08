package io.github.omegabird113.cmd_delete.config.registry;

public class CustomMappingsRegistryKey {
    private final int key;
    private final boolean shift;
    private final boolean altOption;
    private final boolean control;
    private final boolean superCommand;

    public CustomMappingsRegistryKey(int key, boolean shift, boolean altOption, boolean control,
                                     boolean superCommand) {
        this.key = key;
        this.shift = shift;
        this.altOption = altOption;
        this.control = control;
        this.superCommand = superCommand;
    }

    public int key() {
        return key;
    }
    public boolean shift() {
        return shift;
    }

    public boolean altOption() {
        return altOption;
    }

    public boolean control() {
        return control;
    }

    public boolean superCommand() {
        return superCommand;
    }
}
