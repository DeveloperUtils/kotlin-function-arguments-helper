# ![Kotlin Function Arguments Helper](src/main/resources/META-INF/pluginIcon.svg) kotlin-function-arguments-helper 

![Build](https://github.com/DeveloperUtils/kotlin-function-arguments-helper/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/com.github.developerutils.kotlin-function-arguments-helper.svg)](https://plugins.jetbrains.com/plugin/14168-kotlin-function-arguments-helper)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/com.github.developerutils.kotlin-function-arguments-helper.svg)](https://plugins.jetbrains.com/plugin/14168-kotlin-function-arguments-helper)

<!-- Plugin description -->
IntelliJ plugin that provides intention action for constructors or functions to fill in arguments with name and a default value.


<a target="_blank" href="https://user-images.githubusercontent.com/774381/79955782-8f69a580-847f-11ea-94fa-c07a8816d7f8.gif">
  <img src="https://user-images.githubusercontent.com/774381/79955782-8f69a580-847f-11ea-94fa-c07a8816d7f8.gif" alt="kotlin-function-arguments-helper-demo animation" style="max-width:100%;"/>
</a>

<!-- Plugin description end -->

Inspired by and extended from [Kotlin Fill Class Plugin](https://github.com/suusan2go/kotlin-fill-class)

## Installation

- Using IDE built-in plugin system:
  
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "kotlin-function-arguments-helper"</kbd> >
  <kbd>Install Plugin</kbd>
  
- Manually:

  Download the [latest release](https://github.com/WorkingDevel/kotlin-function-arguments-helper/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


### Build & Install from Disk

0. Checkout repository `git clone https://github.com/DeveloperUtils/kotlin-function-arguments-helper.git`
   or `git clone git@github.com:DeveloperUtils/kotlin-function-arguments-helper.git`
1. Build this plugin `./gradlew build`
2. Install the plugin from `./build/distributions/Kotlin Function Arguments Helper*.zip`
   with <kbd>Install plugin from disk</kbd> in IDEA plugin manager

## Thanks

- [@suunsa](https://github.com/suunsan2go) For the plugin idea and development of it.
- [@t-kameyama](https://github.com/t-kameyama) Fill function call arguments [#17](https://github.com/suusan2go/kotlin-fill-class/pull/17)
- [@shiraji](https://github.com/shiraji) Convert to intention & Support non-empty constructor [#6](https://github.com/suusan2go/kotlin-fill-class/pull/6)
- [@Pluu](https://github.com/Pluu) [@naofumi-fujii](https://github.com/naofumi-fujii]) fix #2 Double type fill error [#3](https://github.com/suusan2go/kotlin-fill-class/pull/3)

---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
