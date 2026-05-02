package io.github.omegabird113.cmd_delete.actions.mapping;

import io.github.omegabird113.cmd_delete.actions.Os;

public class LinuxNavMapping extends WindowsNavMapping {
    @Override
    public Os getMappingOs() {
        return Os.LINUX;
    }
}
