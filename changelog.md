### 1.4.0

Changes:

- Increased the Mappings format version to `4`, which:
    - Does not break compatibility this time, **because fv 2 and 3 mappings still load**.
    - Added more vanilla override actions for editing: `OVR_CUT`, `OVR_COPY`, `OVR_PASTE`, and `OVR_SELECT_ALL`.
    - Added support for `strict` mode (enabled when the optional `strict` boolean at the top level of the JSON is set to true) which throws exceptions to stop loading instead of logging warnings when the following recoverable errors occur:
        - There is a duplicate action registration (including conflicts between different actions)
        - An unknown friendly keyname is specified
        - An unknown action name is specified
        - Action or system names have leading/trailing whitespace or have the wrong capitalization.
        - Numbers that don't adhere to the RegEx `-?(0|[1-9]\d*)` but would pass `Integer.parseInt()` and therefore would be accepted not in strict mode.
    - It is now a requirement that all builtin mappings load successfully in strict mode.
- Added a sharecode system for generating long encoded strings of mappings files that are easier to work with than raw JSON and the mod can automatically import for you.
- Added the following new builtin mappings sets: `builtin:emacs_windows_linux`, `builtin:emacs_mac`, and `builtin:readline`.
- Add the `/navmappings debug dumpDetailedActions` and `/navmappings debug dumpMappingsState` sub-commands.
- Improved the `/navmappings debug aboutCmdDelete` sub-command’s generated text.
- When you switch mappings, and your new mappings fail to load, the previously active mappings will be used instead of always switching to default mappings. This comes with other stability & robustness improvements to mappings switching.
- Added a verbose logging mode which promotes `TRACE`/`DEBUG` level logs to `INFO` for inclusion in `latest.log` in all environments (especially for the vanilla launcher) for easier bug reports. This mode is only enabled if the `cmd_delete.allowVerboseLogs` JVM argument is set to `true`.
- Added support for forcing CMD + Delete not to crash Minecraft on certain fatal exceptions by setting the `cmd_delete.forcePreventMinecraftCrashes` JVM argument to `true`. Note that this may allow for undefined behavior and/or illegal states after certain types of failures, so this mode is not recommended for normal use.
- Use a shorter description of the mod in its about info.
- Added automated testing for CMD + Delete.
- Internal code improvements.

Changes since RC 1:

- Fixed a bug where attempting to switch to mappings that were already active would fail.
- Internal code improvements.

Full changelog: https://github.com/Omegabird113/CMD_Delete/compare/1.3.1+mc26.1...1.4.0+mc26.1