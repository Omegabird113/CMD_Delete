### 1.3.0 Release Candidate 1

Changes:

- Increased the Mappings format version to `3`, which:
  - Does not break compatability this time, **because fv 2 mappings still load**.
  - Mappings can now set feature flags `overrideVanillaNavigation` and `crossLineSignMovement` within the `flags` top level JSON format.
  - Added new vanilla override actions so mappings can customize more keybinds (they only work if `overrideVanillaNavigation` is `true`): `OVR_NAV_CHAR_LEFT`, `OVR_NAV_CHAR_RIGHT`, `OVR_SEL_CHAR_LEFT`, `OVR_SEL_CHAR_RIGHT`, `OVR_DEL_CHAR_LEFT`, `OVR_DEL_CHAR_RIGHT`, `OVR_NAV_TEXT_UP`, and `OVR_NAV_TEXT_DOWN`.
  - Note that, in the future, any new actions will cause an `fv` bump, but new feature flags will not.
- Added the `/navmappings debug dumpActions` and `/navmappings debug dumpFeatureFlags` subcommands
- Internal code improvements

Full changelog: https://github.com/Omegabird113/CMD_Delete/compare/1.2.0+mc26.1...1.3.0-rc1+mc26.1
