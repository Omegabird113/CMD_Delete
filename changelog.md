### 1.4.3

Changes:

- Sharecode generation should now produce more explicit error messages on failure instead of silently catching them and generating broken sharecodes like `CDS:EV1::0`.
- Properly error on invalid types for boolean values in mappings JSONs in all cases.
- Make the strings for the `/navmappings info` command actually translatable.
- Make some improvements to various strings across the mod.
- The mappings loader now throws for the file not being found if it is a folder/directory instead of a file.
- The licenses of 3rd-party libraries redistributed with CMD + Delete are now properly included in the mod jar.
- Internal code improvements.

Full changelog: https://github.com/Omegabird113/CMD_Delete/compare/1.4.2+mc26.1...1.4.3+mc26.1