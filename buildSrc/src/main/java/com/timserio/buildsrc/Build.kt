object Build {
    private const val androidBuildToolsVersion = "8.1.1"

    const val androidApplication = "com.android.application"
    const val androidLibrary = "com.android.library"
    const val hilt = "com.google.dagger.hilt.android"

    const val androidBuildTools = "com.android.tools.build:gradle:$androidBuildToolsVersion"

    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.version}"

    private const val hiltAndroidGradlePluginVersion = "2.45"
    const val hiltAndroidGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:$hiltAndroidGradlePluginVersion"
}
