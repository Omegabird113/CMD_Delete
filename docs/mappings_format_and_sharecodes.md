# The Mappings Format & Sharecodes

ou can define your own custom mappings in configuration files with the location & name: `<minecraft install>/config/cmd_delete/mappings/<id>.json`. These allow you to customize the text navigation shortcut behavior in Minecraft for your own taste & configuration. You can also easily share your mappings via sharecodes!

### Mappings Format Features

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

#### Example

`sample.json` file:
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

### Sharecode Syntax

| Segment (in order) | Description                                                          |
|:-------------------|:---------------------------------------------------------------------|
| `CDS`              | The prefix for a `CMD + Delete share`.                               |
| `EV1`              | The version of the sharecode encoding format (currently `1`).        |
| the payload        | The `Base58` encoded `GZip` compressed payload of the mappings JSON. |
| the checksum       | The `CRC32` checksum of the uncompressed mappings JSON.              |

#### Example
`builtin:mac` sharecode:
`CDS:EV1:tMeXxZVB45VieA3HvggAeo3p5zjrCbj7crBL5PbmPwdWGpgx8nz6vXnqxX48Cgc5BHZX63EYegfSTth3sTL16tud1DU6KFQKQNLmEtrhaoojTDiqtcm88t2ePt3yKWY4NnfPEUBhPqMgmoFCZwkfgVMpmKr6L2QYWmtruBniZRgBG5hxpA95W2E3pn5dv9SnDhpQenXnr2cWvyENAGmNpTzpUyktLDQoEZGJZE8CKK84G8jXsTf81f1Kxojv88pF66AeMYEqMCYCki7jafgTDi4YspYGxLvqrd9eZPGUPy1JvV588pD6MFT13sYK2m5yiJk5qGwqMg17DSbXjEUzU3dLN322AqPDP4Xvm3n1nztxo2fuCeGPpmD7poJDbHCws7RNKtVcgnpas8EhxHDUBqZPwyDWjbUwqxNfmqSvKQwYQbU3jTt527Z89g1Li4QPBto4bNFc9Fr7jWYQVdu8k7PxahZEFjPes5YrufVoNtU1ZC8VuMreMQNeBUz9MH5qN3oDbdYu7L5Jupr6XBrpKMoVzGGS4ygdcBMfG7ZZF6ruzNjTGDjHvrNDGDuJXpXfnccn9ami1xM4SipedBHugVM56FyR26MsRcddBMwmnSr4LTrGT6hofbsGuapfVJZUNfgab6WvHDAgHMXFQD9reFnfuMQ3KNtzMFzPJXSyVLfbFYDjPxv94yB5:2419707130`