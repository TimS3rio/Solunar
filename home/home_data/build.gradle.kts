plugins {
    `android-library`
    `kotlin-android`
}

apply {
    from("$rootDir/base-module.gradle")
}

android {
    namespace = "com.timserio.home_data"
}

dependencies {
    "implementation"(project(Modules.core))
    "implementation"(project(Modules.homeDomain))

    "implementation"(Retrofit.okHttp)
    "implementation"(Retrofit.retrofit)
    "implementation"(Retrofit.moshiConverter)

    "kapt"(Room.roomCompiler)
    "implementation"(Room.roomKtx)
    "implementation"(Room.roomRuntime)
}
