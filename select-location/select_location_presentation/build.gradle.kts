import com.timserio.buildsrc.GoogleMaps

plugins {
    `android-library`
    `kotlin-android`
}

apply {
    from("$rootDir/compose-module.gradle")
}

android {
    namespace = "com.timserio.select_location_presentation"
}

dependencies {
    "implementation"(project(Modules.core))
    "implementation"(project(Modules.coreUi))
    "implementation"(project(Modules.homeDomain))
    "implementation"(project(Modules.testUtils))
    "implementation"(GoogleMaps.maps)
    "implementation"(Compose.maps)
    "implementation"(Testing.coroutines)
    "implementation"(Testing.mockito)
}
