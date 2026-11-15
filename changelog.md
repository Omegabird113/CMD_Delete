### 1.5.0 Release Candidate 1

Changes:

- Fixed an exploit where there was no maximum cap on how much data GZip would decompress from a sharecode which allowed Zip Bombs to be possible.
- Improve the experience of using sharecodes by validating the mappings they represent fully when they are imported. This means it will error early on invalid mappings in sharecodes instead of erroring when first activating imported mappings.
- Add the `/navmappings debug generateShareCodeOfText` sub-command.
- Internal code improvements.

Full changelog: https://github.com/Omegabird113/CMD_Delete/compare/1.5.0-beta.4+mc26.3...1.5.0-rc.1+mc26.3
