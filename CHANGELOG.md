<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Kotlin Function Arguments Helper Changelog

## Unreleased

### Added

### Changed
- Update kotlin to v1.9.21
- Update compatibility for IDEA 2021.3 - 2023.3

### Deprecated

### Removed

### Fixed

### Security

## 2022.1.2 - 2022-12-06

### Changed
- Update kotlin to v1.7.22
- Update compatibility for IDEA 2021.3 - 2023.1

## 2022.1.1

### Changed
- Applied changes from [IntelliJ Platform Plugin Template#Next@f8b4bb9](https://github.com/JetBrains/intellij-platform-plugin-template/tree/next)
- Update kotlin to v1.7.10
- Update `platformVersion` to `2021.3.3`
- Change since/until build to `213-222.*` (2021.3 - 2022.2)

## 2022.1.0

### Changed
- imported changes from yet unsubmitted PR [IntelliJ Platform Plugin Template#Next](https://github.com/JetBrains/intellij-platform-plugin-template/pull/240/files)
- - Update `platformVersion` to `2021.2.4`
  - Change since/until build to `212-221.*` (2021.2 - 2022.1)
  - Dependencies - upgrade `org.jetbrains.intellij` to `1.6.0`
  - Dependencies - upgrade `org.jetbrains.kotlin.jvm` to `1.6.21`
  - Dependencies (GitHub Actions) - upgrade `actions/checkout` to `3`
  - Dependencies (GitHub Actions) - upgrade `actions/cache` to `3`
  - Dependencies (GitHub Actions) - upgrade `actions/setup-java` to `3`
  - Dependencies (GitHub Actions) - upgrade `actions/upload-artifact` to `3`
  - Dependencies (GitHub Actions) - upgrade `JetBrains/qodana-action` to `5.1.0`

## 2021.3.0

### Added
- - if lambda has 2+ parameters: add also parameter with guessed names

### Changed
- rebased on latest [IntelliJ Platform Plugin Template](https://github.com/JetBrains/intellij-platform-plugin-template)
- - *gradle* to 7.3
  - *kotlin* to 1.6.0
  - *detekt* to 1.18.1

## 2021.2.0

### Added
- - Don't add default values (guessed by variable type)
  - Do not add arguments that have default values declared

### Changed
- Kotlin Function Arguments Helper is now an inspection instead of a intention (because they can have options)
- Quick fix is now named `Add missing constructor arguments` and `Add missing function arguments` instead of `Fill (constructor|function) arguments`
- - *gradle* to 7.0.2
  - *kotlin* to 1.5.0
- detekt is using default configuration from [https://github.com/detekt/detekt/blob/master/detekt-core/src/main/resources/default-detekt-config.yml](https://github.com/detekt/detekt/blob/693f4f3/detekt-core/src/main/resources/default-detekt-config.yml)
- Plugin is verified against release 2021.1 instead of pre-release 211.5538.20

### Removed
- KotlinFunctionArgumentsHelperIntention

### Fixed
- patching `CHANGELOG.md` leaded to build error

## 2021.1.2

### Added
- plugin logo

### Changed
- rebased plugin on [IntelliJ Platform Plugin Template](https://github.com/JetBrains/intellij-platform-plugin-template)
- build with kotlin `1.4.30`

### Removed
- dropped support for IDEA before `2020.3`

### Fixed
- runs now with IDEA `2021.*`
- Release build pipeline

## 2020.1.2

### Added
- Added demo animation.

### Changed
- Widened range of IDE compatibility.
- Build with Kotlin ${kotlinVersion}
