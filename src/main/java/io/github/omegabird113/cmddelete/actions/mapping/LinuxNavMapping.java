package io.github.omegabird113.cmddelete.actions.mapping;

import io.github.omegabird113.cmddelete.actions.OsConstant;

public class LinuxNavMapping extends WindowsNavMapping {
    @Override
    public OsConstant getMappingOs() {
        return OsConstant.LINUX;
    }
}
