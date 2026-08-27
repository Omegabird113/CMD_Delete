# Changelog

This is a collection of all historical CMD + Delete changelogs for every release of the mod. Note that some release
notes were retroactively added and the formatting was retroactively changed in some versions to fix issues and make
inter-version formatting consistent.

### 1.5.0 Alpha 1

[Released](https://github.com/Omegabird113/CMD_Delete/releases/tag/1.5.0-alpha.1%2Bmc26.1) on August 27th, 2026.

This is a pre-release build of the multi-platform support branch
(PR [#28](https://github.com/Omegabird113/CMD_Delete/pull/28)) designed to test the new architecture.

Changes:

- CMD + Delete now has a NeoForge build alongside its Fabric/Quilt one. In the future, this will also allow NF1.21.1
  support without Sinytra Connector and potentially an entire wave of NeoForge backports.
- Added documentation (with its own [website](https://omegabird113.github.io/CMD_Delete/)) for CMD + Delete.
- Internal code improvements

Full changelog: https://github.com/Omegabird113/CMD_Delete/compare/1.4.3+mc26.1...1.5.0-alpha.1+mc26.1

### 1.4.3b

[Released](https://github.com/Omegabird113/CMD_Delete/releases/tag/1.4.3b%2Bmc1.21.11) on August 11th, 2026.

Changes:

- Fixed a dependency configuration issue on 1.21.11 and earlier that actually caused the crash in 1.4.3/1.4.3a.

Release notes:

- This version is not available for the primary (26.1-26.2) build of the mod.

Full changelog: https://github.com/Omegabird113/CMD_Delete/compare/1.4.3a+mc1.21.11...1.4.3b+mc1.21.11

### 1.4.3a

[Released](https://github.com/Omegabird113/CMD_Delete/releases/tag/1.4.3a%2Bmc26.1) on August 11th, 2026.

Changes:

- Revert a change to the source of the game directory that I believe caused a crash on Minecraft 1.21.11 and before.

Release notes:

- This version was affected by an issue where CMD + Delete would crash on Minecraft versions `1.21.11` and before due to
  a dependency configuration error.

Full changelog: https://github.com/Omegabird113/CMD_Delete/compare/1.4.3+mc26.1...1.4.3a+mc26.1

### 1.4.3

[Released](https://github.com/Omegabird113/CMD_Delete/releases/tag/1.4.3%2Bmc26.1) on August 11th, 2026.

Changes:

- Sharecode generation should now produce more explicit error messages on failure instead of silently catching them and
  generating broken sharecodes like `CDS:EV1::0`.
- Properly error on invalid types for boolean values in mappings JSONs in all cases.
- Make the strings for the `/navmappings info` command actually translatable.
- Make some improvements to various strings across the mod.
- The mappings loader now throws for the file not being found if it is a folder/directory instead of a file.
- The licenses of 3rd-party libraries redistributed with CMD + Delete are now properly included in the mod jar.
- Internal code improvements.

Release notes:

- This version was affected by an issue where CMD + Delete would crash on Minecraft versions `1.21.11` and before due to
  a dependency configuration error.

Full changelog: https://github.com/Omegabird113/CMD_Delete/compare/1.4.2+mc26.1...1.4.3+mc26.1

### 1.4.2

[Released](https://github.com/Omegabird113/CMD_Delete/releases/tag/1.4.2%2Bmc26.1) on August 8th, 2026.

Changes:

- Fix an exploit where sharecodes and files could contain ids that would cause them to be placed in a sub-folder of or
  outside the custom mappings folder when imported. Path separation characters are now not allowed in ids.
- Make many strings for the `/navmappings` command translatable.
- Use less technical description for the mod's about information.
- Mark the mod as breakingly incompatible with `macOS Chat Fixes`.
- Internal code improvements.

Full changelog: https://github.com/Omegabird113/CMD_Delete/compare/1.4.1+mc26.1...1.4.2+mc26.1

### 1.4.1

[Released](https://github.com/Omegabird113/CMD_Delete/releases/tag/1.4.1%2Bmc26.1) on August 6th, 2026.

Changes:

- Fixed a crash that would occur if you loaded Minecraft after deleting a custom mappings JSON file that was previously
  active.
- Fixed a bug where the cut functionally might not work properly in some places.
- Add a `cmd_delete.forcePreventOverrideMode` JVM property that can be used to force the mod to prevent override mode,
  even if a user's custom mappings set or a builtin mappings set has enabled. Note that this option is intended for
  debug purposes and not recommended for normal use.
- Internal code improvements.

Full changelog: https://github.com/Omegabird113/CMD_Delete/compare/1.4.0+mc26.1...1.4.1+mc26.1

### 1.4.0

[Released](https://github.com/Omegabird113/CMD_Delete/releases/tag/1.4.0%2Bmc26.1) on August 3rd, 2026.

Changes:

- Increased the Mappings format version to `4`, which:
    - Does not break compatibility this time, **because fv 2 and 3 mappings still load**.
    - Added more vanilla override actions for editing: `OVR_CUT`, `OVR_COPY`, `OVR_PASTE`, and `OVR_SELECT_ALL`.
    - Added support for `strict` mode (enabled when the optional `strict` boolean at the top level of the JSON is set to
      true) which throws exceptions to stop loading instead of logging warnings when the following recoverable errors
      occur:
        - There is a duplicate action registration (including conflicts between different actions)
        - An unknown friendly keyname is specified
        - An unknown action name is specified
        - Action or system names have leading/trailing whitespace or have the wrong capitalization.
        - Numbers that don't adhere to the RegEx `-?(0|[1-9]\d*)` but would pass `Integer.parseInt()` and therefore
          would be accepted not in strict mode.
    - It is now a requirement that all builtin mappings load successfully in strict mode.
- Added a sharecode system for generating long encoded strings of mappings files that are easier to work with than raw
  JSON and the mod can automatically import for you.
- Added the following new builtin mappings sets: `builtin:emacs_windows_linux`, `builtin:emacs_mac`, and
  `builtin:readline`.
- Add the `/navmappings debug dumpDetailedActions` and `/navmappings debug dumpMappingsState` sub-commands.
- Improved the `/navmappings debug aboutCmdDelete` sub-command’s generated text.
- When you switch mappings, and your new mappings fail to load, the previously active mappings will be used instead of
  always switching to default mappings. This comes with other stability & robustness improvements to mappings switching.
- Added a verbose logging mode which promotes `TRACE`/`DEBUG` level logs to `INFO` for inclusion in `latest.log` in all
  environments (especially for the vanilla launcher) for easier bug reports. This mode is only enabled if the
  `cmd_delete.allowVerboseLogs` JVM argument is set to `true`.
- Added support for forcing CMD + Delete not to crash Minecraft on certain fatal exceptions by setting the
  `cmd_delete.forcePreventMinecraftCrashes` JVM argument to `true`. Note that this may allow for undefined behavior
  and/or illegal states after certain types of failures, so this mode is not recommended for normal use.
- Use a shorter description of the mod in its about info.
- Added automated testing for CMD + Delete.
- Internal code improvements.

Changes since RC 1:

- Fixed a bug where attempting to switch to mappings that were already active would fail.
- Internal code improvements.

Full changelog: https://github.com/Omegabird113/CMD_Delete/compare/1.3.1+mc26.1...1.4.0+mc26.1

### 1.4.0 Release Candidate 1

[Released](https://github.com/Omegabird113/CMD_Delete/releases/tag/1.4.0-rc.1%2Bmc26.1) on July 31st, 2026.

Changes:

- When you switch mappings, and your previous mappings fail to load, the previously active mappings will be used instead
  of always switching to default mappings. This comes with other stability & robustness improvements to mappings
  switching.
- Added a `/navmappings debug dumpMappingsState` sub-command.
- Added a hard-coded warning that the builtin mappings `emacs_mac`, `emacs_windows_linux`, and `readline` are
  incomplete & not fully accurate emulates of those pieces of software.
- Added a verbose logging mode which promotes `TRACE`/`DEBUG` level logs to `INFO` for inclusion in `latest.log` in all
  environments (especially for the vanilla launcher) for easier bug reports. This mode is only enabled if the
  `cmd_delete.allowVerboseLogs` JVM argument is set to `true`.
- Fixed a crash that would occur when exporting or importing sharecodes on Minecraft 26.1.2 or earlier (#24).
- Fixed a bug where exporting builtin mappings' sharecodes would not work outside a development environment.
- Fixed a bug where custom mappings JSONs that inherited nothing wouldn't load.
- Added support for forcing CMD + Delete not to crash Minecraft on certain fatal exceptions by setting the
  `cmd_delete.forcePreventMinecraftCrashes` JVM argument to `true`. Note that this may allow for undefined behavior
  and/or illegal states after certain types of failures, so this mode is not recommended for normal use.
- Internal code improvements.

Full changelog: https://github.com/Omegabird113/CMD_Delete/compare/1.4.0-beta.3+mc26.1...1.4.0-rc.1+mc26.1

### 1.4.0 Beta 3

[Released](https://github.com/Omegabird113/CMD_Delete/releases/tag/1.4.0-beta.3%2Bmc26.1) on July 24th, 2026.

Changes:

- Fixed the survival inventory not closing when the recipe book is open with CMD + Delete installed (#23).
- Add the `/navmappings debug dumpDetailedActions` sub-command.
- Improved the `/navmappings debug aboutCmdDelete` sub-command’s generated text.
- Use a shorter description of the mod in its about info.
- Internal code improvements.

Full changelog: https://github.com/Omegabird113/CMD_Delete/compare/1.4.0-beta.2+mc26.1...1.4.0-beta.3+mc26.1

### 1.4.0 Beta 2

[Released](https://github.com/Omegabird113/CMD_Delete/releases/tag/1.4.0-beta.2%2Bmc26.1) on July 21st, 2026.

Changes:

- Added automated testing for my mod.
- Added support for `26.3-snapshot.5` on the 26.3 snapshots build.
- Internal code improvements.

Some notes on Minecraft 26.3 Snapshot 4/5 and above:

- Beta 1 added a separate build for this version, because it breaks compatibility due to the fact that Mojang moved from
  `GLFW` to `SDL3` for the game's input library.
- As a result of this, mappings that use integers instead of friendly keynames will be treated differently on different
  versions (They'll be `GLFW keycodes` on `1.16`-`26.3-snapshot.3` and `SDL3 scancodes` on `26.3-snapshot.4` and above).
  Also, the friendly keyname `f25` only works on GLFW. In a future mappings format version, `f25` will be removed as a
  friendly keyname.
- If you only use friendly keynames in your mappings, (excluding `f25`) they will work fine across versions.
- After 26.3 full release, the primary build of the mod will become the 26.3 build and the 26.1-26.2 build will become
  the secondary build. At this point, the previous 1.21.11 secondary build will lose support and no longer be made. Note
  that the builds for `1.20`-`1.20.1` and `1.20.5`-`1.21.5` are considered LTS builds and will not lose support any time
  soon.

Full changelog: https://github.com/Omegabird113/CMD_Delete/compare/1.4.0-beta.1+mc26.1...1.4.0-beta.2+mc26.1

### 1.4.0 Beta 1

[Released](https://github.com/Omegabird113/CMD_Delete/releases/tag/1.4.0-beta.1%2Bmc26.1) on July 16th, 2026.

Changes:

- Increased the Mappings format version to `4`, which:
    - Does not break compatibility this time, **because fv 2 and 3 mappings still load**.
    - Added more vanilla override actions for editing: `OVR_CUT`, `OVR_COPY`, `OVR_PASTE`, and `OVR_SELECT_ALL`.
    - Added support for `strict` mode (enabled when the optional `strict` boolean at the top level of the JSON is set to
      true) which throws exceptions to stop loading instead of logging warnings when the following recoverable errors
      occur:
        - There is a duplicate action registration (including conflicts between different actions)
        - An unknown friendly keyname is specified
        - An unknown action name is specified
        - Action or system names have leading/trailing whitespace or have the wrong capitalization.
        - Numbers that don't adhere to the RegEx `-?(0|[1-9]\d*)` but would pass `Integer.parseInt()` and therefore
          would be accepted not in strict mode.
    - It is now a requirement that all builtin mappings load successfully in strict mode.
- Added a sharecode system for generating long encoded strings of mappings files that are easier to work with than raw
  JSON and the mod can automatically import for you.
- Added the following new builtin mappings sets: `builtin:emacs_windows_linux`, `builtin:emacs_mac`, and
  `builtin:readline`.
- Internal code improvements.

Full changelog: https://github.com/Omegabird113/CMD_Delete/compare/1.3.1+mc26.1...1.4.0-beta.1+mc26.1

### 1.3.1

[Released](https://github.com/Omegabird113/CMD_Delete/releases/tag/1.3.1%2Bmc26.1) on July 11th, 2026.

Changes:

- Fixed a bug where many vanilla shortcuts for things like copy/paste and select-all would not work with the default
  mappings (#20).

Full changelog: https://github.com/Omegabird113/CMD_Delete/compare/1.3.0+mc26.1...1.3.1+mc26.1

### 1.3.0

[Released](https://github.com/Omegabird113/CMD_Delete/releases/tag/1.3.0%2Bmc26.1) on July 10th, 2026.

Changes:

- Increased the Mappings format version to `3`, which:
    - Does not break compatibility this time, **because fv 2 mappings still load**.
    - Mappings can now set feature flags `overrideVanillaNavigation` and `crossLineSignMovement` within the `flags` top
      level JSON format.
    - Added new vanilla override actions so mappings can customize more keybinds (they only work if
      `overrideVanillaNavigation` is `true`): `OVR_NAV_CHAR_LEFT`, `OVR_NAV_CHAR_RIGHT`, `OVR_SEL_CHAR_LEFT`,
      `OVR_SEL_CHAR_RIGHT`, `OVR_DEL_CHAR_LEFT`, `OVR_DEL_CHAR_RIGHT`, `OVR_NAV_TEXT_UP`, and `OVR_NAV_TEXT_DOWN`.
    - Note that, in the future, any new actions will cause an `fv` bump, but new feature flags will not.
- Added the `/navmappings debug dumpActions` and `/navmappings debug dumpFeatureFlags` subcommands
- Internal code improvements

Changes since RC 1:

- Internal code improvements

Release notes:

- This version was affected by an issue where copy/paste/cut/select-all shortcuts would not work with builtin mappings
  (#20).

Full changelog: https://github.com/Omegabird113/CMD_Delete/compare/1.2.0+mc26.1...1.3.0+mc26.1

### 1.3.0 Release Candidate 1

[Released](https://github.com/Omegabird113/CMD_Delete/releases/tag/1.3.0-rc1%2Bmc26.1) on July 8th, 2026.

Changes:

- Increased the Mappings format version to `3`, which:
    - Does not break compatibility this time, **because fv 2 mappings still load**.
    - Mappings can now set feature flags `overrideVanillaNavigation` and `crossLineSignMovement` within the `flags` top
      level JSON format.
    - Added new vanilla override actions so mappings can customize more keybinds (they only work if
      `overrideVanillaNavigation` is `true`): `OVR_NAV_CHAR_LEFT`, `OVR_NAV_CHAR_RIGHT`, `OVR_SEL_CHAR_LEFT`,
      `OVR_SEL_CHAR_RIGHT`, `OVR_DEL_CHAR_LEFT`, `OVR_DEL_CHAR_RIGHT`, `OVR_NAV_TEXT_UP`, and `OVR_NAV_TEXT_DOWN`.
    - Note that, in the future, any new actions will cause an `fv` bump, but new feature flags will not.
- Added the `/navmappings debug dumpActions` and `/navmappings debug dumpFeatureFlags` subcommands
- Internal code improvements

Release notes:

- This version was affected by an issue where copy/paste/cut/select-all shortcuts would not work with builtin mappings
  (#20).

Full changelog: https://github.com/Omegabird113/CMD_Delete/compare/1.2.0+mc26.1...1.3.0-rc1+mc26.1

### 1.2.0

[Released](https://github.com/Omegabird113/CMD_Delete/releases/tag/1.2.0%2Bmc26.1) on June 27th, 2026.

Changes:

- Increased the Mappings format version to `2`, which:
    - **Breaks compatibility with previous mappings**
    - Allows custom mappings to now inherit from other mappings
    - Mappings now must have an `id` field within the `meta` object which must match their filename
    - Mappings can now set the `key` value in a key registration to an integer of the GLFW keycode instead of only
      allowing CMD + Delete's custom friendly keynames.
- Added the `/navmappings reload`, `/navmappings debug`, `/navmappings import`, and `/navmappings export` subcommands
- Improved logging significantly
- Builtin mappings are now stored in the JSON format
- Added more links and a better description to the mod's about info
- Internal code improvements

Changes since RC2:

- Improved some error messages
- Internal code improvements

Full changelog: https://github.com/Omegabird113/CMD_Delete/compare/1.1.0+mc26.1...1.2.0+mc26.1

### 1.2.0 Release Candidate 2

[Released](https://github.com/Omegabird113/CMD_Delete/releases/tag/1.2.0-rc2%2Bmc26.1) on June 26th, 2026.

Changes:

- Add dedicated subcommands for `/navammpings import` and `/navmappings export`
- Add `/navmappings debug aboutCmdDelete`
- Improve the mod's marketing materials
- Internal code improvements

Full Changelog: https://github.com/Omegabird113/CMD_Delete/compare/1.2.0-rc1+mc26.1...1.2.0-rc2+mc26.1

### 1.2.0 Release Candidate 1

[Released](https://github.com/Omegabird113/CMD_Delete/releases/tag/1.2.0-rc1%2Bmc26.1) on June 25th, 2026.

Changes:

- Increased the Mappings format version to `2` which breaks compatibility with previous mappings
    - Custom mappings can now inherit from other mappings
    - Mappings now must have a `meta.id` field which must match their filename
- Added `/navmappings reload` and `/navmappings debug` sub-commands
- Improved logging significantly
- Builtin mappings are now stored in the JSON format
- Added more links to the mod's about info
- Internal code improvements

Full Changelog: https://github.com/Omegabird113/CMD_Delete/compare/1.1.0+mc26.1...1.2.0-rc1+mc26.1

### 1.1.0

[Released](https://github.com/Omegabird113/CMD_Delete/releases/tag/1.1.0%2Bmc26.1) on June 16th. 2026.

Changes:

- Added support for the new 26.2 update
- Improved the details & formatting of `/navmappings info`
- `/navmappings set builtin windows` and `/navmappings set builtin linux` are no longer allowed (you must use
  `/navmappings set builtin windows_linux`)
- Improve logging
- Improve the validation of custom mappings JSONs
- Fixed a bug where non-json files were shown in `/navmappings list`
- Made improvements which should help the mod's performance & file size
- Internal code improvements
- Made a wave of one-time backport builds which eventually got as far back as 1.14.4

Release notes:

- The 1.14.4, 1.15-1.15.2, and 1.16-1.16.4 builds of this update do not have working command suggestions (the
  /navmappings command works, but it'll appear highlighted in red as if it didn't exist).
- At the time of this release, changelogs were input as a comma-separated single-line list into a GitHub Actions
  workflow. Due to an issue with this workflow's formatting not allowing the ` character, a very broken version of this
  changelog was posted on to modding platforms.
- Many backport builds were released significantly after the main release of this update.

Full Changelog: https://github.com/Omegabird113/CMD_Delete/compare/1.0.0+mc26.1...1.1.0+mc26.1

### 1.0.0

[Released](https://github.com/Omegabird113/CMD_Delete/releases/tag/1.0.0%2Bmc26.1) on June 6th, 2026.

Changes:

- The initial public full release of CMD + Delete

Changes since RC1:

- Some logging improvements
- Internal code improvements

### 1.0.0 Release Candidate 1

[Released](https://github.com/Omegabird113/CMD_Delete/releases/tag/1.0.0-rc1%2Bmc26.1) on June 5th, 2026.

Changes:

- Added book support
- Added a /navmappings command to handle switching out the mappings
- Internal code improvements

Full Changelog: https://github.com/Omegabird113/CMD_Delete/compare/1.0.0-beta4+mc26.1.x...1.0.0-rc1+mc26.1

### 1.0.0 Beta 4

[Released](https://github.com/Omegabird113/CMD_Delete/releases/tag/1.0.0-beta4%2Bmc26.1.x) on May 7th, 2026.

Changes:

- There's a new logo
- CMD + Delete now uses an actions/mapping system internally
- CMD + Delete now supports user-defined custom mappings (there is currently no way to switch between mappings in game,
  you have to edit .active_mappings to change mappings)
- Moved back to the old `cmd_delete` modid
- internal code improvements

Release notes:

- This release still did not work in Books.

Full Changelog: https://github.com/Omegabird113/CMD_Delete/compare/1.0.0-beta3+mc26.1.x...1.0.0-beta4+mc26.1.x

### 1.0.0 Beta 3

[Released](https://github.com/Omegabird113/CMD_Delete/releases/tag/1.0.0-beta3%2Bmc26.1.x) on April 30th, 2026.

Changes:

- Added support for signs
- Internal code improvements

Release notes:

- This release still did not work in Books.

Full Changelog: https://github.com/Omegabird113/CMD_Delete/compare/1.0.0-beta2+mc26.1.x...1.0.0-beta3+mc26.1.x

### 1.0.0 Beta 2

[Released](https://github.com/Omegabird113/CMD_Delete/releases/tag/1.0.0-beta2%2Bmc26.1.x) on April 16th, 2026.

Changes:

- CMD + Delete now supports Minecraft `26.1`, `26.1.1`, and `26.1.2`
- Make the mod match its new ID (`cmd-delete`) in more places
- Internal code improvements

Release notes:

- This release still did not work in Books and Signs.

Full Changelog: https://github.com/Omegabird113/CMD_Delete/compare/1.0.0-beta1+mc26.1...1.0.0-beta2+mc26.1.x

### 1.0.0 Beta 1

[Released](https://github.com/Omegabird113/CMD_Delete/releases/tag/1.0.0-beta1%2Bmc26.1) on March 25th, 2026.

Changes:

- Updated to Minecraft 26.1
- Changed the license from the `MIT` license to the `Apache 2.0` license
- Change the Mod ID to `cmd-delete` from `cmd_delete`
- Internal code improvements
- Removed a lot of debug logging

Release notes:

- This release still did not work in Books and Signs.

Full Changelog: https://github.com/Omegabird113/CMD_Delete/compare/1.0-alpha3+mc1.21.11...1.0.0-beta1+mc26.1

### 1.0 Alpha 3

[Released](https://github.com/Omegabird113/CMD_Delete/releases/tag/1.0-alpha3%2Bmc1.21.11) on February 28th, 2026.

Changes:

- The mod has now been exported properly, so it actually works
- It supports `Mod Menu`
- Internal Code Improvements

Release notes:

- This release still did not work in Books and Signs.
- **This release includes what is essentially a keylogger which puts many keypresses in logs. This was included for
  debugging purposes only, and it is no longer recommended to use this version.**

Full Changelog: https://github.com/Omegabird113/CMD_Delete/compare/1.0-alpha2...1.0-alpha3+mc1.21.11

### 1.0 Alpha 2

[Released](https://github.com/Omegabird113/CMD_Delete/releases/tag/1.0-alpha2) on February 12th, 2026.

Changes:

- Made internal code improvements
- Adding logging *(this includes logging all presses of `arrow` or `backspace`/`delete` keys in chat if debug logging is
  enabled)*
- The logo has been compressed more to reduce the final jar's size

Release notes:

- This release still did not work in Books and Signs.
- **This release includes what is essentially a keylogger which puts many keypresses in logs. This was included for
  debugging purposes only, and it is no longer recommended to use this version.**
- This version was exported using the wrong Gradle task and was distributed as a `-dev.jar` file. Therefore, this
  version will not work in a real Minecraft environment.

Full changelog: https://github.com/Omegabird113/CMD_Delete/compare/1.0-alpha1...1.0-alpha2

### 1.0 Alpha 1

[Released](https://github.com/Omegabird113/CMD_Delete/releases/tag/1.0-alpha1) on February 6th, 2026.

Changes:

- This is the 1st release of CMD + Delete.

Release notes:

- This mod does not currently work in Books, Sign, etc. It should work in chat and creative mode search.
- This version was exported using the wrong Gradle task and was distributed as a `-dev.jar` file. Therefore, this
  version will not work in a real Minecraft environment.