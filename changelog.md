### 1.4.0 Release Candidate 1

Changes:

- When you switch mappings, and your previous mappings fail to load, the previously active mappings will be used instead of always switching to default mappings. This comes with other stability & robustness improvements to mappings switching.
- Added a `/navmappings debug dumpMappingsState` sub-command.
- Added a hard-coded warning that the builtin mappings `emacs_mac`, `emacs_windows_linux`, and `readline` are incomplete & not fully accurate emulates of those pieces of software. 
- Added a verbose logging mode which promotes `TRACE`/`DEBUG` level logs to `INFO` for inclusion in `latest.log` in all environments (especially for the vanilla launcher) for easier bug reports. This mode is only enabled if the `cmd_delete.allowVerboseLogs` JVM argument is set to `true`.
- Fixed a crash that would occur when exporting or importing sharecodes on Minecraft 26.1.2 or earlier (#24).
- Fixed a bug where exporting builtin mappings' sharecodes would not work outside of a development environment.
- Fixed a bug where custom mappings JSONs that inherited nothing wouldn't load.
- Added support for forcing CMD + Delete not to crash Minecraft on certain fatal exceptions by setting the `cmd_delete.forcePreventMinecraftCrashes` JVM argument to `true`. Note that this may allow for undefined behavior and/or illegal states after certain types of failures, so this mode is not recommended for normal use.
- Internal code improvements.

Full changelog: https://github.com/Omegabird113/CMD_Delete/compare/1.4.0-beta.3+mc26.1...1.4.0-rc.1+mc26.1