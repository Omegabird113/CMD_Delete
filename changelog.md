### 1.5.0 Beta 1

Changes:

- Increased the Mappings format version to `5`, which:
	- Does not break compatibility this time, **because fv 2, 3, and 4 mappings still load**.
	- Added support for the new optional `meta.credits` and `meta.license` fields.
	- Case/whitespace normalization is no longer applied to strings in JSONs outside strict mode.
	- The `f25` keyname throws an exception now.
- CMD + Delete now uses a fully complete multi-platform architecture.
- Internal code improvements.

Full changelog: https://github.com/Omegabird113/CMD_Delete/compare/1.5.0-alpha.1+mc26.1...1.5.0-beta.1+mc26.1
