# 1.2.0 

Changes:

- Increased the Mappings format version to `2`, which:
  - **Breaks compatibility with previous mappings**
  - Allows custom mappings to now inherit from other mappings
  - Mappings now must have an `id` field within the `meta` object which must match their filename
  - Mappings can now set the `key` value in a key registration to an integer of the GLFW keycode instead of only allowing CMD + Delete's custom friendly keynames.
- Added the `/navmappings reload`, `/navmappings debug`, `/navmappings import`, and `/navmappings export` subcommands
- Improved logging significantly
- Builtin mappings are now stored in the JSON format
- Added more links and a better description to the mod's about info
- Internal code improvements


Changes since RC2:

- Improved some error messages
- Internal code improvements

Full changelog: https://github.com/Omegabird113/CMD_Delete/compare/1.1.0+mc26.1...1.2.0+mc26.1
