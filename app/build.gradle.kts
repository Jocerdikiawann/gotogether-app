plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

val mapsApiKeys = findProperty("MAPS_API_KEYS")
val baseUrlGoogle = findProperty("BASE_URL_GOOGLE")

android {
    namespace = "com.example.livetracking"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.livetracking"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            manifestPlaceholders["MAPS_API_KEYS"] = "$mapsApiKeys"
            buildConfigField("String", "MAPS_API_KEYS", "\"${mapsApiKeys}\"")
            buildConfigField("String", "BASE_URL_GOOGLE", "\"$baseUrlGoogle\"")
        }
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            manifestPlaceholders["MAPS_API_KEYS"] = "$mapsApiKeys"
            buildConfigField("String", "MAPS_API_KEYS", "\"${mapsApiKeys}\"")
            buildConfigField("String", "BASE_URL_GOOGLE", "\"$baseUrlGoogle\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.1"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/gradle/incremental.annotation.processors"
        }
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(platform(Libs.SquareUp.okhttpBOM))
    implementation(Libs.SquareUp.okhttp)
    implementation(Libs.SquareUp.logging)
    implementation(Libs.Google.gsonConverter)
    implementation(Libs.Maps.utils)
    implementation(Libs.AndroidX.multidex)
    implementation(Libs.Google.systemUi)
    implementation(Libs.Google.hiltNavigationCompose)
    implementation(Libs.Google.location)
    implementation(Libs.AndroidX.Room.roomRuntime)
    implementation(Libs.AndroidX.Room.roomKtx)
    annotationProcessor(Libs.AndroidX.Room.roomCompiler)
    kapt(Libs.AndroidX.Room.roomCompiler)
    implementation(Libs.AndroidX.androidxCore)
    implementation(Libs.AndroidX.androidXLifeCycleRuntime)
    implementation(Libs.AndroidX.navigation)
    implementation(Libs.AndroidX.Compose.liveData)
    implementation(Libs.Coil.coil)
    implementation(Libs.SquareUp.retrofit)
    implementation(Libs.Google.gson)
    implementation(Libs.Google.dagger)
    implementation(Libs.Google.daggerCompiler)
    implementation(Libs.Kotlinx.coroutine)
    implementation(Libs.AndroidX.Compose.activityCompose)
    implementation(Libs.AndroidX.Compose.ui)
    implementation(Libs.AndroidX.Compose.toolingpreview)
    implementation(Libs.AndroidX.Compose.materialTree)
    implementation(Libs.Maps.mapsCompose)
//    implementation(Libs.Maps.openStreetMaps)
//    implementation(Libs.Maps.bonusPackOsm)
    implementation(Libs.Maps.playServiceMaps)
    testImplementation(Libs.Junit.junit)
    androidTestImplementation(Libs.AndroidX.Test.junit)
    androidTestImplementation(Libs.AndroidX.Test.espresso)
    androidTestImplementation(Libs.AndroidX.Test.composeTestJunit)
    debugImplementation(Libs.AndroidX.Compose.uiTooling)
    debugImplementation(Libs.AndroidX.Compose.uiTestManifest)
}