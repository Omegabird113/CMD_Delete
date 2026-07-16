### 1.4.0 Beta 1

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
- Internal code improvements.

Full changelog: https://github.com/Omegabird113/CMD_Delete/compare/1.3.1+mc26.1...1.4.0-beta.1+mc26.1
