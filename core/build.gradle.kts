plugins {
    `android-library`
    `kotlin-android`
}

apply {
    from("$rootDir/base-module.gradle")
}

android {
    namespace = "com.timserio.core"
}

dependencies {
    "implementation"("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
}
