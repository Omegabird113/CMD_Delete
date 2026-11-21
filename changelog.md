### 1.5.0

Changes:

- CMD + Delete is now based on a multiplatform architecture with a new NeoForge/Forge build.
- Increased the Mappings format version to `5`, which:
	- Does not break compatibility this time, **because fv 2, 3, and 4 mappings still load**.
	- Added support for the new optional `meta.credits` and `meta.license` fields.
	- Case/whitespace normalization is no longer applied to strings in JSONs outside strict mode.
	- The `f25` keyname throws an exception now.
- Added documentation (with its own [website](https://omegabird113.github.io/CMD_Delete/)) for CMD + Delete.
- Updated the main branch to Minecraft 26.3.
- Added a `/navmappings debug sendDocsLink` sub-command.
- Added a `/navmappings debug sendIssuesLink` sub-command.
- Add a `/navmappings debug generateShareCodeOfText` sub-command.
- Add machine-translated [Crowdin translations](https://crowdin.com/project/cmd-delete-mcmod).
- Use `win` instead of `sup` for more native terminology in KeyCombo names on Windows.
- Added a new `builtin:vanilla` mappings set.
- Added a `nativeStyleWordBoundaries` feature flag that allows support for using native OS style detection of where word
  boundaries are.
- Mappings should now always be sorted in alphabetical order when listed.
- Improve logging and fix some issues with inaccurate logging.
- Fixed an exploit where there was no maximum cap on how much data Gzip would decompress from a sharecode which allowed Zip Bombs to be possible.
- Improve the experience of using sharecodes by validating the mappings they represent fully when they are imported. This means it will error early on invalid mappings in sharecodes instead of erroring when first activating imported mappings.
- Internal code improvements.

Full changelog: https://github.com/Omegabird113/CMD_Delete/compare/1.4.3a+mc26.1...1.5.0+mc26.3
