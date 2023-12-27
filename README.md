# Solunar

![](https://github.com/TimS3rio/Solunar/blob/main/solunar.gif)

Solunar is an Android app that returns solunar times based on a selectable location and a selectable date. Solunar times help fishermen and hunters predict the best times of day to hunt or fish. I love to fish and I have used solunar times in the past so I figured it would be fun to build an app that returns solunar times. This app is built fully in Jetpack Compose. This app uses clean architecture and is set up in a multi module structure with the following modules:
* core: contains code that is shared between multiple modules
* core-ui: contains compose related code that is shared between multiple modules
* home: a base module for the home screen which contains three submodules for the data, domain, and presentation layers:
  * home_data: the data layer for the home screen
  * home_domain: the domain layer for the home screen
  * home_presentation: the presentation layer for the home screen
* select-location: a base module for the select location screen which contains a single submodule for the presentation layer:
  * select_location_presentation: the presentation layer for the select location screen
* test-utils: a module for utilities that are used for testing

This app also contains testing for the data, domain, and presentation layer of each feature. I plan to continue to add to this app so stay tuned for updates!

API: https://solunar.org/

## Tech
* Jetpack Compose
* Multi module architecture
* Clean architecture
* MVI
* Dagger Hilt
* Retrofit
* OkHttp
* Moshi
* Kotlin DSL
* Coroutines
* Ktlint
* Compose UI Testing
* UIAutomator
* MockWebServer
* Junit
* Kotlin