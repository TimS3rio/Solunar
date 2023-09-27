import com.timserio.buildsrc.LocationService

plugins {
    id("com.android.application")
    kotlin("android")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
    id(KtLint.ktLint)
}

android {
    namespace = ProjectConfig.appId
    compileSdk = ProjectConfig.compileSdk

    defaultConfig {
        applicationId = ProjectConfig.appId
        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk
        versionCode = ProjectConfig.versionCode
        versionName = ProjectConfig.versionName

        testInstrumentationRunner = "com.timserio.solunar.HiltTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }
    kotlinOptions {
        jvmTarget = "18"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(Modules.core))
    implementation(project(Modules.coreUi))
    implementation(project(Modules.homeData))
    implementation(project(Modules.homeDomain))
    implementation(project(Modules.homePresentation))

    implementation(AndroidX.coreKtx)
    implementation(AndroidX.lifecycle)
    implementation(Compose.activityCompose)
    implementation(platform(Compose.bom))
    implementation(Compose.ui)
    implementation(Compose.uiGraphics)
    implementation(Compose.toolingPreview)
    implementation(Compose.material3)
    implementation(Compose.hiltNavigationCompose)
    implementation(Compose.permission)
    implementation(LocationService.location)
    implementation(DaggerHilt.hilt)
    kapt(DaggerHilt.hiltCompiler)

    testImplementation(Testing.junit)
    androidTestImplementation(Testing.extJunit)
    androidTestImplementation(Testing.espressoCore)
    androidTestImplementation(platform(Compose.bom))
    androidTestImplementation(Testing.composeJunit)
    androidTestImplementation(Testing.testRule)
    debugImplementation(Testing.composeTooling)
    debugImplementation(Testing.composeManifest)
    androidTestImplementation(DaggerHilt.hiltTesting)
    kaptAndroidTest(DaggerHilt.hiltCompiler)
    testImplementation(DaggerHilt.hiltTesting)
    kaptTest(DaggerHilt.hiltCompiler)
}
