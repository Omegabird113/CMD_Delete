# CMD + Delete

[![Build status](https://img.shields.io/github/actions/workflow/status/Omegabird113/cmd_delete/build.yml)](https://github.com/Omegabird113/cmd_delete/actions/workflows/build.yml)
[![GitHub License](https://img.shields.io/github/license/Omegabird113/cmd_delete)](https://github.com/Omegabird113/cmd_delete/blob/master/LICENSE)
[![Modrinth Downloads](https://img.shields.io/modrinth/dt/xrOWVab9)](https://modrinth.com/mod/cmd-delete)
[![CurseForge Downloads](https://img.shields.io/curseforge/dt/1455987)](https://www.curseforge.com/minecraft/mc-mods/cmd-delete)
[![GitHub Downloads (all assets, all releases)](https://img.shields.io/github/downloads/Omegabird113/cmd_delete/total)](https://github.com/Omegabird113/cmd_delete/releases)

In Minecraft on macOS, pressing `option` and `backspace` deletes a single character, and pressing `command` and `backspace` deletes a word. **This is completely inconsistent with the native OS**, so I fixed it:

Not only does CMD + Delete fix the macOS text behavior, it also expands Minecraft into a fully user-configurable text editing/navigation shortcut framework!

## Important Context

1. If you use this mod on Windows or Linux, and you don't use custom mappings, nothing will change from vanilla Minecraft.
2. If you have any problems with this mod, **please report them as Issues on GitHub**. This mod is Open Source and Pull Requests are very much welcome! :)

> [!Note]
>
> CMD + Delete is a **Fabric mod** which also supports interopting with Quilt and Forge/NeoForge (but **only with [Sinytra Connector](https://modrinth.com/mod/connector)**).

![A showcase of opt/cmd + backspace in chat.](./showcase.gif)

## Custom Mappings

You can define your own custom mappings in configuration files with the location & name: `<minecraft install>/config/cmd_delete/mappings/<id>.json`. These allow you to customize the text navigation shortcut behavior in Minecraft for your own taste & configuration. 

### Examples

sample.json:
```json
{
  "fv": 3,
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

mac.json:
```json
{
  "fv": 3,
  "meta": {
    "name": "Mac mappings",
    "author": "$$cmd_delete$$",
    "description": "Pre-bundled mappings for macOS.",
    "version": "$$cmd_delete$$",
    "id": "mac",
    "systems": [
      "mac"
    ]
  },
  "actions": {
    "NAV_TEXT_START": [
      {"key": "up", "superCommand": true, "altOption": false, "shift": false}
    ],
    "SEL_TEXT_START": [
      {"key": "up", "superCommand": true, "altOption": false, "shift": true}
    ],
    "NAV_TEXT_END": [
      {"key": "down", "superCommand": true, "altOption": false, "shift": false}
    ],
    "SEL_TEXT_END": [
      {"key": "down", "superCommand": true, "altOption": false, "shift": true}
    ],
    "NAV_LINE_LEFT": [
      {"key": "left", "superCommand": true, "altOption": false, "shift": false}
    ],
    "SEL_LINE_LEFT": [
      {"key": "left", "superCommand": true, "altOption": false, "shift": true}
    ],
    "NAV_LINE_RIGHT": [
      {"key": "right", "superCommand": true, "altOption": false, "shift": false}
    ],
    "SEL_LINE_RIGHT": [
      {"key": "right", "superCommand": true, "altOption": false, "shift": true}
    ],
    "NAV_WORD_LEFT": [
      {"key": "left", "superCommand": false, "altOption": true, "shift": false}
    ],
    "SEL_WORD_LEFT": [
      {"key": "left", "superCommand": false, "altOption": true, "shift": true}
    ],
    "NAV_WORD_RIGHT": [
      {"key": "right", "superCommand": false, "altOption": true, "shift": false}
    ],
    "SEL_WORD_RIGHT": [
      {"key": "right", "superCommand": false, "altOption": true, "shift": true}
    ],
    "DEL_LINE_LEFT": [
      {"key": "backspace", "superCommand": true, "altOption": false}
    ],
    "DEL_LINE_RIGHT": [
      {"key": "delete", "superCommand": true, "altOption": false}
    ],
    "DEL_WORD_LEFT": [
      {"key": "backspace", "superCommand": false, "altOption": true}
    ],
    "DEL_WORD_RIGHT": [
      {"key": "delete", "superCommand": false, "altOption": true}
    ],
    "SEL_TEXT_UP": [
      {"key": "up", "superCommand": false, "altOption": false, "shift": true}
    ],
    "SEL_TEXT_DOWN": [
      {"key": "down", "superCommand": false, "altOption": false, "shift": true}
    ],
    "OVR_NAV_CHAR_LEFT": [
      {"key": "left", "superCommand": false, "altOption": false, "shift": false}
    ],
    "OVR_NAV_CHAR_RIGHT": [
      {"key": "right", "superCommand": false, "altOption": false, "shift": false}
    ],
    "OVR_SEL_CHAR_LEFT": [
      {"key": "left", "superCommand": false, "altOption": false, "shift": true}
    ],
    "OVR_SEL_CHAR_RIGHT": [
      {"key": "right", "superCommand": false, "altOption": false, "shift": true}
    ],
    "OVR_DEL_CHAR_LEFT": [
      {"key": "backspace", "superCommand": false, "altOption": false, "shift": false}
    ],
    "OVR_DEL_CHAR_RIGHT": [
      {"key": "delete", "superCommand": false, "altOption": false, "shift": false}
    ],
    "OVR_NAV_TEXT_UP": [
      {"key": "up", "superCommand": false, "altOption": false, "shift": false}
    ],
    "OVR_NAV_TEXT_DOWN": [
      {"key": "down", "superCommand": false, "altOption": false, "shift": false}
    ]
  },
  "flags": {
    "overrideVanillaNavigation": true,
    "crossLineSignMovement": true
  }
}
```

### Features
Custom mappings can:
- Use wildcard expansion where any modifier not included in a key combo is treated as `true` OR `false`.
- Use friendly keyname strings defined by CMD + Delete instead of GLFW keycodes. If a keyname is not defined, you can still choose to use a raw GLFW keycode in your JSON.
- Inherit keybinds from the builtin mappings or other custom mappings by setting the top-level `inherits` value to `custom:<id>` to inherit a custom JSON, or `builtin:<id>` or just `<id>` to inherit a builtin JSON.
- Patch & override the mappings they inherit by setting the keybind to have `enabled` property as `false`. Note that CMD + Delete always removes your disabled keybinds before adding your enabled keybinds, allowing a patch-then-reimplement pattern to be easily done.
- Set certain boolean feature flags to control the mod's behavior.

Note that:
- The format version `fv` is currently `3` and your mappings should match that (Though old mappings of format version `2` are still supported in-game).
- The `meta.id` field must exactly match the filename without the `.json` extension

### The /navmappings command
CMD + Delete includes an in-game, client `/navmappings` command for checking, switching, importing, exporting, and debugging mappings without manually touching config files.

Here are all the subcommands:
- `/navmappings info`: displays info about the currently loaded mappings.
- `/navmappings list`: displays a list of the namespaced IDs of the currently available mappings, which you could switch to.
- `/navmappings set default`: switch to the default mappings behavior.
- `/navmappings set builtin <non-namespaced id>`: switch a specific OS's builtin mappings JSON.
- `/navmappings set custom <non-namespaced id>`: switch to one of your own custom mappings JSONs.
- `/navmappings reload`: reload the currently active mappings.
- `/navmappings export <type> <non-namespaced id> <absolute location>`: make a copy of a mappings JSON to another location on your system so you can copy it.
- `/navmappings import <non-namespaced id> <absolute location>`: import a copy of a JSON you made from another location on your system into the custom JSONs folder for you to use as custom mappings.
- `/navmappings debug`: a utility that provides dumps of information like info about CMD + Delete, a dump of the friendly keynames which CMD + Delete mappings JSONs support, or a raw dump of the internal loaded mappings' registry.

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
