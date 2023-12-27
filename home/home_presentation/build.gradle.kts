plugins {
    `android-library`
    `kotlin-android`
}

apply {
    from("$rootDir/compose-module.gradle")
}

android {
    namespace = "com.timserio.home_presentation"
}

dependencies {
    "implementation"(project(Modules.core))
    "implementation"(project(Modules.coreUi))
    "implementation"(project(Modules.homeDomain))
    "implementation"(project(Modules.selectLocationPresentation))
    "testImplementation"(project(Modules.testUtils))
    "testImplementation"(Testing.coroutines)
}
