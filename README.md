# CMD + Delete

[![Build status](https://img.shields.io/github/actions/workflow/status/Omegabird113/cmd_delete/build.yml)](https://github.com/Omegabird113/cmd_delete/actions/workflows/build.yml)
[![GitHub License](https://img.shields.io/github/license/Omegabird113/cmd_delete)](https://github.com/Omegabird113/cmd_delete/blob/master/LICENSE)
[![Modrinth Downloads](https://img.shields.io/modrinth/dt/xrOWVab9)](https://modrinth.com/mod/cmd-delete)
[![CurseForge Downloads](https://img.shields.io/curseforge/dt/1455987)](https://www.curseforge.com/minecraft/mc-mods/cmd-delete)
[![GitHub Downloads (all assets, all releases)](https://img.shields.io/github/downloads/Omegabird113/cmd_delete/total)](https://github.com/Omegabird113/cmd_delete/releases)

In Minecraft on macOS, pressing `option` + `backspace` deletes a single character, and pressing `command` + `backspace` deletes a word. **This is completely inconsistent with the native OS**, so I fixed it...

This mod also functions as a fully configurable text editing/navigation shortcut framework for Minecraft with custom mappings JSONs and sharecodes, in-game switching, and more!

## Some Context

1. If you use this mod on Windows or Linux, and you don't use custom mappings, nothing will change from vanilla Minecraft.
2. If you have any problems with this mod, **please report them as Issues on GitHub**. This mod is Open Source and Pull Requests are very much welcome! :)

> [!Note]
>
> CMD + Delete is a **Fabric mod** which also supports Quilt for Minecraft 1.14.4 to the latest version.
> 
> It also supports Forge 1.20.1 and NeoForge 1.21.1, but **only with [Sinytra Connector](https://modrinth.com/mod/connector)**.

![A showcase of opt/cmd + backspace in chat.](./showcase.gif)

## Builtin Mappings' Shortcuts

By default, CMD + Delete will detect if you're using macOS, and if you are it'll set you to those shortcuts, otherwise it will set you to use Windows/Linux shortcuts.

| Action                  | macOS                    | Windows / Linux           |
|-------------------------|--------------------------|---------------------------|
| Delete previous word    | `option` + `backspace`   | `ctrl` + `backspace`      |
| Delete next word        | `option` + `delete`      | `ctrl` + `delete`         |
| Delete to start of line | `cmd` + `backspace`      | N/A                       |
| Delete to end of line   | `cmd` + `delete`         | N/A                       |
| Move to previous word   | `option` + `←`           | `ctrl` + `←`              |
| Move to next word       | `option` + `→`           | `ctrl` + `→`              |
| Move to start of line   | `cmd` + `←`              | `home`                    |
| Move to end of line     | `cmd` + `→`              | `end`                     |
| Move to start of text   | `cmd` + `↑`              | `ctrl` + `home`           |
| Move to end of text     | `cmd` + `↓`              | `ctrl` + `end`            |
| Select to previous word | `option` + `shift` + `←` | `ctrl` + `shift` + `←`    |
| Select to next word     | `option` + `shift` + `→` | `ctrl` + `shift` + `→`    |
| Select to start of line | `cmd` + `shift` + `←`    | `shift` + `home`          |
| Select to end of line   | `cmd` + `shift` + `→`    | `shift` + `end`           |
| Select to start of text | `cmd` + `shift` + `↑`    | `ctrl` + `shift` + `home` |
| Select to end of text   | `cmd` + `shift` + `↓`    | `ctrl` + `shift` + `end`  |
| Select up one line      | `shift` + `↑`            | `shift` + `↑`             |
| Select down one line    | `shift` + `↓`            | `shift` + `↓`             |

## Other Builtin Mappings

CMD + Delete also now provides these other builtin mappings:
- `builtin:emacs_windows_linux` the standard Windows/Linux shortcuts for text navigation in Emacs to the best replication I can make (they're not perfect).
- `builtin:emacs_mac` the standard macOS Cocoa shortcuts for text navigation in Emacs to the best replication I can make (they're not perfect).
- `builtin:readline` the standard shortcuts for text navigation in GNU Readline to the best replication I can make (they're not perfect).

## Custom Mappings

You can define your own custom mappings in configuration files with the location & name: `<minecraft install>/config/cmd_delete/mappings/<id>.json`. These allow you to customize the text navigation shortcut behavior in Minecraft for your own taste & configuration. You can also easily share your mappings via sharecodes!

### Examples

sample.json file:
```json
{
  "fv": 4,
  "inherits": "builtin:mac",
  "meta": {
    "name": "Example custom mapping",
    "author": "Omegabird113",
    "description": "This example demonstrates inheritance, patching, and more! Though, these keybinds are made up and people wouldn't use these most likely...",
    "version": "2.1.0",
    "id": "sample",
    "systems": [
      "mac", "windows", "linux"
    ]
  },
  "actions": {
    "NAV_TEXT_START": [
      {
        "key": "up",
        "superCommand": true,
        "altOption": false,
        "shift": false,
        "enabled": false
      },
      {
        "key": "e",
        "superCommand": true
      },
      {
        "key": "home"
      }
    ]
  }
}
```

`mac` sharecode:
`CDS:EV1:tMeXxZVB45VieA3HvggAeo3p5zjrCbj7crBL5PbmPwdWGpgx8nz6vXnqxX48Cgc5BHZX63EYegfSTth3sTL16tud1DU6KFQKQNLmEtrhaoojTDiqtcm88t2ePt3yKWY4NnfPEUBhPqMgmoFCZwkfgVMpmKr6L2QYWmtruBniZRgBG5hxpA95W2E3pn5dv9SnDhpQenXnr2cWvyENAGmNpTzpUyktLDQoEZGJZE8CKK84G8jXsTf81f1Kxojv88pF66AeMYEqMCYCki7jafgTDi4YspYGxLvqrd9eZPGUPy1JvV588pD6MFT13sYK2m5yiJk5qGwqMg17DSbXjEUzU3dLN322AqPDP4Xvm3n1nztxo2fuCeGPpmD7poJDbHCws7RNKtVcgnpas8EhxHDUBqZPwyDWjbUwqxNfmqSvKQwYQbU3jTt527Z89g1Li4QPBto4bNFc9Fr7jWYQVdu8k7PxahZEFjPes5YrufVoNtU1ZC8VuMreMQNeBUz9MH5qN3oDbdYu7L5Jupr6XBrpKMoVzGGS4ygdcBMfG7ZZF6ruzNjTGDjHvrNDGDuJXpXfnccn9ami1xM4SipedBHugVM56FyR26MsRcddBMwmnSr4LTrGT6hofbsGuapfVJZUNfgab6WvHDAgHMXFQD9reFnfuMQ3KNtzMFzPJXSyVLfbFYDjPxv94yB5:2419707130`

### Technical Features
Custom mappings can:
- Use wildcard expansion where any modifier not included in a key combo is treated as `true` OR `false`.
- Use friendly keyname strings defined by CMD + Delete instead of GLFW keycodes. If a keyname is not defined, you can still choose to use a raw GLFW keycode in your JSON.
- Inherit keybinds from the builtin mappings or other custom mappings by setting the top-level `inherits` value to `custom:<id>` to inherit a custom JSON, or `builtin:<id>` or just `<id>` to inherit a builtin JSON.
- Patch & override the mappings they inherit by setting the keybind to have `enabled` property as `false`. Note that CMD + Delete always removes your disabled keybinds before adding your enabled keybinds, allowing a patch-then-reimplement pattern to be easily done.
- Set certain boolean feature flags to control the mod's behavior.
- Enable `strict` mode (as a top-level JSON boolean) which would prevent your mappings from loading if they have certain recoverable issues like keybind conflicts or unknown actions.

Note that:
- The format version `fv` is currently `4` and your mappings should match that (Though old mappings of format versions `2` and `3` are still supported in-game).
- The `meta.id` field must exactly match the filename without the `.json` extension
- In some places, you'll see a mappings id of `""` (empty string), that refers to the default mappings behavior of loading either `builtin:windows_linux` or `builtin:mac` depending on the user's OS. Note that `"inherits": ""` means a mappings JSON does not inherit anything, so it is impossible to inherit from the default behavior.

### Sharecode Syntax

| Segment (in order) | Description                                                          |
|:-------------------|:---------------------------------------------------------------------|
| `CDS`              | The prefix for a `CMD + Delete share`.                               |
| `EV1`              | The version of the sharecode encoding format (currently `1`).        |
| the payload        | The `Base58` encoded `GZip` compressed payload of the mappings JSON. |
| the checksum       | The `CRC32` checksum of the uncompressed mappings JSON.              |

## The /navmappings Command
CMD + Delete includes an in-game, client `/navmappings` command for checking, switching, importing, exporting, and debugging mappings without manually touching config files.

| Command                                                                   | Description                                                                                                                                                                                                    |
|:--------------------------------------------------------------------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `/navmappings info`                                                       | Displays info about the currently loaded mappings.                                                                                                                                                             |
| `/navmappings list`                                                       | Displays a list of the namespaced IDs of the currently available mappings, which you could switch to.                                                                                                          |
| `/navmappings set default`                                                | Switch to the default mappings behavior.                                                                                                                                                                       |
| `/navmappings set builtin <non-namespaced id>`                            | Switch a specific OS's builtin mappings JSON.                                                                                                                                                                  |
| `/navmappings set custom <non-namespaced id>`                             | Switch to one of your own custom mappings JSONs.                                                                                                                                                               |
| `/navmappings reload`                                                     | Reload the currently active mappings.                                                                                                                                                                          |
| `/navmappings export <type> file <non-namespaced id> <absolute location>` | Make a copy of a mappings JSON to another location on your system so you can copy it.                                                                                                                          |
| `/navmappings export <type> sharecode <non-namespaced id>`                | Make a sharecode copy of a set of mappings and prints it to the chat & copies it to your clipboard.                                                                                                            |
| `/navmappings import file <absolute location>`                            | Import a copy of a JSON you made from another location on your system into the custom JSONs folder for you to use as custom mappings.                                                                          |
| `/navmappings import sharecode clipboard`                                 | Import a copy of a set of mappings from a sharecode you have copied to your clipboard.                                                                                                                         |
| `/navmappings import sharecode chat <sharecode>`                          | Import a copy of a set of mappings from a sharecode you entered into the command.                                                                                                                              |
| `/navmappings debug`                                                      | A utility that provides dumps of information like info about CMD + Delete, a dump of the friendly keynames which CMD + Delete mappings JSONs support, or a raw dump of the internal loaded mappings' registry. |