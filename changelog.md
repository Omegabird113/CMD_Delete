### 1.4.0 Beta 2

Changes:

- Added automated testing for my mod.
- Added support for `26.3-snapshot.5` on the 26.3 snapshots build.
- Internal code improvements.

Some notes on Minecraft 26.3 Snapshot 4/5 amd above:

- Beta 1 added a separate build for this version, because it breaks compatability due to the fact that Mojang moved from `GLFW` to `SDL3` for the game's input library.
- As a result of this, mappings that use integers instead of friendly keynames will be treated differently on different versions (They'll be `GLFW keycodes` on `1.16`-`26.3-snapshot.3` and `SDL3 scancodes` on `26.3-snapshot.4` and above). Also, the friendly keyname `f25` only works on GLFW. In a future mappings format version, `f25` will be removed as a friendly keyname.
- If you only use friendly keynames in your mappings, (excluding `f25`) they will work fine across versions.
- After 26.3 full release, the primary build of the mod will become the 26.3 build and the 26.1-26.2 build will become the secondary build. At this point, the previous 1.21.11 secondary build will lose support and no longer be made. Noe that the builds for `1.20`-`1.20.1` and `1.20.5`-`1.21.5` are considered LTS builds and will not lose support any time soon.

Full changelog: https://github.com/Omegabird113/CMD_Delete/compare/1.4.0-beta.1+mc26.1...1.4.0-beta.2+mc26.1