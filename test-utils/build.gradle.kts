plugins {
    `android-library`
    `kotlin-android`
}

apply {
    from("$rootDir/test-utils-module.gradle")
}

android {
    namespace = "com.timserio.test_utils"
}

dependencies { }
