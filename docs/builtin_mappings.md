# Builtin Mappings

This page includes the entire JSON contents and sharecodes of every builtin mappings set.

## builtin:emacs_mac

JSON:
```json
{
  "fv": 4,
  "strict": true,
  "meta": {
    "name": "Emacs (Mac)",
    "author": "$$cmd_delete$$",
    "description": "Pre-bundled Emacs-style mappings for macOS. Note that these may not perfectly mirror Emacs's behavior.",
    "version": "$$cmd_delete$$",
    "id": "emacs_mac",
    "systems": [
      "mac"
    ]
  },
  "actions": {
    "NAV_LINE_LEFT": [
      {"key": "a", "control": true, "shift": false}
    ],
    "NAV_LINE_RIGHT": [
      {"key": "e", "control": true, "shift": false}
    ],
    "NAV_WORD_LEFT": [
      {"key": "b", "altOption": true, "shift": false}
    ],
    "NAV_WORD_RIGHT": [
      {"key": "f", "altOption": true, "shift": false}
    ],
    "SEL_LINE_LEFT": [
      {"key": "a", "control": true, "shift": true}
    ],
    "SEL_LINE_RIGHT": [
      {"key": "e", "control": true, "shift": true}
    ],
    "SEL_WORD_LEFT": [
      {"key": "b", "altOption": true, "shift": true}
    ],
    "SEL_WORD_RIGHT": [
      {"key": "f", "altOption": true, "shift": true}
    ],
    "DEL_LINE_LEFT": [
      {"key": "u", "control": true}
    ],
    "DEL_LINE_RIGHT": [
      {"key": "k", "control": true}
    ],
    "DEL_WORD_LEFT": [
      {"key": "backspace", "altOption": true}
    ],
    "DEL_WORD_RIGHT": [
      {"key": "d", "altOption": true}
    ],
    "NAV_TEXT_START": [
      {"key": "comma", "altOption": true, "shift": false}
    ],
    "NAV_TEXT_END": [
      {"key": "period", "altOption": true, "shift": false}
    ],
    "SEL_TEXT_START": [
      {"key": "comma", "altOption": true, "shift": true}
    ],
    "SEL_TEXT_END": [
      {"key": "period", "altOption": true, "shift": true}
    ],
    "SEL_TEXT_UP": [
      {"key": "p", "control": true, "shift": true}
    ],
    "SEL_TEXT_DOWN": [
      {"key": "n", "control": true, "shift": true}
    ],
    "OVR_NAV_CHAR_LEFT": [
      {"key": "b", "control": true, "shift": false, "altOption": false},
      {"key": "left", "shift": false}
    ],
    "OVR_NAV_CHAR_RIGHT": [
      {"key": "f", "control": true, "shift": false, "altOption": false},
      {"key": "right", "shift": false}
    ],
    "OVR_SEL_CHAR_LEFT": [
      {"key": "b", "control": true, "shift": true, "altOption": false},
      {"key": "left", "shift": true}
    ],
    "OVR_SEL_CHAR_RIGHT": [
      {"key": "f", "control": true, "shift": true, "altOption": false},
      {"key": "right", "shift": true}
    ],
    "OVR_DEL_CHAR_LEFT": [
      {"key": "h", "control": true, "altOption": false},
      {"key": "backspace", "altOption": false, "shift": false}
    ],
    "OVR_DEL_CHAR_RIGHT": [
      {"key": "d", "control": true, "altOption": false},
      {"key": "delete"}
    ],
    "OVR_NAV_TEXT_UP": [
      {"key": "p", "control": true, "shift": false, "altOption": false},
      {"key": "up", "shift": false}
    ],
    "OVR_NAV_TEXT_DOWN": [
      {"key": "n", "control": true, "shift": false, "altOption": false},
      {"key": "down", "shift": false}
    ],
    "OVR_COPY": [
      {"key": "w", "altOption": true, "control": false}
    ],
    "OVR_CUT": [
      {"key": "w", "control": true, "altOption": false}
    ],
    "OVR_PASTE": [
      {"key": "y", "control": true, "altOption": false}
    ],
    "OVR_SELECT_ALL": [
      {"key": "a", "superCommand": true, "altOption": false, "control": false}
    ]
  },
  "flags": {
    "overrideVanillaNavigation": true,
    "crossLineSignMovement": true
  }
}
```

Sharecode:
`CDS:EV1:LN3V2q115TUzfYUHNaCpyKJN6rHnYuTPyx2mbCCJfPUksuYpSjLjA4Az5ptnWxyVF4WaW1NY9KXZi9S5H1PorDCBqkYR3mX4bEcQNQBBzpkio5BZ8Yw5wcYhwztrhgpcZBJ98P42pJdUY4ujtX6zcF4XxAkh4ijc2Lz5KGfZEuvFqs3XSpLct7cLQVDy7LhLoXG5iGPFFueQAwGFhtFhvTAsTApRNfbhQBbjXuZCULDjiFTDJ7fvBUbAhqbevvXvMRYft8otffAz55s3yo8Ng5XuXEv8n9s6iQSyRFaDGb51MHVSRJLZpW5Rstb4ysYpVK3wB34RYq3HC1UR7triu1defKhtCCnNNSmhfmg8uqTY1jzYQsPmk6whJqGesDVncvvFsmuvQa1WEPKHKX898AaF4jptaHSTQWjSyP9ap3oEbcN1JWVF2Pp5ji9kHviqrSkR6pb4Yfuvv5wzzFDWkdcKnTGBnAqvK8iPNoje5s79nCsPLPrpYvAYF7VpzgXWzWuqA1pZeL5ZYnCHPr1wVUegSDLsQvB2AdhqKwpN8NWFd876aRB3P23bv1XjoMwA3tnukRGt8Gq8TfwGyp6Lb7KETCKbi4oVyq6zJfnhpqp9DoUSFh8BFNP9Uxtu62Ku4Q7nwbfRvNTMm4rp4fRqjzqLy99gikm9p96mSozeya3KrktjpLQVz5vXZHTo1HQgggRPsJFBC7TnC417LeWbjpsUmhcKCYkHxHJK48HpL4piDNkDy3G358y6cv3x36sQE3UpvPWrv4JJiZDinzhpAa7AtwKLtVHh4X9PGvSAd6tegKZozRg5G28PFFEFKZwC8gbyJwCXh:1101312380`

## builtin:emacs_windows_linux

JSON: 
```json
{
  "fv": 4,
  "strict": true,
  "meta": {
    "name": "Emacs (Windows/Linux)",
    "author": "$$cmd_delete$$",
    "description": "Pre-bundled Emacs-style mappings for Windows and Linux. Note that these may not perfectly mirror Emacs's behavior.",
    "version": "$$cmd_delete$$",
    "id": "emacs_windows_linux",
    "systems": [
      "windows",
      "linux"
    ]
  },
  "actions": {
    "NAV_LINE_LEFT": [
      {"key": "a", "control": true, "shift": false}
    ],
    "NAV_LINE_RIGHT": [
      {"key": "e", "control": true, "shift": false}
    ],
    "NAV_WORD_LEFT": [
      {"key": "b", "altOption": true, "shift": false}
    ],
    "NAV_WORD_RIGHT": [
      {"key": "f", "altOption": true, "shift": false}
    ],
    "SEL_LINE_LEFT": [
      {"key": "a", "control": true, "shift": true}
    ],
    "SEL_LINE_RIGHT": [
      {"key": "e", "control": true, "shift": true}
    ],
    "SEL_WORD_LEFT": [
      {"key": "b", "altOption": true, "shift": true}
    ],
    "SEL_WORD_RIGHT": [
      {"key": "f", "altOption": true, "shift": true}
    ],
    "DEL_LINE_LEFT": [
      {"key": "u", "control": true}
    ],
    "DEL_LINE_RIGHT": [
      {"key": "k", "control": true}
    ],
    "DEL_WORD_LEFT": [
      {"key": "backspace", "altOption": true}
    ],
    "DEL_WORD_RIGHT": [
      {"key": "d", "altOption": true}
    ],
    "NAV_TEXT_START": [
      {"key": "comma", "altOption": true, "shift": false}
    ],
    "NAV_TEXT_END": [
      {"key": "period", "altOption": true, "shift": false}
    ],
    "SEL_TEXT_START": [
      {"key": "comma", "altOption": true, "shift": true}
    ],
    "SEL_TEXT_END": [
      {"key": "period", "altOption": true, "shift": true}
    ],
    "SEL_TEXT_UP": [
      {"key": "p", "control": true, "shift": true}
    ],
    "SEL_TEXT_DOWN": [
      {"key": "n", "control": true, "shift": true}
    ],
    "OVR_NAV_CHAR_LEFT": [
      {"key": "b", "control": true, "shift": false, "altOption": false},
      {"key": "left", "shift": false}
    ],
    "OVR_NAV_CHAR_RIGHT": [
      {"key": "f", "control": true, "shift": false, "altOption": false},
      {"key": "right", "shift": false}
    ],
    "OVR_SEL_CHAR_LEFT": [
      {"key": "b", "control": true, "shift": true, "altOption": false},
      {"key": "left", "shift": true}
    ],
    "OVR_SEL_CHAR_RIGHT": [
      {"key": "f", "control": true, "shift": true, "altOption": false},
      {"key": "right", "shift": true}
    ],
    "OVR_DEL_CHAR_LEFT": [
      {"key": "h", "control": true, "altOption": false},
      {"key": "backspace", "altOption": false}
    ],
    "OVR_DEL_CHAR_RIGHT": [
      {"key": "d", "control": true, "altOption": false},
      {"key": "delete"}
    ],
    "OVR_NAV_TEXT_UP": [
      {"key": "p", "control": true, "shift": false, "altOption": false},
      {"key": "up", "shift": false}
    ],
    "OVR_NAV_TEXT_DOWN": [
      {"key": "n", "control": true, "shift": false, "altOption": false},
      {"key": "down", "shift": false}
    ],
    "OVR_COPY": [
      {"key": "w", "altOption": true, "control": false}
    ],
    "OVR_CUT": [
      {"key": "w", "control": true, "altOption": false}
    ],
    "OVR_PASTE": [
      {"key": "y", "control": true, "altOption": false}
    ]
  },
  "flags": {
    "overrideVanillaNavigation": true,
    "crossLineSignMovement": true
  }
}
```

Sharecode: 
`CDS:EV1:3xdA1aMDAHdBoEw5G7VV2rSyLgnbpEz169GhAAtzRLv5i6ivHM2mseCHRv3UqeMVKoxSzfGS5azSpvexcjMxMMTuH4k7eidNbNaKAycRupCinTJ5WxUDnhyZSQjhv69vjL9jiDDc27DBCsUnUNsWYcdGJpJjBLnSej46yM9mscZN1jh14qNCijr4y7C8yTSchjJ1xAMch2BpwMrf6BNkBW2ZRvBvHNSH79cEhKEdE3FWNJvMDWvqQGrSGgpDJfaSFYGR22Gcf61zccSpUdWmkhoX3aWxTGUvkh4NWjKxeetRveViYE8euQowogZAjwT9P8eVtgy6nPBgzEUj7k7kHdSH9gqyzWDGV7xSE3X4uHyDdYRWFMJuXsuDQh9ksVuX8ieEL7jK1jRFu7xhESb4cDA8Yp1ftMgVEfrAtTjAjeoy8Ax9toQX1YLuHsWJbootiXT8TTonHamEH15LP7EgAFf65DrWT9W7Zv8V7okcsbZ3kFhMaGF1u7kzNqs2eq86jNhbMdenEcJpaZGfXTiUfeFgEX55jdjKaUmudXkqBdQC3SQ9BbMsLyvtfDve6jaLGVuatRyQnEoQMvaf5sRBKbQoKKM6tXszPEoGEBcL7eV7fcVjYxYLGGQrinZzjTHz7nUfLegzCMtQ2mHLmKtNH5PoE7SEuPU5rupVT94Ao45mWeQLV4Vux6hFh3Xqas75E6HLW9Ba6N26pK2ZBChfcFxcQX61TJrRLYEL2bYCqDdQTgB2A5TwtDN47D4cothdQ7dDQ4q91X2tFUZPSKCSS96YK4RsUvtMkCHMQq2h4VNTofKYPGVTyYg1skow2PA1LkQf:878752458`

## builtin:mac

JSON:
```json
{
  "fv": 4,
  "strict": true,
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
    ],
    "OVR_COPY": [
      {"key": "c", "superCommand": true}
    ],
    "OVR_CUT": [
      {"key": "x", "superCommand": true}
    ],
    "OVR_PASTE": [
      {"key": "v", "superCommand": true}
    ],
    "OVR_SELECT_ALL": [
      {"key": "a", "superCommand": true}
    ]
  },
  "flags": {
    "overrideVanillaNavigation": true,
    "crossLineSignMovement": true
  }
}
```

Sharecode:
`CDS:EV1:tMeXxZVB45VieA3HvggAeo3p5zjrCbj7crBL5PbmPwdWGpgx8nz6vXnqxX48Cgc5BHZX63EYegfSTth3sTL16tud1DU6KFQKQNLmEtrhaoojTDiqtcm88t2ePt3yKWY4NnfPEUBhPqMgmoFCZwkfgVMpmKr6L2QYWmtruBniZRgBG5hxpA95W2E3pn5dv9SnDhpQenXnr2cWvyENAGmNpTzpUyktLDQoEZGJZE8CKK84G8jXsTf81f1Kxojv88pF66AeMYEqMCYCki7jafgTDi4YspYGxLvqrd9eZPGUPy1JvV588pD6MFT13sYK2m5yiJk5qGwqMg17DSbXjEUzU3dLN322AqPDP4Xvm3n1nztxo2fuCeGPpmD7poJDbHCws7RNKtVcgnpas8EhxHDUBqZPwyDWjbUwqxNfmqSvKQwYQbU3jTt527Z89g1Li4QPBto4bNFc9Fr7jWYQVdu8k7PxahZEFjPes5YrufVoNtU1ZC8VuMreMQNeBUz9MH5qN3oDbdYu7L5Jupr6XBrpKMoVzGGS4ygdcBMfG7ZZF6ruzNjTGDjHvrNDGDuJXpXfnccn9ami1xM4SipedBHugVM56FyR26MsRcddBMwmnSr4LTrGT6hofbsGuapfVJZUNfgab6WvHDAgHMXFQD9reFnfuMQ3KNtzMFzPJXSyVLfbFYDjPxv94yB5:2419707130`

## builtin:readline

JSON:
```json
{
  "fv": 4,
  "strict": true,
  "meta": {
    "name": "GNU Readline",
    "author": "$$cmd_delete$$",
    "description": "Pre-bundled GNU Readline-style mappings. Note that these may not perfectly mirror Readline's behavior.",
    "version": "$$cmd_delete$$",
    "id": "readline",
    "systems": [
      "mac",
      "windows",
      "linux"
    ]
  },
  "actions": {
    "NAV_LINE_LEFT": [
      {"key": "a", "control": true, "shift": false}
    ],
    "NAV_LINE_RIGHT": [
      {"key": "e", "control": true, "shift": false}
    ],
    "SEL_LINE_LEFT": [
      {"key": "a", "control": true, "shift": true}
    ],
    "SEL_LINE_RIGHT": [
      {"key": "e", "control": true, "shift": true}
    ],
    "NAV_WORD_LEFT": [
      {"key": "b", "altOption": true, "shift": false}
    ],
    "NAV_WORD_RIGHT": [
      {"key": "f", "altOption": true, "shift": false}
    ],
    "SEL_WORD_LEFT": [
      {"key": "b", "altOption": true, "shift": true}
    ],
    "SEL_WORD_RIGHT": [
      {"key": "f", "altOption": true, "shift": true}
    ],
    "DEL_LINE_LEFT": [
      {"key": "u", "control": true}
    ],
    "DEL_LINE_RIGHT": [
      {"key": "k", "control": true}
    ],
    "DEL_WORD_LEFT": [
      {"key": "w", "control": true},
      {"key": "backspace", "altOption": true}
    ],
    "DEL_WORD_RIGHT": [
      {"key": "d", "altOption": true}
    ],
    "SEL_TEXT_UP": [
      {"key": "p", "control": true, "shift": true}
    ],
    "SEL_TEXT_DOWN": [
      {"key": "n", "control": true, "shift": true}
    ],
    "OVR_NAV_CHAR_LEFT": [
      {"key": "b", "control": true, "shift": false, "altOption": false},
      {"key": "left", "shift": false}
    ],
    "OVR_NAV_CHAR_RIGHT": [
      {"key": "f", "control": true, "shift": false, "altOption": false},
      {"key": "right", "shift": false}
    ],
    "OVR_SEL_CHAR_LEFT": [
      {"key": "b", "control": true, "shift": true, "altOption": false},
      {"key": "left", "shift": true}
    ],
    "OVR_SEL_CHAR_RIGHT": [
      {"key": "f", "control": true, "shift": true, "altOption": false},
      {"key": "right", "shift": true}
    ],
    "OVR_DEL_CHAR_LEFT": [
      {"key": "h", "control": true, "altOption": false},
      {"key": "backspace", "altOption": false}
    ],
    "OVR_DEL_CHAR_RIGHT": [
      {"key": "d", "control": true, "altOption": false},
      {"key": "delete"}
    ],
    "OVR_NAV_TEXT_UP": [
      {"key": "p", "control": true, "shift": false, "altOption": false},
      {"key": "up", "shift": false}
    ],
    "OVR_NAV_TEXT_DOWN": [
      {"key": "n", "control": true, "shift": false, "altOption": false},
      {"key": "down", "shift": false}
    ],
    "OVR_PASTE": [
      {"key": "y", "control": true, "altOption": false}
    ]
  },
  "flags": {
    "overrideVanillaNavigation": true,
    "crossLineSignMovement": true
  }
}
```

Sharecode: 
`CDS:EV1:3Z15yMrwbyh3XwikdeDtaZqPW7xLFBxq9hLMky5CzDRNFF6XMYy5eN9zHvXRpwDQKLxkc436VgFDcRtsCKR95EsTAjjEZtBqLz6QkcTUU1CfwdfM9yiDktpXHxEe3Mr439ZndY6fu3QYCxVQfwHoJqoAcUB82gM1ZgQxjMbaMryePx2A3N2pTdh9KtqKqBKSikKt5sgJMvpqGj9FkedsUSbgsVF8iYvJUvuuGQkmeJLLCYqgmEqyAw9ofyygGX7cEPCCDgsHCnJRMFd6zAs6VDfEoyJRjDVZRTxAGngtGupAmtGJn6eucXnqNgoNgTL8nrgS4iTLtFcgv5JPt9F8CCk1155JGHFHxDE8o33dWV1MPdYNCRkefesWbwnHDPypufADgw5Hw9LMPo7TqvK7MXPhcP23e2kQmwsgfZ2qU42n93bWHfdFWCywGGeoW6cZN1KwXpWMQQ2mFd8RuKWaSJNhrLU9feBdHppWq6Jey5XDz1QcGnARCsbEMDz3ZhoJL8thes67p48PKPdzR9fQaBKiwLLzgSQfMnUqF1WvFr9ft4NuF1PLzYfBybxGXPWBxxdhYzxG2Cmg4kjhW2xnr55NUATCoed6mxgdLHPHMzW2AwirMwM6TMsTeA9Pf6VdL2PmN1pN2QEakz5ZgMW8phFyeL6ZZtug8ywfGxKjPfsVKTQswXRj3fRUKy7aQwoBtWL51wovjk5Mej21shwHuMe8UJ9c1UsiZMJD2YBQNwWHTM:3994342116`

## builtin:windows_linux

JSON:
```json
{
  "fv": 4,
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
      {"key": "home", "control": true, "shift": false}
    ],
    "SEL_TEXT_START": [
      {"key": "home", "control": true, "shift": true}
    ],
    "NAV_TEXT_END": [
      {"key": "end", "control": true, "shift": false}
    ],
    "SEL_TEXT_END": [
      {"key": "end", "control": true, "shift": true}
    ],
    "NAV_LINE_LEFT": [
      {"key": "home", "control": false, "shift": false}
    ],
    "SEL_LINE_LEFT": [
      {"key": "home", "control": false, "shift": true}
    ],
    "NAV_LINE_RIGHT": [
      {"key": "end", "control": false, "shift": false}
    ],
    "SEL_LINE_RIGHT": [
      {"key": "end", "control": false, "shift": true}
    ],
    "NAV_WORD_LEFT": [
      {"key": "left", "control": true, "shift": false}
    ],
    "SEL_WORD_LEFT": [
      {"key": "left", "control": true, "shift": true}
    ],
    "NAV_WORD_RIGHT": [
      {"key": "right", "control": true, "shift": false}
    ],
    "SEL_WORD_RIGHT": [
      {"key": "right", "control": true, "shift": true}
    ],
    "DEL_WORD_LEFT": [
      {"key": "backspace", "control": true}
    ],
    "DEL_WORD_RIGHT": [
      {"key": "delete", "control": true}
    ],
    "SEL_TEXT_UP": [
      {"key": "up", "shift": true}
    ],
    "SEL_TEXT_DOWN": [
      {"key": "down", "shift": true}
    ],
    "OVR_NAV_CHAR_LEFT": [
      {"key": "left", "control": false, "shift": false}
    ],
    "OVR_NAV_CHAR_RIGHT": [
      {"key": "right", "control": false, "shift": false}
    ],
    "OVR_SEL_CHAR_LEFT": [
      {"key": "left", "control": false, "shift": true}
    ],
    "OVR_SEL_CHAR_RIGHT": [
      {"key": "right", "control": false, "shift": true}
    ],
    "OVR_DEL_CHAR_LEFT": [
      {"key": "backspace", "control": false, "shift": false}
    ],
    "OVR_DEL_CHAR_RIGHT": [
      {"key": "delete", "control": false, "shift": false}
    ],
    "OVR_NAV_TEXT_UP": [
      {"key": "up", "control": false, "shift": false}
    ],
    "OVR_NAV_TEXT_DOWN": [
      {"key": "down", "control": false, "shift": false}
    ],
    "OVR_COPY": [
      {"key": "c", "control": true}
    ],
    "OVR_CUT": [
      {"key": "x", "control": true}
    ],
    "OVR_PASTE": [
      {"key": "v", "control": true
      }
    ],
    "OVR_SELECT_ALL": [
      {"key": "a", "control": true
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
`CDS:EV1:3QhD4z2TSi865KBmD8aSoLrzAijpn9bpsazX68FTEXQxDHeAWSeua77UNrDUb7CVnV8jnCKtmxymuf6VmmRMtbiQPzYiytZfpPQn2iCAByovKoZC8BDFh2HhENmVq15ayjvR9AxvyVZNsHbfAU7ewin1g7NcT7jVpPRNktvAsBMnaEJiuTiqECRfjXSwSegGdDQ2GJgneHjW42tMUWKqSBsCKtVGk12rVh7CkP4DdKj3v53Ui3uPJu7eWz8ArF2mX9dcEf9gqni6MQG72qWpQdgLcXUjvQ2szVm2VUto8sGCsvviqXJJx8YmCwZMCiNQZKUzrqiwHqyBSLGWaPDb52rD4bj5wKqNHTp2myCYGQrsAaqKNcYz4JseJEynCwX6uU6simppPFFWGGYYUcd58bfbqounGm8evmCpE4BgyHhjSHKaHStXWSvHoZpZ6BAweVy8y6EwzTKW62jDk4u2WdY1Q4NmL6LQwXeJEdtYkrJuXrEVe3J4tkxSHbBvgH9yc8mGUnZdQ5hTDjbnPc3rt4QAhsj2GxYz7QKZfN3BgatoGGyZWJquoUHXg78R1SdkzEKPSHKV3b7HkRzNLeWjVBPZjfM9XLkzWj5G62VR5MxVJcEvvoD2Dnmkh3mLpW3sDcr9UzkjGRcVPdNyx5BNNN1nwhFhMCVPDwH7w4B:3121412833`