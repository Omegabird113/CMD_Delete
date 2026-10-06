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
		"crossLineSignMovement": true,
		"nativeStyleWordBoundaries": true
	}
}
```

Sharecode:
`CDS:EV1:3Ewe9ax6jjtHWpBWXzPmmn8MLcnfBRdz1kPQLRiPqi8EMUe134FdSjToDFbMNqPiUpVvzeNxaKXdEKmwCFjKNGK95GTFNXnm6tqaLKRPq5UKgcUZDkA63ztspYimtJYCnhxjBz1azK2PjXu6heqwnGhp9zp5aHLfD2ihyY7KXW9QjV1DMNRSYQwbXhQrSQghB6h3Uj9BLMJKEZ1kzLJ16dtdAA8TUPEapeqn2GREZMdaNvzT4Y2njBhkKYpmoU4ebLRzJg6k5m8UqshREeBiZaz4hyta8SWUqkVRvePrD9L72byf3qAT1YVhJyQA1FUpSitJjhs9Rm3yPQFuAGNRubnQ81E3jE3GmzLeDxdrLbwpH9PACukvPTUUa3ketJ8ngjuXDLgJc55dkUjVPYeC9fTMj4akH19Qtpj6v5CHUZbSYJumrwjQ8jR47nHPt3vn27XHT5Ge6RYYErRkkFS6hsPoN91hni4ttaWiQ7mZ45HfPYMuxK9jYkPF5cjmsjjFzmiiaao3qykowf6MMPb3j4j8UTFd3HJAB5gMp8K4SSvq5r2SbewwBTxvLwj3Ua2khfJedLiCV4B9APLva4fNzHv98upaAhA5e5XY2NqTDutLepuDS2f9eczvEs4GmECjsNZG5Wdesp8ePyEMYpuYSWbDMA2mJtyFfy18CBD5SHneSm4jaifLsqu4wQ1hrikSW8RyDUahaEfNYfPbK14cYxGeXVbGobmXqU3xsMeKu1S1kM3RwNuN7oknoWtq4y7ocMoPWKet1pcyhUNgu2LSX79u6USzUchSVHjHLFzCb3WWnLp3EYPJv7uyC6jawgJmjW7n5SStNpAmeyZgNS5ojCpiqXgxWu8sohah3X5jv7m4DVq2TpcMgsCU7brENt58fuWvoLPURj4LZyhzrmgXJXkhCbVzA2UqV79KC6Hgz1Px3KXmrbxG9sWBLUidJeXptdGgyFPYxjr5PD:311671979`

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
		"crossLineSignMovement": true,
		"nativeStyleWordBoundaries": true
	}
}
```

Sharecode:
`CDS:EV1:EPsdWLij7Cxsk7cmezkrf3WYowGxCapvRTv8NmYKsX6CKwYLqQsBE73gtETxKfd3gVWKTQrxQz9zcx4ssKbpL33dXGzNEDCrb9evR6j17upqioPPCMaAynLWVE5hjJL4N6UYxvwoqbxYQ5kKXb3iv5PznkgBQXjCXxbV3pwdjrQ3E9RbgKcRQdtSPnR7ToxNQ71ELLg9LxduBrZggvqAry5zQ3uDRs3J8Ug3LHLMBHgePeFkF9XvZHKSaFdjwYHgmUrm8ZLsBvrXoF7jDsLtzserTGrQiRtHuafnFmTWLkbfDiLMvQYbaTPbjhfpFVuTvBUzzYcbA99XdqkRMyGeLBqzFFuaCYGivwwoCedGR32Wj3CnVzMwdWQfwAYJXBjmQcBa5u1mFespABtCBdgWrtpJLgSqyLzsoJJnH39pwRPpmCbAjXepjyujqMJmJWLuiiWs8ET1Sv9aQsDYgzyzW21Cv3CHhno9LgaTTQjTc72ZZMVYV5yCYmei48JYKWUbZQNx2M3xgQL7smKsbjyxi7ThiHYVXYtidjy3AGVZf8majVHzRjVMrLneK5S7F1o73SKivCP7ANKiE3Zij3w2Bgxki1jDDikSfKhnY9h3kVW5MTRwCjy7hRNJs5m9MXuVCTjU1EmCsoXEDJ8uyXJAEFugodbcqPuEKKNMuHWMsBLW8Xoh1Byuj3ydr5TQkF65gmP1aeRXPyYD6y3j8XiAVaBmNQb5PsVceLLaN7xnUFX1pSza3zYaKBquesfw2yNrqp1hqk2zGJPgc2E2QQBmMEQipmKsHxnyMuX4KtprqXhonMwF7DWq1kmBGJmWNtzVhu5JTUCSDDfh7bJiTWh5tSHrTzHrDtt2iUo48gPEMPjnMQQtjP7zLrcfNgr2ev1DVcnc6ZQvmy2f8yKHQN5PYeSMhRQnrQdKdFzTEcSMRB33eZau3QVxojoVkjdYRme2BfJF:2769662961`

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
		"crossLineSignMovement": true,
		"nativeStyleWordBoundaries": true
	}
}
```

Sharecode:
`CDS:EV1:3UGK1GyFWsodDsztQa3ykFDDL2yT8xgaAPjLxohNtxVCcGCeVV6nanhjz1uU1SzyXMcZ1qNEdMzZ7ZYFtYwGqPe27FeUWXbuHHQGibgZHqkpa9EzihPnaKr6atWPW1TkpspyYZdsW2HMWrSwRd6dVpB5RTb2uAKDgyL5w4f6ncZRwGvwnpHAr4k6BdXZiZrAvF6DqrmLeQ1d2Q2CnfpkU1BVmJNFyarC63wYYoAMRSkxh3z3pxMacamDdcbzdLfDVyoTN7iS3JYxHXYPjJAvsYZgYh4LK9v6ejzUNdWRnS8R4kfUA5vZJJuANKUvWKjRyP1jJ4rSoemLZA1qN9cSXNtYHJRxR9zMf2AbH4VcrGAgAHjavM9CGKDSC1PHWGLyVSzwtLBKjeovTcKqGJgNCNPxjmgGTbWqxdX1bEcWy3m6ErLwf1oVsD4JeXNVR84QM9MHtrVHzNE5AABvCusmjyY1b6b1c8aDLSFqE2vGDMffnVya4yR6bigcj7kPhJfUr8p7n4HRZqWgfs8n1xJq7AGBFcYzP4znbaUU8yktpmZzfWZjG3RrghVawa9131nZ5MaM1TCikLNsZqHhKqyRwvuDJeD6UXbnUrjkPxYcMNSg8HtRF9ZUpnhuQe9Yk2auXUw1mEDbMZTfSxoBM5iNkxBppDzXddQmnh13vXDn2CuXUBR2wexQrJdtWpcPxmuad6RZh57ru6oqMce5BbhELtzQe8hy2mcXB29JaXeGVcwQLUAz3DoZ7fLpidsPT9583LTomFwyBoLvcHNZnbA1xeXbD7HHhcQAiWJSxo:4099694340`

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
		"crossLineSignMovement": true,
		"nativeStyleWordBoundaries": true
	}
}
```

Sharecode:
`CDS:EV1:J8E36Dd6CnSjHaYZLzxAmo4kC8E3aC17DKV1m86LtMTxAce1wyGQupntQEUcJ7cXMx2yZybCcDq8NFkd44fWAmuzPnroNcfeWA4F6WFaxL2aUBZzSqLpVweKmFs4KEZmn84CeV6xdFrZZg4qVEGm6FujdTa5vr6Z2tessgVyDyaZPMdfW3ELMeKuCVHrBuja6BurVuqVk28shAzdxDd1oYwkLJDGPRKfm292VZd4iYGfmMkkeSAwnzHKeb33oXZmgRGm4AGdADTLqAYxYKyNLNCRX6QqWhqgTsp5wjQXzfdY3ifG1TFMDALNj6UJgRUN8QxvYUsTdpFLcAbphN1uMnLFhXA3G9QreFZdigb7AeKAdcp8TbUa7LCWWYxV3JWgEPmYs8TgJMQrktynL4EPXzMnSviLFwp4nDN8oYWUbfSdRdEkuxtDGEiLZjFVBgB3EZ9ppf6jGnk1QNKDF3TMnDVtq1dRJgVt4U8gaWbwqZu62yG3VyNMc2uuzRKZiz3M8PiczpYazXAK4AtXDAX3Yjfy8chBag3hXbaCsbJd7uWbYiYJ28NMpyAfMjLBGdwa7c26btAmcjUzfY9P6Ek1fLgBXruJUmyScTBenjeypeVtV8rHD487X1uhDVwsEL2nHRnZYhXmNpE813K6GcdawqAZQwktNgpQ88VKbsdVW4STyL85BCupwFvDudwSGYZZoihARFnGoFQoo8D1xo4VqhgJnt9YhyTvevjRDurXMtUmz81prBMFNpDhwD61hAb6WXYpY8RRkBG2uiG1b79ahCdoHwx3phvabFadia1AZn98VWTpPVwgU2X5Vo94Sh6xqNAYyJBDyBCC2C2x31JKKWgcWGNisze98A2n9DJWLQBnrmdPtT:1286983502`

## builtin:vanilla

JSON:

```json
{
  "fv": 5,
  "strict": true,
  "meta": {
    "name": "Vanilla mappings",
    "author": "$$cmd_delete$$",
    "description": "Pre-bundled mappings for vanilla behavior.",
    "version": "$$cmd_delete$$",
    "license": "Apache-2.0 OR CC-BY-4.0",
    "credits": "Any contributors to CMD + Delete.",
    "id": "vanilla",
    "systems": [
      "windows",
      "mac",
      "linux"
    ]
  },
  "actions": {},
  "flags": {
    "overrideVanillaNavigation": false,
    "crossLineSignMovement": false,
    "nativeStyleWordBoundaries": false
  }
}
```

Sharecode:
`CDS:EV1:UVb7RFoDev3PoeGwnFJiyQgyiQwdUXGKLGpa4VTAhzHgdc6QAzYWydToYDxdZ9Prm1bjUWExRWVFMqrEx55HJYdiTwJDB2Z8ZgME1JzY2pwurtZxCBgHKP37KmyF1cuV6NCJCF7RjCBdkfKe2aMfb91x96wRXPAhi8EVeTsnxQJ29EvnTf57LximrS4qeDxcLTuQi9KJZxoykKN3W9LtRkSxCjzJQTvJPc5NGwBFrzZsHNz3YAwjW18fo8uk8HGZ5BEw7yFsprjpetGBiZsyLY5oZdmzkKd7YkxnjsHZ5zxvJ42if3grb5yAgtAvHv8KJYeL5SuEeEecqFC6jUfi82nQqqdEtupuysF6BDMsh7Jd3ehTw1BrRC3ogcHAekBx1cXdZ3Wecr72GvsLP11HTqUK:166085064`

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
		"license": "Apache-2.0 OR CC-BY-4.0",
		"credits": "Any contributors to CMD + Delete.",
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
		"crossLineSignMovement": true,
		"nativeStyleWordBoundaries": true
	}
}
```

Sharecode:
`CDS:EV1:5XrVSYM4i8dK3Ju2rD9VWJkCXTERcdv5JLuS9Ed8YoZcXQgXBf2ivsLshdWAwLkgj4DNxbyChRYe653bRVSwM2bTFYujVwqpkCmyqm5R1ZKdWRX3cccjzxvwriwok7kLFWjfWtc97XnZX4SmhC4qaUsTDDSGCXDxRnyyVSnHPTaU8ZBJiaL3Rw8WBMZWPzct1g9NK8QfDsZzS1xLm9THDLFmFuqDry5H9DBv28uooKqgxPXttDThK6kbByUcsEXunrzTc45uKMj8yZUnwqHbCpzKyBceHMTuPkM9SiqbifcMH8K8J9KefdP3X5YuP4ejhgbiNXAi8C3Uq5YAVyX6PQi76UZPR8kbbnHTE13dmq2FGVssSf1EnuQArksTiuysjyEqSReRYTB5DP4xsDNSdeHd6yVqpeNt8tyNuR8ScxLJkv2FBsgbzUPgMZxKDrJL3GMjxA5NWiKU9p39ozYhLFa3UZRBjjyj3jMNVkF6SrCLmUVaJ8zwDMD4zfThMWAUZYooL25mi7ChqvhVGDj2pZDem932HpFyqr9R3BLZPLJUAEzmgtmbyY5E8ZswuA1eHx9ooKddtUfY3rbYd8EAnThAwuKDhYjRrtc1U5etAKJuzmgAstgJSx1Z8yQuw2vWHpDueARLn3hyL1AAttbEhkWeNNYEn9CrjzQbqawGST3zcYhB5qvF3Sk3gLvXFrekir8oiurY3zm2Qyhmi8n5Tp9ymZt5jqRK51dJYCViB88ntnmnJ7u6ikYVnuXKBGscFPMJa7egeRFsTUST:3071942624`
