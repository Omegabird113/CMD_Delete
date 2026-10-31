# NavActions List

This is a detailed list of each action and its properties alongside the supported mappings format version.

## The Table

| Action               | Type      | Scope         | Offset    | Override | Minimum Mappings FV Supported |
|:---------------------|:----------|:--------------|:----------|:---------|:------------------------------|
| `NAV_LINE_LEFT`      | `MOVE`    | `WITHIN_LINE` | `LEFT`    | `false`  | `1`                           |
| `NAV_LINE_RIGHT`     | `MOVE`    | `WITHIN_LINE` | `RIGHT`   | `false`  | `1`                           |
| `NAV_WORD_LEFT`      | `MOVE`    | `WORD`        | `LEFT`    | `false`  | `1`                           |
| `NAV_WORD_RIGHT`     | `MOVE`    | `WORD`        | `RIGHT`   | `false`  | `1`                           |
| `SEL_LINE_LEFT`      | `SELECT`  | `WITHIN_LINE` | `LEFT`    | `false`  | `1`                           |
| `SEL_LINE_RIGHT`     | `SELECT`  | `WITHIN_LINE` | `RIGHT`   | `false`  | `1`                           |
| `SEL_WORD_LEFT`      | `SELECT`  | `WORD`        | `LEFT`    | `false`  | `1`                           |
| `SEL_WORD_RIGHT`     | `SELECT`  | `WORD`        | `RIGHT`   | `false`  | `1`                           |
| `DEL_LINE_LEFT`      | `DELETE`  | `WITHIN_LINE` | `LEFT`    | `false`  | `1`                           |
| `DEL_LINE_RIGHT`     | `DELETE`  | `WITHIN_LINE` | `RIGHT`   | `false`  | `1`                           |
| `DEL_WORD_LEFT`      | `DELETE`  | `WORD`        | `LEFT`    | `false`  | `1`                           |
| `DEL_WORD_RIGHT`     | `DELETE`  | `WORD`        | `RIGHT`   | `false`  | `1`                           |
| `NAV_TEXT_START`     | `MOVE`    | `TEXT`        | `UP`      | `false`  | `1`                           |
| `NAV_TEXT_END`       | `MOVE`    | `TEXT`        | `DOWN`    | `false`  | `1`                           |
| `SEL_TEXT_START`     | `SELECT`  | `TEXT`        | `UP`      | `false`  | `1`                           |
| `SEL_TEXT_END`       | `SELECT`  | `TEXT`        | `DOWN`    | `false`  | `1`                           |
| `SEL_TEXT_UP`        | `SELECT`  | `LINE`        | `UP`      | `false`  | `1`                           |
| `SEL_TEXT_DOWN`      | `SELECT`  | `LINE`        | `DOWN`    | `false`  | `1`                           |
| `OVR_NAV_CHAR_LEFT`  | `MOVE`    | `CHAR`        | `LEFT`    | `true`   | `3`                           |
| `OVR_NAV_CHAR_RIGHT` | `MOVE`    | `CHAR`        | `RIGHT`   | `true`   | `3`                           |
| `OVR_SEL_CHAR_LEFT`  | `SELECT`  | `CHAR`        | `LEFT`    | `true`   | `3`                           |
| `OVR_SEL_CHAR_RIGHT` | `SELECT`  | `CHAR`        | `RIGHT`   | `true`   | `3`                           |
| `OVR_DEL_CHAR_LEFT`  | `DELETE`  | `CHAR`        | `LEFT`    | `true`   | `3`                           |
| `OVR_DEL_CHAR_RIGHT` | `DELETE`  | `CHAR`        | `RIGHT`   | `true`   | `3`                           |
| `OVR_NAV_TEXT_UP`    | `MOVE`    | `LINE`        | `UP`      | `true`   | `3`                           |
| `OVR_NAV_TEXT_DOWN`  | `MOVE`    | `LINE`        | `DOWN`    | `true`   | `3`                           |
| `OVR_COPY`           | `EDIT`    | `TEXT`        | `INVALID` | `true`   | `4`                           |
| `OVR_CUT`            | `EDIT`    | `TEXT`        | `INVALID` | `true`   | `4`                           |
| `OVR_PASTE`          | `EDIT`    | `TEXT`        | `INVALID` | `true`   | `4`                           |
| `OVR_SELECT_ALL`     | `EDIT`    | `TEXT`        | `INVALID` | `true`   | `4`                           |
| `NONE`               | `NO_TYPE` | `NO_SCOPE`    | `INVALID` | `false`  | `N/A`                         |
