# Solunar

![](https://github.com/TimS3rio/Solunar/blob/main/solunar.gif)

Solunar is an Android app that returns solunar times based on the user’s location and a selectable date. Solunar times help fishermen and hunters predict the best times of day to hunt or fish. I love to fish and I have used solunar times in the past so I figured it would be fun to build an app that returns solunar times. This repo is a side project that I started a few weeks ago. This app is built fully in Jetpack Compose. This app uses clean architecture and is set up in a multi module structure with the following modules:
* core: contains code that is shared between multiple modules
* core-ui: contains compose related code that is shared between multiple modules
* home: a base module for the home screen which contains three submodules for the data, domain, and presentation layers:
  * home_data: the data layer for the home screen
  * home_domain: the domain layer for the home screen
  * home_presentation: the presentation layer for the home screen

This app also contains testing for the data, domain, and presentation layer. I plan to continue to add to this app so stay tuned for updates!

API: https://solunar.org/

Stack: Jetpack Compose, Multi module architecture, Clean architecture, MVI, Dagger Hilt, Retrofit, OkHttp, Moshi, Kotlin DSL, Coroutines, Ktlint, Espresso, MockWebServer, Junit, Kotlin