### 1.4.0

Changes:

- Fixed a crash that would occur if you loaded Minecraft after deleting a custom mappings JSON file that was previously active.
- Fixed a bug where the cut functionally might not work properly in some places.
- Add a `cmd_delete.forcePreventOverrideMode` JVM property that can be used to force the mod to prevent override mode, even if a user's custom mappings set or a builtin mappings set has enabled. Note that this option is intended for debug purposes and not recommended for normal use.
- Internal code improvements.

Full changelog: https://github.com/Omegabird113/CMD_Delete/compare/1.4.0+mc26.1...1.4.1+mc26.1