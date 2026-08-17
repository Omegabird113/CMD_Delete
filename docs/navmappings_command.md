# /navmappings Command
This is a table of the /navmnappings sub-commands and what they do:

| Command                                                                   | Description                                                                                                                           |
|:--------------------------------------------------------------------------|:--------------------------------------------------------------------------------------------------------------------------------------|
| `/navmappings info`                                                       | Displays info about the currently loaded mappings.                                                                                    |
| `/navmappings list`                                                       | Displays a list of the namespaced IDs of the currently available mappings, which you could switch to.                                 |
| `/navmappings set default`                                                | Switch to the default mappings behavior.                                                                                              |
| `/navmappings set builtin <non-namespaced id>`                            | Switch a specific OS's builtin mappings JSON.                                                                                         |
| `/navmappings set custom <non-namespaced id>`                             | Switch to one of your own custom mappings JSONs.                                                                                      |
| `/navmappings reload`                                                     | Reload the currently active mappings.                                                                                                 |
| `/navmappings export <type> file <non-namespaced id> <absolute location>` | Make a copy of a mappings JSON to another location on your system so you can copy it.                                                 |
| `/navmappings export <type> sharecode <non-namespaced id>`                | Make a sharecode copy of a set of mappings and prints it to the chat & copies it to your clipboard.                                   |
| `/navmappings import file <absolute location>`                            | Import a copy of a JSON you made from another location on your system into the custom JSONs folder for you to use as custom mappings. |
| `/navmappings import sharecode clipboard`                                 | Import a copy of a set of mappings from a sharecode you have copied to your clipboard.                                                |
| `/navmappings import sharecode chat <sharecode>`                          | Import a copy of a set of mappings from a sharecode you entered into the command.                                                     |
| `/navmappings debug aboutCmdDelete`                                       | Send the version, mod ID, and format versions for the mod.                                                                            |
| `/navmappings debug dumpActions`                                          | Dumps a comma-seperated list of all NavActions.                                                                                       |
| `/navmappings debug dumpDetailedActions`                                  | Dumps a table of all NavActions and their properties.                                                                                 |
| `/navmappings debug dumpFeatureFlags`                                     | Sends a hard-coded string explaining all the feature flags available in chat.                                                         |
| `/navmappings debug dumpRegistry`                                         | Dumps a detailed string representation of the currently used MappingsRegistry.                                                        |
| `/navmappings debug dumpMappingsState`                                    | Dumps a string interpration of the current MappingsState in chat.                                                                     |