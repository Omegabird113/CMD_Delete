# Contributing Guidelines

## General Contribution Rules

All issues, pull requests (PRs), security reports, and other contributions must follow these rules:

- Be reasonable:
    - Follow GitHub's guidelines.
    - Follow the law.
    - Treat people humanely.
    - Be appropriate.
    - No spam.
    - No unnecessary self-promotion.
    - No unrelated/off-topic conversations.
    - etc.
- Response times may vary, as this is a personal project.

## Issue & Security Report Rules

- Fully & properly fill out an issue template when submitting an issue and provide all information asked for.
- If you do not provide enough information, they will be closed.
- Only one topic per issue/report.
- Check that your issue/report has not already been reported.
- Report security vulnerabilities via GitHub security advisories in the `Security` tab.

## Pull Request Rules

- PRs must have a limited scope of only one feature or bug fix.
- Include any necessary changes to the tests or documentation with PRs.
- PRs must pass all checks and have all merge conflicts resolved.
- Low-quality or large change PRs will get closed (potentially without comment).
- PRs should follow the project's existing code style, including following these rules in the Code Style Rules section.
- By making a PR, you agree that you have the legal rights to your contribution and that it will be licensed under this
  set of terms for each type of contribution:
    - Code contributions are licensed under the [Apache 2.0 license](https://www.apache.org/licenses/LICENSE-2.0.html).
    - CMD + Delete wiki/documentation contributions are licensed under
      the [CC BY 4.0 license](https://creativecommons.org/licenses/by/4.0/legalcode.en).
    - All builtin mappings JSON file contributions are licensed under either
      the [Apache 2.0 license](https://www.apache.org/licenses/LICENSE-2.0.html) or
      the [CC BY 4.0 license](https://creativecommons.org/licenses/by/4.0/legalcode.en) (You may choose to comply with
      either license).

## Code Style Rules

- The structure/patterns used in your contributions should be similar to existing code.
- Class and method names should be descriptive, but not overly so.
- Comments should generally be avoided in favor of descriptive code and docs, excluding certain small one-line
  explanation/edge-case comments necessary to understand the code.
- Braces in if-statements should be omitted whenever possible to save unnecessary lines of code.
- Long chains in things like streams, command builders, and similar should be formatted in a way where the code is clear
  and easy-to-read.
- The `final` keyword should be used extensively wherever applicable, including in local variables, method signatures,
  class fields, and more instead of relying on implicit finality.
- The `var` keyword for type inference is not allowed.
- All classes should be `final` unless inheritance is needed. Inheritance should only be used with interfaces if
  possible, though composition is still preferred.
- Names of interfaces should start with `I`.
- Utility/Manager classes should be `final` and have a private constructor.
- The project intentionally uses the spelling `builtin` instead of the proper English `built-in` and `keyanme` instead
  of `key name` in many places to match its technical naming.
- `private static final` variables should be in UPPER_SNAKE_CASE, other variables in camelCase, method names in
  camelCase, class names in UpperCamelCase, package names using single-words in lowercase or lower_snake_case if
  absolutely needed, and Strings should be in proper English case conventions.
- No IntelliJ warnings (excluding `.gradle` files) or spelling errors are allowed. Warnings may be suppressed if they
  don't make sense given the context. Unrecognized spellings should be added to the project-level IntelliJ dictionary.
  IntelliJ grammar errors of defensible decisions that still make sense are allowed.
- The IntelliJ formatter should be run on any PRs.
- Otherwise, this is a Java `25` project, so follow normal java conventions.