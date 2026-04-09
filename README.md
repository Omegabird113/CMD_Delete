# CMD + Delete

[![Build status](https://img.shields.io/github/actions/workflow/status/Omegabird113/cmd_delete/build.yml)](https://github.com/Omegabird113/cmd_delete/actions/workflows/build.yml)
[![GitHub License](https://img.shields.io/github/license/Omegabird113/cmd_delete)](https://github.com/Omegabird113/cmd_delete/blob/master/LICENSE)
[![Modrinth Downloads](https://img.shields.io/modrinth/dt/xrOWVab9)](https://modrinth.com/mod/cmd-delete)
[![CurseForge Downloads](https://img.shields.io/curseforge/dt/1455987)](https://www.curseforge.com/minecraft/mc-mods/cmd-delete)
[![GitHub Downloads (all assets, all releases)](https://img.shields.io/github/downloads/Omegabird113/cmd_delete/total)](https://github.com/Omegabird113/cmd_delete/releases)

In Minecraft on macOS, pressing `option` and `backspace` deletes a single character, and pressing `command` and `backspace` deletes a word. **This is completely inconsistent with the native OS**, so I fixed it:

## Important Context

1. As of right now, **this only works in some places**. This includes chat and the Creative Menu Search. It does **not** include Signs or Books.
2. If you use this mod **on Windows** (or Linux), **nothing should change** from the Vanilla behavior
3. If you have any problems with this mod, **please report them as Issues on GitHub**. Pull Requests are also welcome.

## All the New Key Combinations

- `option` + `backspace` (`control` + `backspace` on Windows): deletes previous word
- `command` + backspace (no Windows equivalent): deletes all of the line previous to cursor
- `option` + left/right arrow keys (`control` + left/right arrow keys on Windows): moves to one word in the direction of the arrow
- `command` + left/right arrow keys (no Windows equivalent): moves to the start/end of the line in the direction of the arrow
- `option` + `shift` + left/right arrow keys (`control` + `shift` + left/right arrow keys on Windows): moves to & selects one word in the direction of the arrow
- `command` + `shift` + left/right arrow keys (no Windows equivalent): moves to & selects the start/end of the line in the direction of the arrow
