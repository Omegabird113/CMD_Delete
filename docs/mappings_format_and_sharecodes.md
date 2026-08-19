# The Mappings Format & Sharecodes

You can define your own custom mappings in configuration files with the location & name: `<minecraft install>/config/cmd_delete/mappings/<id>.json`. These allow you to customize the text navigation shortcut behavior in Minecraft for your own taste & configuration. You can also easily share your mappings via sharecodes!

## Mappings Format Features

Custom mappings can:

- Use wildcard expansion where any modifier not included in a key combo is treated as `true` OR `false`.
- Use [friendly keyname strings](friendly_keynames.md) defined by CMD + Delete instead of GLFW keycodes. If a keyname is not defined, you can still choose to use a raw GLFW keycode in your JSON.
- Inherit keybinds from the builtin mappings or other custom mappings by setting the top-level `inherits` value to `custom:<id>` to inherit a custom JSON, or `builtin:<id>` or just `<id>` to inherit a builtin JSON.
- Patch & override the mappings they inherit by setting the keybind to have `enabled` property as `false`. Note that CMD + Delete always removes your disabled keybinds before adding your enabled keybinds, allowing a patch-then-reimplement pattern to be easily done.
- Set certain boolean feature flags to control the mod's behavior.
- Enable `strict` mode (as a top-level JSON boolean) which would prevent your mappings from loading if they have certain recoverable issues like keybind conflicts or unknown actions.

Note that:

- The format version `fv` is currently `4` and your mappings should match that (Though old mappings of format versions `2` and `3` are still supported in-game).
- The `meta.id` field must exactly match the filename without the `.json` extension
- In some places, you'll see a mappings id of `""` (empty string), that refers to the default mappings behavior of loading either `builtin:windows_linux` or `builtin:mac` depending on the user's OS. Note that `"inherits": ""` means a mappings JSON does not inherit anything, so it is impossible to inherit from the default behavior.
- The parser generally enforces JSON value types strictly to avoid errors.

### Example

Minimum possible mappings example of the `my-mappings.json` file 
```json
{
  "fv": 4,
  "meta": {
    "id": "my-mappings",
    "systems": ["mac"]
  },
  "actions": {
    "NAV_TEXT_START": [
      {
        "key": "home"
      }
    ]
  }
}
```

Complete example of the `sample.json` file:
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
  },
  "flags": {
    "overrideVanillaNavigation": true,
    "crossLineSignMovement": false
  }
}
```

`builtin:mac` is being inherited from and this gets all its mappings. `builtin:mac` defines `{"key": "up", "superCommand": true, "altOption": false, "shift": false}` for `NAV_TEXT_START`. This mappings set disables that keybinding and replaces it with some new ones.

### Meta Fields

Below is a table explaining each meta field and whether it is required:

| Field name    | Type           | Required                                   | Description                                                                                                                                                                                        |
|:--------------|:---------------|:-------------------------------------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `id`          | `String`       | Yes                                        | The non-namespaced ID of your mappings JSON. Note that it must match the filename of the JSON (unless being imported through /navmappings) and may not contain the `/` or `\` characters.          |
| `systems`     | `List<String>` | Yes                                        | A metadata field of which systems your mappings are designed to run on. Note that it is not enforced that a user is on one of these systems. Acceptable systems are `windows`, `mac`, and `linux`. |
| `name`        | `String`       | No (Defaults to `Unnamed Custom Mappings`) | The display/common name of your mappings set.                                                                                                                                                      |
| `author`      | `String`       | No (Defaults to `unknown`)                 | The creator(s) of the mappings JSON.                                                                                                                                                               |
| `description` | `String`       | No (Defaults to `No description provided`) | A detailed description of the mappings set.                                                                                                                                                        |
| `version`     | `String`       | No (Defaults to `unknown`)                 | The version/revision of the mappings set                                                                                                                                                           |

### Actions JSON Syntax

The primary part of a Mappings JSON is the actual `actions` section mapping [NavActions](actions_list.md) to key combos. 

This is done using an actions sub-dictionary where an Action is a key and a list of KeyCombos is the value. You cannot use `NONE` as a key. Then each KeyCombo is a `key` value of a `String` [Friendly keyname](friendly_keynames.md) or an Integer GLFW keycode/SDL3 scancode (GLFW on any Minecraft version before `26.3 Snapshot 4` and SDL3 on any version after, due to Mojang's decision to change the game's input library in that version), and a set of optional boolean modifier fields, where if excluded get treated as `true` or `false` via wildcarding: `shift`, `control`, `superCommand` (Windows Key/Super/Command depending on platform), `altOption` (Alt/Option depending on platform).

Keys can be disabled when inheritance is used by using an optional `boolean` field named `enabled` (which defaults to `true`) in key combo and setting it to `false` to disable the key.

The way wildcarding works is that the JSON deserializer expands the list<KeyCombos> where omitted values become `true` or `false` and it does it so that all combinations possible become keys in the key registry. In CMD + Delete code, a KeyCombo must have all values specified, so wildcarding expands in `2^n` KeyCombos where `n` is the number of unspecified modifiers. For example, a key combo leaving all 4 modifiers unspecified becomes `16` (`2^4`) key combo registrations in the final registry. 

Syntax Template Example (This does not actually work because it uses fake actions, keycodes, and keynames):

```json
{
  "actions": {
    "ACTION_1": [
      {
        "key": "friendly_name_1",
        "shift": false
      },
      {
        "key": "friendly_name_2"
      },
      {
        "key": 100,
        "shift": true,
        "control": false,
        "altOption": false,
        "superCommand": false
      }
    ],
    "ACTION_2": [
      {
        "key": 100,
        "shift": true,
        "control": true,
        "altOption": false,
        "superCommand": false
      },
      {
        "key": 100,
        "shift": true,
        "control": false,
        "altOption": true,
        "superCommand": false
      },
      {
        "key": 100,
        "shift": true,
        "control": true,
        "altOption": true,
        "superCommand": false
      }
    ]
  }
}
```

### Feature Flags

Feature flags are configurable optional boolean values within a `flags` top-level JSON object that can be specified by mappings. Feature flags can be added/removed/name-changed without a change to the mappings format Version. These are the available feature flags to be used by mappings:

| Feature flag name           | Default Value | Description                                                                                                                                                                                                                                                                       |
|:----------------------------|:--------------|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `overrideVanillaNavigation` | `false`       | This feature flag controls whether it is possible to produce override mode actions. The reason this is a feature flag is because override actions aren't needed if you're willing to let vanilla Minecraft handle them, but if you want to override vanilla's shortcuts, you can. |
| `crossLineSignMovement`     | `true`        | When true, this changes signs from isolating each line into being a better multi-line text editor. For example, with this true, you can go to the end of a line and press the right arrow and it will take you to the next line.                                                  |

### Strict Mode

Strict mode can be enabled by setting a top-level optional `boolean` field named `strict` to `true` and causes the deserializer to change its behavior on the following issues:

| Issue                                                                                                | Behavior Outside of Strict Mode               | Behavior in Strict Mode |
|:-----------------------------------------------------------------------------------------------------|:----------------------------------------------|:------------------------|
| A duplicate action registration after wildcarding                                                    | Logs a warning                                | Throws an exception     |
| Conflicts between different actions where a KeyCombo is registered twice after wildcarding           | Logs a warning                                | Throws an exception     |
| An unknown friendly keyname is specified                                                             | Logs a warning                                | Throws an exception     |
| An unknown action name is specified                                                                  | Logs a warning                                | Throws an exception     |
| Action names or `systems` entries have leading/trailing whitespace or have the wrong capitalization. | Removes whitespace & corrects capitialization | Throws an exception     |
| Numbers that don't adhere to the strict number RegEx but would pass `Integer.parseInt()`             | Number is accepted                            | Throws an exception     |

The strict number RegEx is `-?(0|[1-9]\d*)`.

### Inheritance

NavAction/KeyCombo pairs are inherited through an iterative `parent's enabled bindings` - `child's disabled bindings` + `child's enabled bindings` process. There is no maximum on the levels of inheritance allowed, though any cyclical inheritance is treated as an error and the parser fails. If you try to remove a binding that doesn't exist anywhere in the parent mappings, it does nothing. If you try to disable a binding in the wrong NavAction (one where it wasn't registered by the parent), the removal does nothing and may leave a conflicting key combo registered.

Feature flags inherit the parent's feature flag if the child feature flag is not specified, otherwise, they use the child's feature flag.

The entire contents of the `meta` object and the strict mode value are not inherited and entirely determined by the last child in the chain.

As of right now, builtin mappings can use inheritance but the code, but they currently do not as builtin mappings should themselves be a complete source of truth.

## Sharecode Syntax

| Segment (in order) | Description                                                                                                                                                                                                                      |
|:-------------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `CDS`              | The prefix for a `CMD + Delete share`.                                                                                                                                                                                           |
| `EV1`              | The version of the sharecode encoding format (currently `1`). This represents how the sharecodes are encoded, and is different than the `FV` (Format Version) of the mappings JSONs which represents the JSON schema version.    |
| the payload        | The `Base58` encoded (Using the Apache Commons Codec implementation which uses the Bitcoin Base58 alphabet) `GZip` compressed payload of the mappings JSON after being re-serialized without whitespace/pretty-printing by GSON. |
| the checksum       | The `CRC32` checksum of the uncompressed mappings JSON as UTF-8 bytes after being re-serialized without whitespace/pretty-printing by GSON.                                                                                      |

### Example
`builtin:mac` sharecode:
`CDS:EV1:tMeXxZVB45VieA3HvggAeo3p5zjrCbj7crBL5PbmPwdWGpgx8nz6vXnqxX48Cgc5BHZX63EYegfSTth3sTL16tud1DU6KFQKQNLmEtrhaoojTDiqtcm88t2ePt3yKWY4NnfPEUBhPqMgmoFCZwkfgVMpmKr6L2QYWmtruBniZRgBG5hxpA95W2E3pn5dv9SnDhpQenXnr2cWvyENAGmNpTzpUyktLDQoEZGJZE8CKK84G8jXsTf81f1Kxojv88pF66AeMYEqMCYCki7jafgTDi4YspYGxLvqrd9eZPGUPy1JvV588pD6MFT13sYK2m5yiJk5qGwqMg17DSbXjEUzU3dLN322AqPDP4Xvm3n1nztxo2fuCeGPpmD7poJDbHCws7RNKtVcgnpas8EhxHDUBqZPwyDWjbUwqxNfmqSvKQwYQbU3jTt527Z89g1Li4QPBto4bNFc9Fr7jWYQVdu8k7PxahZEFjPes5YrufVoNtU1ZC8VuMreMQNeBUz9MH5qN3oDbdYu7L5Jupr6XBrpKMoVzGGS4ygdcBMfG7ZZF6ruzNjTGDjHvrNDGDuJXpXfnccn9ami1xM4SipedBHugVM56FyR26MsRcddBMwmnSr4LTrGT6hofbsGuapfVJZUNfgab6WvHDAgHMXFQD9reFnfuMQ3KNtzMFzPJXSyVLfbFYDjPxv94yB5:2419707130`
