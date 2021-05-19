<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Kotlin Function Arguments Helper Changelog

## [Unreleased]
### Added

### Changed

### Deprecated

### Removed

### Fixed

### Security
## [2021.2.0]
### Added
- Plugin configuration options. You can now change the plugin behavior by setting options in the inspections settings. Currently, these two options are supported:
  - Don't add default values (guessed by variable type)
  - Do not add arguments that have default values declared

### Changed
- Kotlin Function Arguments Helper is now an inspection instead of a intention (because they can have options)
- Quick fix is now named `Add missing constructor arguments` and `Add missing function arguments` instead of `Fill (constructor|function) arguments`
- upgraded
  - *gradle* to 7.0.2
  - *kotlin* to 1.5.0
- detekt is using default configuration from [https://github.com/detekt/detekt/blob/master/detekt-core/src/main/resources/default-detekt-config.yml](https://github.com/detekt/detekt/blob/693f4f3/detekt-core/src/main/resources/default-detekt-config.yml)
- Plugin is verified against release 2021.1 instead of pre-release 211.5538.20

### Removed
- KotlinFunctionArgumentsHelperIntention

### Fixed
- patching `CHANGELOG.md` leaded to build error

## [2021.1.2]
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

## [2020.1.2]
### Added
- Added demo animation.

### Changed
- Widened range of IDE compatibility.
- Build with Kotlin ${kotlinVersion}
