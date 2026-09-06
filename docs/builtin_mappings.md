# Builtin Mappings

This page includes the entire JSON contents and sharecodes of every builtin mappings set.

## builtin:emacs_mac

JSON:

```json
{
	"fv": 5,
	"strict": true,
	"meta": {
		"name": "Emacs (Mac)",
		"author": "$$cmd_delete$$",
		"description": "Pre-bundled Emacs-style mappings for macOS. Note that these may not perfectly mirror Emacs's behavior.",
		"version": "$$cmd_delete$$",
		"license": "Apache-2.0 OR CC-BY-4.0",
		"credits": "The original program(s) and any contributors to CMD + Delete.",
		"id": "emacs_mac",
		"systems": [
			"mac"
		]
	},
	"actions": {
		"NAV_LINE_LEFT": [
			{
				"key": "a",
				"control": true,
				"shift": false
			}
		],
		"NAV_LINE_RIGHT": [
			{
				"key": "e",
				"control": true,
				"shift": false
			}
		],
		"NAV_WORD_LEFT": [
			{
				"key": "b",
				"altOption": true,
				"shift": false
			}
		],
		"NAV_WORD_RIGHT": [
			{
				"key": "f",
				"altOption": true,
				"shift": false
			}
		],
		"SEL_LINE_LEFT": [
			{
				"key": "a",
				"control": true,
				"shift": true
			}
		],
		"SEL_LINE_RIGHT": [
			{
				"key": "e",
				"control": true,
				"shift": true
			}
		],
		"SEL_WORD_LEFT": [
			{
				"key": "b",
				"altOption": true,
				"shift": true
			}
		],
		"SEL_WORD_RIGHT": [
			{
				"key": "f",
				"altOption": true,
				"shift": true
			}
		],
		"DEL_LINE_LEFT": [
			{
				"key": "u",
				"control": true
			}
		],
		"DEL_LINE_RIGHT": [
			{
				"key": "k",
				"control": true
			}
		],
		"DEL_WORD_LEFT": [
			{
				"key": "backspace",
				"altOption": true
			}
		],
		"DEL_WORD_RIGHT": [
			{
				"key": "d",
				"altOption": true
			}
		],
		"NAV_TEXT_START": [
			{
				"key": "comma",
				"altOption": true,
				"shift": false
			}
		],
		"NAV_TEXT_END": [
			{
				"key": "period",
				"altOption": true,
				"shift": false
			}
		],
		"SEL_TEXT_START": [
			{
				"key": "comma",
				"altOption": true,
				"shift": true
			}
		],
		"SEL_TEXT_END": [
			{
				"key": "period",
				"altOption": true,
				"shift": true
			}
		],
		"SEL_TEXT_UP": [
			{
				"key": "p",
				"control": true,
				"shift": true
			}
		],
		"SEL_TEXT_DOWN": [
			{
				"key": "n",
				"control": true,
				"shift": true
			}
		],
		"OVR_NAV_CHAR_LEFT": [
			{
				"key": "b",
				"control": true,
				"shift": false,
				"altOption": false
			},
			{
				"key": "left",
				"shift": false
			}
		],
		"OVR_NAV_CHAR_RIGHT": [
			{
				"key": "f",
				"control": true,
				"shift": false,
				"altOption": false
			},
			{
				"key": "right",
				"shift": false
			}
		],
		"OVR_SEL_CHAR_LEFT": [
			{
				"key": "b",
				"control": true,
				"shift": true,
				"altOption": false
			},
			{
				"key": "left",
				"shift": true
			}
		],
		"OVR_SEL_CHAR_RIGHT": [
			{
				"key": "f",
				"control": true,
				"shift": true,
				"altOption": false
			},
			{
				"key": "right",
				"shift": true
			}
		],
		"OVR_DEL_CHAR_LEFT": [
			{
				"key": "h",
				"control": true,
				"altOption": false
			},
			{
				"key": "backspace",
				"altOption": false,
				"shift": false
			}
		],
		"OVR_DEL_CHAR_RIGHT": [
			{
				"key": "d",
				"control": true,
				"altOption": false
			},
			{
				"key": "delete"
			}
		],
		"OVR_NAV_TEXT_UP": [
			{
				"key": "p",
				"control": true,
				"shift": false,
				"altOption": false
			},
			{
				"key": "up",
				"shift": false
			}
		],
		"OVR_NAV_TEXT_DOWN": [
			{
				"key": "n",
				"control": true,
				"shift": false,
				"altOption": false
			},
			{
				"key": "down",
				"shift": false
			}
		],
		"OVR_COPY": [
			{
				"key": "w",
				"altOption": true,
				"control": false
			}
		],
		"OVR_CUT": [
			{
				"key": "w",
				"control": true,
				"altOption": false
			}
		],
		"OVR_PASTE": [
			{
				"key": "y",
				"control": true,
				"altOption": false
			}
		],
		"OVR_SELECT_ALL": [
			{
				"key": "a",
				"superCommand": true,
				"altOption": false,
				"control": false
			}
		]
	},
	"flags": {
		"overrideVanillaNavigation": true,
		"crossLineSignMovement": true
	}
}
```

Sharecode:
`CDS:EV1:dSApxGKqukryZwu5CMwGyx7BRptTVwMUe9ohqM9UFoaA27QkF59B7m4hDpdjra97r5NDTqxkVtSMP23TrY6a7fgotB9qDm2JiTUioYaScq3e3nFxGHo9NC4HRtrZbeKqa9MGyjAvhgjCZH7TM6CC3R4h3EAuXx6uWZifWaXmjvmaBs8MTprCxDVhnAFhooEyECDLvqJRu62x5czYb9Rmi1Eu7fc2BcBniuUXP2CEuXsKXobYveowtRZEgwj8d5h3YJvFCPvpK2zUrapB7yVmzEY5v72USM769bdawRbbAbcuDPQBVUYNV1mjX7yzCnifPNLjE7rwGc4SvRuypjr4EAFYRUu42sS1hZCYqFkkEMDjZaZjMfSj5xEWV2kEKa7Wrw59GhTJPxUbHL84Z3geJrXqVFGzYE3eojhKJxRPUt1sDjuffLTkDPyMJVMz6ffHsxidkCueJsLoKpz9MWJeHFsQMKdfdcGkEzTP3n8s1Hk4FCo5NuY3ELC4tw5YKVs8pm9sMyu9qsaMDvoEa8Bwojbum5exeE2vQWJtfAqHWehtWNgZkLURtytC4CyuzTaWbZy938LupQf4upnQRTdyVncP2EtHJ6YWRkgcQPbM5MUUAJDVKwoQoupzzsjkxEQPJgUj6HcNXUUrLHcST4unaT4EFfReqxcimdzKQ3x6krHsJJRKDs8mAZSCB3tfBJgAz3zdZxn3s3y3rYansYudkVbch5axYLRZG8kiuJPGkVJ38SLTXc5mP3uVDgZR7Aph7XPzHTYWCbovWzTo1aZ9xnh97iAL9t6mB5xvNg1siKyvtEjF2GraKMXVd1Tuq2bWXCbDZyvpey68yTj3ALVJDKLDHn18PzEyp4oNLXPprAG3u7nbKydVeeV7tAt11nZr1fwEhPzzkDen8hp9bZxKHj8E6kjtaM9mGvqwz5DyURJEHxE93u:459580176`

## builtin:emacs_windows_linux

JSON:

```json
{
	"fv": 5,
	"strict": true,
	"meta": {
		"name": "Emacs (Windows/Linux)",
		"author": "$$cmd_delete$$",
		"description": "Pre-bundled Emacs-style mappings for Windows and Linux. Note that these may not perfectly mirror Emacs's behavior.",
		"version": "$$cmd_delete$$",
		"license": "Apache-2.0 OR CC-BY-4.0",
		"credits": "The original program(s) and any contributors to CMD + Delete.",
		"id": "emacs_windows_linux",
		"systems": [
			"windows",
			"linux"
		]
	},
	"actions": {
		"NAV_LINE_LEFT": [
			{
				"key": "a",
				"control": true,
				"shift": false
			}
		],
		"NAV_LINE_RIGHT": [
			{
				"key": "e",
				"control": true,
				"shift": false
			}
		],
		"NAV_WORD_LEFT": [
			{
				"key": "b",
				"altOption": true,
				"shift": false
			}
		],
		"NAV_WORD_RIGHT": [
			{
				"key": "f",
				"altOption": true,
				"shift": false
			}
		],
		"SEL_LINE_LEFT": [
			{
				"key": "a",
				"control": true,
				"shift": true
			}
		],
		"SEL_LINE_RIGHT": [
			{
				"key": "e",
				"control": true,
				"shift": true
			}
		],
		"SEL_WORD_LEFT": [
			{
				"key": "b",
				"altOption": true,
				"shift": true
			}
		],
		"SEL_WORD_RIGHT": [
			{
				"key": "f",
				"altOption": true,
				"shift": true
			}
		],
		"DEL_LINE_LEFT": [
			{
				"key": "u",
				"control": true
			}
		],
		"DEL_LINE_RIGHT": [
			{
				"key": "k",
				"control": true
			}
		],
		"DEL_WORD_LEFT": [
			{
				"key": "backspace",
				"altOption": true
			}
		],
		"DEL_WORD_RIGHT": [
			{
				"key": "d",
				"altOption": true
			}
		],
		"NAV_TEXT_START": [
			{
				"key": "comma",
				"altOption": true,
				"shift": false
			}
		],
		"NAV_TEXT_END": [
			{
				"key": "period",
				"altOption": true,
				"shift": false
			}
		],
		"SEL_TEXT_START": [
			{
				"key": "comma",
				"altOption": true,
				"shift": true
			}
		],
		"SEL_TEXT_END": [
			{
				"key": "period",
				"altOption": true,
				"shift": true
			}
		],
		"SEL_TEXT_UP": [
			{
				"key": "p",
				"control": true,
				"shift": true
			}
		],
		"SEL_TEXT_DOWN": [
			{
				"key": "n",
				"control": true,
				"shift": true
			}
		],
		"OVR_NAV_CHAR_LEFT": [
			{
				"key": "b",
				"control": true,
				"shift": false,
				"altOption": false
			},
			{
				"key": "left",
				"shift": false
			}
		],
		"OVR_NAV_CHAR_RIGHT": [
			{
				"key": "f",
				"control": true,
				"shift": false,
				"altOption": false
			},
			{
				"key": "right",
				"shift": false
			}
		],
		"OVR_SEL_CHAR_LEFT": [
			{
				"key": "b",
				"control": true,
				"shift": true,
				"altOption": false
			},
			{
				"key": "left",
				"shift": true
			}
		],
		"OVR_SEL_CHAR_RIGHT": [
			{
				"key": "f",
				"control": true,
				"shift": true,
				"altOption": false
			},
			{
				"key": "right",
				"shift": true
			}
		],
		"OVR_DEL_CHAR_LEFT": [
			{
				"key": "h",
				"control": true,
				"altOption": false
			},
			{
				"key": "backspace",
				"altOption": false
			}
		],
		"OVR_DEL_CHAR_RIGHT": [
			{
				"key": "d",
				"control": true,
				"altOption": false
			},
			{
				"key": "delete"
			}
		],
		"OVR_NAV_TEXT_UP": [
			{
				"key": "p",
				"control": true,
				"shift": false,
				"altOption": false
			},
			{
				"key": "up",
				"shift": false
			}
		],
		"OVR_NAV_TEXT_DOWN": [
			{
				"key": "n",
				"control": true,
				"shift": false,
				"altOption": false
			},
			{
				"key": "down",
				"shift": false
			}
		],
		"OVR_COPY": [
			{
				"key": "w",
				"altOption": true,
				"control": false
			}
		],
		"OVR_CUT": [
			{
				"key": "w",
				"control": true,
				"altOption": false
			}
		],
		"OVR_PASTE": [
			{
				"key": "y",
				"control": true,
				"altOption": false
			}
		]
	},
	"flags": {
		"overrideVanillaNavigation": true,
		"crossLineSignMovement": true
	}
}
```

Sharecode:
`CDS:EV1:HaS3XFfzNdU3NxCHF2zNXghSjQi4gsttApEocCcAjh41oLZyvKHH3spykzo941vdt6mhEqJZXQqyvmerp5KXihKAFwKUbLcUCgEPfXmm26mGLuuT1TAZpP1BBF2xNzHc3eqxPZVRmCrsc1VwveWgqqznciKTnKD59oW7S1T6NiFeuP6VR7ZqGCqSkufAQjExULQH9WNxE5ny29vNwjFcACwtzoJnDm9PigXC7vov5sU9fRkWXZ98D5i7aJ5mACVAPe3zji4jZoB1pUsDUPF9WB65Bab7jCoCxgvyYYUg6tQzdeQThESrPW4qar4QB2LiE7X4DAcyAd1SscFok3YuD3riMTCKGaPM4ePamBML4ueDFoTMh9nqx97XFX7wNbW9Q1VXzgKparg8ALKK2LGf5b2ukmPutS5oCZGK5wxatAGWL3TVk1uBHedhwVcoeb5ush1cdQGJhhGvDVMQChFj7G9YHv3xg9YR53qqcQNcckutZ6P4XCYUYWuzKUGUQFaxnnRy693TDLN37W8smyg6oUcFwXGs2d5MZaHgen7NGVr2LPCPAcYzA5LWqSCKedqiY8uGCiWb1a2ZUZh7hzHeVCRyBdemQ7TBeR7zHLKP6mZAXV3TnWRJ64pa2UnGbhyprYQz6ASV8TisVVhaAkkdcdYcqQNFAf3JwwuVnqi7GwXiuXT4iApLQ5HqiMyS2cMWWc189AQGgqNDz8twcDsU6gJTVxJtuuah4nNGTFMpMkv1X9BpXpEm2AJLiar5qbzJmAtgZ1UguRrSfa4tXPSeEfHi2Ge4FQoZLmRGpsKexXM1biKTEQJkpwyEKSS5RoFP3uB3zZirVPcdQmxsZYtZmukU6ifb9rJyPZZNQs6fRxvuLXKkYnXpRqJsDYF6HEwGtj7Ptcp5TRzo4QxLnWWd7oQJPmhKXLn3LwV1UWhkcP:2162629053`

## builtin:mac

JSON:

```json
{
	"fv": 5,
	"strict": true,
	"meta": {
		"name": "Mac mappings",
		"author": "$$cmd_delete$$",
		"description": "Pre-bundled mappings for macOS.",
		"version": "$$cmd_delete$$",
		"license": "Apache-2.0 OR CC-BY-4.0",
		"credits": "Any contributors to CMD + Delete.",
		"id": "mac",
		"systems": [
			"mac"
		]
	},
	"actions": {
		"NAV_TEXT_START": [
			{
				"key": "up",
				"superCommand": true,
				"altOption": false,
				"shift": false
			}
		],
		"SEL_TEXT_START": [
			{
				"key": "up",
				"superCommand": true,
				"altOption": false,
				"shift": true
			}
		],
		"NAV_TEXT_END": [
			{
				"key": "down",
				"superCommand": true,
				"altOption": false,
				"shift": false
			}
		],
		"SEL_TEXT_END": [
			{
				"key": "down",
				"superCommand": true,
				"altOption": false,
				"shift": true
			}
		],
		"NAV_LINE_LEFT": [
			{
				"key": "left",
				"superCommand": true,
				"altOption": false,
				"shift": false
			}
		],
		"SEL_LINE_LEFT": [
			{
				"key": "left",
				"superCommand": true,
				"altOption": false,
				"shift": true
			}
		],
		"NAV_LINE_RIGHT": [
			{
				"key": "right",
				"superCommand": true,
				"altOption": false,
				"shift": false
			}
		],
		"SEL_LINE_RIGHT": [
			{
				"key": "right",
				"superCommand": true,
				"altOption": false,
				"shift": true
			}
		],
		"NAV_WORD_LEFT": [
			{
				"key": "left",
				"superCommand": false,
				"altOption": true,
				"shift": false
			}
		],
		"SEL_WORD_LEFT": [
			{
				"key": "left",
				"superCommand": false,
				"altOption": true,
				"shift": true
			}
		],
		"NAV_WORD_RIGHT": [
			{
				"key": "right",
				"superCommand": false,
				"altOption": true,
				"shift": false
			}
		],
		"SEL_WORD_RIGHT": [
			{
				"key": "right",
				"superCommand": false,
				"altOption": true,
				"shift": true
			}
		],
		"DEL_LINE_LEFT": [
			{
				"key": "backspace",
				"superCommand": true,
				"altOption": false
			}
		],
		"DEL_LINE_RIGHT": [
			{
				"key": "delete",
				"superCommand": true,
				"altOption": false
			}
		],
		"DEL_WORD_LEFT": [
			{
				"key": "backspace",
				"superCommand": false,
				"altOption": true
			}
		],
		"DEL_WORD_RIGHT": [
			{
				"key": "delete",
				"superCommand": false,
				"altOption": true
			}
		],
		"SEL_TEXT_UP": [
			{
				"key": "up",
				"superCommand": false,
				"altOption": false,
				"shift": true
			}
		],
		"SEL_TEXT_DOWN": [
			{
				"key": "down",
				"superCommand": false,
				"altOption": false,
				"shift": true
			}
		],
		"OVR_NAV_CHAR_LEFT": [
			{
				"key": "left",
				"superCommand": false,
				"altOption": false,
				"shift": false
			}
		],
		"OVR_NAV_CHAR_RIGHT": [
			{
				"key": "right",
				"superCommand": false,
				"altOption": false,
				"shift": false
			}
		],
		"OVR_SEL_CHAR_LEFT": [
			{
				"key": "left",
				"superCommand": false,
				"altOption": false,
				"shift": true
			}
		],
		"OVR_SEL_CHAR_RIGHT": [
			{
				"key": "right",
				"superCommand": false,
				"altOption": false,
				"shift": true
			}
		],
		"OVR_DEL_CHAR_LEFT": [
			{
				"key": "backspace",
				"superCommand": false,
				"altOption": false,
				"shift": false
			}
		],
		"OVR_DEL_CHAR_RIGHT": [
			{
				"key": "delete",
				"superCommand": false,
				"altOption": false,
				"shift": false
			}
		],
		"OVR_NAV_TEXT_UP": [
			{
				"key": "up",
				"superCommand": false,
				"altOption": false,
				"shift": false
			}
		],
		"OVR_NAV_TEXT_DOWN": [
			{
				"key": "down",
				"superCommand": false,
				"altOption": false,
				"shift": false
			}
		],
		"OVR_COPY": [
			{
				"key": "c",
				"superCommand": true
			}
		],
		"OVR_CUT": [
			{
				"key": "x",
				"superCommand": true
			}
		],
		"OVR_PASTE": [
			{
				"key": "v",
				"superCommand": true
			}
		],
		"OVR_SELECT_ALL": [
			{
				"key": "a",
				"superCommand": true
			}
		]
	},
	"flags": {
		"overrideVanillaNavigation": true,
		"crossLineSignMovement": true
	}
}
```

Sharecode:
`CDS:EV1:44JKjTG574eHakymgxNGJJeCrU7qET3qcqCUJQhj7UMzBzAoyc9tDXLLstz2FQyZXVLcPPN9KaYkNp3NQCX4yhyRRrLsgYEQFzakWDyGXoGm1RUFkwA53a8Zro4mZUptGYHgZYCewY1Fu9dMJR23woVRJRDRJbVSL8pwzoLm6NaavFcvN3MFPSjCVktNoU9bZJusvvAJZyS9R9PW7HTRvePD2XBcodT6hJT5wWhZ4fCUe6omugZ7XH6q4tzqZaEp4kwLboA9owdDAqaAHKsL3tSnNNHkFx2KN6w8Lw5WHzseuZLR4m5cKeABMsEtbgjQWFFbR1yiCjVq3mRgw3yNMkeKbzsgwgzrTvPZY4xSNrcuTSyYsy91AjgaXydsihEzsMMfHfH8BXyxdht681vMb9GMgq8e2L8az1rnGyGxboLacKoM96TTWamdUDKZnS4dpWNHEYArjAwqfddzGG5VCU8B4WYx6wQgHXjF4hcWV75Xi2wHppwgZh2tjgDQb1RyEnT6itwv3H8R4SQeCz22ZEaYc7so3nDUZfDnpHScSUAqk5Gd6mrRo4JgibzNqn32xZiXEHZ9QjnBPpXDqbLo6kP93Zc6yC8tG7ahFCHYGyPjrryh2nxVH2ZvY93v1WCtzVhdk6nH5TedtUwfRRy3tjqXQKWXZea7cyuUZPE8dWpRVLy9TqQvMmNLisr7Q9gpkkz64QeqSXrryjh5ptc5sXnzboTgTUbeDhRwCp9r3Azyjs95V5GwYZucYJ29m7CHs1wNAdZEWZWP:3421744520`

## builtin:readline

JSON:

```json
{
	"fv": 5,
	"strict": true,
	"meta": {
		"name": "GNU Readline",
		"author": "$$cmd_delete$$",
		"description": "Pre-bundled GNU Readline-style mappings. Note that these may not perfectly mirror Readline's behavior.",
		"version": "$$cmd_delete$$",
		"license": "Apache-2.0 OR CC-BY-4.0",
		"credits": "The original program(s) and any contributors to CMD + Delete.",
		"id": "readline",
		"systems": [
			"mac",
			"windows",
			"linux"
		]
	},
	"actions": {
		"NAV_LINE_LEFT": [
			{
				"key": "a",
				"control": true,
				"shift": false
			}
		],
		"NAV_LINE_RIGHT": [
			{
				"key": "e",
				"control": true,
				"shift": false
			}
		],
		"SEL_LINE_LEFT": [
			{
				"key": "a",
				"control": true,
				"shift": true
			}
		],
		"SEL_LINE_RIGHT": [
			{
				"key": "e",
				"control": true,
				"shift": true
			}
		],
		"NAV_WORD_LEFT": [
			{
				"key": "b",
				"altOption": true,
				"shift": false
			}
		],
		"NAV_WORD_RIGHT": [
			{
				"key": "f",
				"altOption": true,
				"shift": false
			}
		],
		"SEL_WORD_LEFT": [
			{
				"key": "b",
				"altOption": true,
				"shift": true
			}
		],
		"SEL_WORD_RIGHT": [
			{
				"key": "f",
				"altOption": true,
				"shift": true
			}
		],
		"DEL_LINE_LEFT": [
			{
				"key": "u",
				"control": true
			}
		],
		"DEL_LINE_RIGHT": [
			{
				"key": "k",
				"control": true
			}
		],
		"DEL_WORD_LEFT": [
			{
				"key": "w",
				"control": true
			},
			{
				"key": "backspace",
				"altOption": true
			}
		],
		"DEL_WORD_RIGHT": [
			{
				"key": "d",
				"altOption": true
			}
		],
		"SEL_TEXT_UP": [
			{
				"key": "p",
				"control": true,
				"shift": true
			}
		],
		"SEL_TEXT_DOWN": [
			{
				"key": "n",
				"control": true,
				"shift": true
			}
		],
		"OVR_NAV_CHAR_LEFT": [
			{
				"key": "b",
				"control": true,
				"shift": false,
				"altOption": false
			},
			{
				"key": "left",
				"shift": false
			}
		],
		"OVR_NAV_CHAR_RIGHT": [
			{
				"key": "f",
				"control": true,
				"shift": false,
				"altOption": false
			},
			{
				"key": "right",
				"shift": false
			}
		],
		"OVR_SEL_CHAR_LEFT": [
			{
				"key": "b",
				"control": true,
				"shift": true,
				"altOption": false
			},
			{
				"key": "left",
				"shift": true
			}
		],
		"OVR_SEL_CHAR_RIGHT": [
			{
				"key": "f",
				"control": true,
				"shift": true,
				"altOption": false
			},
			{
				"key": "right",
				"shift": true
			}
		],
		"OVR_DEL_CHAR_LEFT": [
			{
				"key": "h",
				"control": true,
				"altOption": false
			},
			{
				"key": "backspace",
				"altOption": false
			}
		],
		"OVR_DEL_CHAR_RIGHT": [
			{
				"key": "d",
				"control": true,
				"altOption": false
			},
			{
				"key": "delete"
			}
		],
		"OVR_NAV_TEXT_UP": [
			{
				"key": "p",
				"control": true,
				"shift": false,
				"altOption": false
			},
			{
				"key": "up",
				"shift": false
			}
		],
		"OVR_NAV_TEXT_DOWN": [
			{
				"key": "n",
				"control": true,
				"shift": false,
				"altOption": false
			},
			{
				"key": "down",
				"shift": false
			}
		],
		"OVR_PASTE": [
			{
				"key": "y",
				"control": true,
				"altOption": false
			}
		]
	},
	"flags": {
		"overrideVanillaNavigation": true,
		"crossLineSignMovement": true
	}
}
```

Sharecode:
`CDS:EV1:266VBbBeNmAyPms1rPTbJUpCQRE4Lwb5HK61vgpSEYsoSgmPYNM2NS8ua1QrL43bfFVz2i5JQxSQUvKQg6dYNMxJz5njM1BVafjjf6t89kfjkgS3AwTGqeiRUA3VTtyBmULkCw65ceRY8qe7hGsXALitnTCq2img6Q76As59gTGafrUaUXjQor1ZcFh7sPK3PSbCb484B4ceJsdCyGBpRX5itAefuUJBTzhYsMntTm4Ma3Cb6BtD7MeDJGH11SFgCK5VT3jWnqGj3V4D4xD1GoAeAWuWeieWqL4mioCtLdSkMf1jgcRpBFtRKxL4CQ5CxxTAx2wE8CBvdQ2mHa5c6837aDyZTBKLasrXXM3pGwnXvXHM7EW29aCHpcf8i4S4vk6AZUZhcD2KJ3i7ixb44qjFkVM5wgr4avmm3EE3ASr8K5C8cP2bQBD8psxRocCM38Gqj99Pxud9XWoMmsLgZh2SvUXXRVMAmzwqB3iwPT4vhB72sEcrVbkYpTdPzvRrn8ikGdQVZP6uSMZa8R6qmPPTRqgzkaYVBMNxRzCsv4gn7yYytTaKa6HgHcrPRNRffdomWARzMJsqLTLQrehmEZ6ktqn9hWt2Cv7HaPdAfyXhaSYCWQdGES9qjHnxeGyBSKZ63u1vX4qKUHUdv7ebFNz9opGbBzXLAMCgKNtKoDgAHhFPRRT4e6sktkAQo26BviSLTfb6qX2cThVqmEYddFij3s7XQKmiGPktnLMmpbgpiAjVZMDpKkELRZeHTvii6onwRecJAdDAFzDW9q6JaBzwHPN37p5wBAJuJaEE3jPbppyVsoJsVgJBLxdHBt6rtrdE6SLRrTPScyzRGZHKK5:1703275242`

## builtin:windows_linux

JSON:

```json
{
	"fv": 5,
	"strict": true,
	"meta": {
		"name": "Windows/Linux mappings",
		"author": "$$cmd_delete$$",
		"description": "Pre-bundled mappings for Windows and Linux.",
		"version": "$$cmd_delete$$",
		"id": "windows_linux",
		"systems": [
			"windows",
			"linux"
		]
	},
	"actions": {
		"NAV_TEXT_START": [
			{
				"key": "home",
				"control": true,
				"shift": false
			}
		],
		"SEL_TEXT_START": [
			{
				"key": "home",
				"control": true,
				"shift": true
			}
		],
		"NAV_TEXT_END": [
			{
				"key": "end",
				"control": true,
				"shift": false
			}
		],
		"SEL_TEXT_END": [
			{
				"key": "end",
				"control": true,
				"shift": true
			}
		],
		"NAV_LINE_LEFT": [
			{
				"key": "home",
				"control": false,
				"shift": false
			}
		],
		"SEL_LINE_LEFT": [
			{
				"key": "home",
				"control": false,
				"shift": true
			}
		],
		"NAV_LINE_RIGHT": [
			{
				"key": "end",
				"control": false,
				"shift": false
			}
		],
		"SEL_LINE_RIGHT": [
			{
				"key": "end",
				"control": false,
				"shift": true
			}
		],
		"NAV_WORD_LEFT": [
			{
				"key": "left",
				"control": true,
				"shift": false
			}
		],
		"SEL_WORD_LEFT": [
			{
				"key": "left",
				"control": true,
				"shift": true
			}
		],
		"NAV_WORD_RIGHT": [
			{
				"key": "right",
				"control": true,
				"shift": false
			}
		],
		"SEL_WORD_RIGHT": [
			{
				"key": "right",
				"control": true,
				"shift": true
			}
		],
		"DEL_WORD_LEFT": [
			{
				"key": "backspace",
				"control": true
			}
		],
		"DEL_WORD_RIGHT": [
			{
				"key": "delete",
				"control": true
			}
		],
		"SEL_TEXT_UP": [
			{
				"key": "up",
				"shift": true
			}
		],
		"SEL_TEXT_DOWN": [
			{
				"key": "down",
				"shift": true
			}
		],
		"OVR_NAV_CHAR_LEFT": [
			{
				"key": "left",
				"control": false,
				"shift": false
			}
		],
		"OVR_NAV_CHAR_RIGHT": [
			{
				"key": "right",
				"control": false,
				"shift": false
			}
		],
		"OVR_SEL_CHAR_LEFT": [
			{
				"key": "left",
				"control": false,
				"shift": true
			}
		],
		"OVR_SEL_CHAR_RIGHT": [
			{
				"key": "right",
				"control": false,
				"shift": true
			}
		],
		"OVR_DEL_CHAR_LEFT": [
			{
				"key": "backspace",
				"control": false,
				"shift": false
			}
		],
		"OVR_DEL_CHAR_RIGHT": [
			{
				"key": "delete",
				"control": false,
				"shift": false
			}
		],
		"OVR_NAV_TEXT_UP": [
			{
				"key": "up",
				"control": false,
				"shift": false
			}
		],
		"OVR_NAV_TEXT_DOWN": [
			{
				"key": "down",
				"control": false,
				"shift": false
			}
		],
		"OVR_COPY": [
			{
				"key": "c",
				"control": true
			}
		],
		"OVR_CUT": [
			{
				"key": "x",
				"control": true
			}
		],
		"OVR_PASTE": [
			{
				"key": "v",
				"control": true
			}
		],
		"OVR_SELECT_ALL": [
			{
				"key": "a",
				"control": true
			}
		]
	},
	"flags": {
		"overrideVanillaNavigation": true,
		"crossLineSignMovement": true
	}
}
```

Sharecode:
`CDS:EV1:HhUEP3RV3ZNF57q9CSuvXzWm4o4ro4Nc9PFxdwQ7ykXecw7LoSYCdSHVbg6zWJCUapfZze6igszYTCTa8rXZ8RwNtRmK2pQ118pLEdJzJzRLzHHbsWS3fE4akKawB3uFrxg6yMFYm7Kgmkqa9V2hGx3Hk68ZExARozSHhTysSTGBnkrrXgtJ3xQZecBAaLfhgC8Yvaz9GpAEq2Q1SxX1bHkG8PnKPX4i9vxxGpo9TU6PuSjP6zYSUqA14GQDhWqhyV3Za5MPAzUMRgqdpyjMn4mwdV3hirH1UCr4eJcZYi49VJBYrFindM8pZyeNjQKSp84TLdJmRzsju9AR9M5hvXvyEv8Vf3trkC2gCAC4HGvdQk2ZigmipFU8uQDgdXhfMp6HbzBPAUqQRU9WgWvYjoe3VhRS51mxejW7uKiCihChqyuasbovju9uDu5CoLbhPUieD5YhkMTfnV7bKMFPiex54nAXVCwFuMLzQmzkfos1Vxsnncg99Q1yt6GWBERorFsPBBNjty6ffkjGecCFLe1EZqXBGAMYEUo2gsrU7v7m3vQSRrvwPtdb4qEkWtawjwyX3WjCxkJQB6ZHVpKdmJh6TYd2xm12bz2GA66trhN7XhqqsGhCYyDZ1S5UEwHLSmx85db6W3oDo5XNrL4XwN2bHUnMjK9ATRYLHRZJhFu4Us2gcLcFYSvvvk98kakFTVvGvPdFnCScGwbT6zqsQtnSZKQSPP1FYtD7RTLryvKfvVq8hM1:443250386`
