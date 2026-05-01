package io.github.omegabird113.cmd_delete.actions.mapping;

import io.github.omegabird113.cmd_delete.actions.OsConstant;

public class LinuxNavMapping extends WindowsNavMapping {
    @Override
    public OsConstant getMappingOs() {
        return OsConstant.LINUX;
    }
}
