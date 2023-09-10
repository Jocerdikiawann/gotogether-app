@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.daggerHiltAndroid)
    alias(libs.plugins.google.mapsplatform)
    id("kotlin-parcelize")
    kotlin("kapt")
}

val mapsApiKeys = findProperty("MAPS_API_KEYS")
val baseUrlGoogle = findProperty("BASE_URL_GOOGLE")
val baseUrlRoutesApi = findProperty("BASE_URL_ROUTES_API")
val baseUrlRoadsApi = findProperty("BASE_URL_ROADS")
val baseUrlShareTrip = findProperty("BASE_URL_SHARE_TRIP")
val clientId = findProperty("GOOGLE_CLIENT_ID")
val labelApp = findProperty("LABEL_APP")
val brokerIp = findProperty("BROKER_IP")
val brokerClientId = findProperty("BROKER_CLIENT_ID")
val brokerUsername = findProperty("BROKER_USERNAME")
val brokerPassword = findProperty("BROKER_PASSWORD")

configurations.all {
    resolutionStrategy {
        force(libs.androidx.appcompat)
        force(libs.androidx.appcompatResource)
    }
}

android {
    namespace = "com.example.livetracking"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.livetracking"
        minSdk = 21
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
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            manifestPlaceholders["MAPS_API_KEYS"] = "$mapsApiKeys"
            manifestPlaceholders["LABEL"] = "$labelApp"
            buildConfigField("String", "MAPS_API_KEYS", "\"${mapsApiKeys}\"")
            buildConfigField("String", "BASE_URL_GOOGLE", "\"$baseUrlGoogle\"")
            buildConfigField("String", "BASE_URL_ROUTES_API", "\"$baseUrlRoutesApi\"")
            buildConfigField("String", "BASE_URL_ROADS", "\"$baseUrlRoadsApi\"")
            buildConfigField("String", "GOOGLE_CLIENT_ID", "\"$clientId\"")
            buildConfigField("String", "BASE_URL_SHARE_TRIP", "\"$baseUrlShareTrip\"")
            buildConfigField("String", "BROKER_IP", "\"$brokerIp\"")
            buildConfigField("String", "BROKER_USERNAME", "\"$brokerUsername\"")
            buildConfigField("String", "BROKER_CLIENT_ID", "\"$brokerClientId\"")
            buildConfigField("String", "BROKER_PASSWORD", "\"$brokerPassword\"")
        }
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            manifestPlaceholders["MAPS_API_KEYS"] = "$mapsApiKeys"
            manifestPlaceholders["LABEL"] = "$labelApp"
            buildConfigField("String", "MAPS_API_KEYS", "\"${mapsApiKeys}\"")
            buildConfigField("String", "BASE_URL_GOOGLE", "\"$baseUrlGoogle\"")
            buildConfigField("String", "BASE_URL_ROUTES_API", "\"$baseUrlRoutesApi\"")
            buildConfigField("String", "BASE_URL_ROADS", "\"$baseUrlRoadsApi\"")
            buildConfigField("String", "GOOGLE_CLIENT_ID", "\"$clientId\"")
            buildConfigField("String", "BASE_URL_SHARE_TRIP", "\"$baseUrlShareTrip\"")
            buildConfigField("String", "BROKER_IP", "\"$brokerIp\"")
            buildConfigField("String", "BROKER_USERNAME", "\"$brokerUsername\"")
            buildConfigField("String", "BROKER_CLIENT_ID", "\"$brokerClientId\"")
            buildConfigField("String", "BROKER_PASSWORD", "\"$brokerPassword\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/gradle/incremental.annotation.processors"
            excludes += listOf("META-INF/INDEX.LIST", "META-INF/io.netty.versions.properties")
        }
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(libs.courier.courier)
    implementation(libs.courier.gson)
    implementation(libs.courier.stream)
    implementation(libs.courier.ping)
    implementation(libs.courier.chucker)
    implementation(libs.jakewharton.timber)
    implementation(libs.joda.time)
    implementation(platform(libs.squareup.okhttp3Bom))
    implementation(libs.squareup.okhttp)
    implementation(libs.squareup.logging)
    implementation(libs.squareup.gson)
    implementation(libs.androidx.uiUtil)
    implementation(libs.google.gson)
    implementation(libs.google.playServiceAuth)
    implementation(libs.google.composeMapsUtils)
    implementation(libs.google.utilsKtx)
    implementation(libs.google.utils)
    implementation(libs.google.mapsKtx)
    implementation(libs.androidx.multidex)
    implementation(libs.google.systemUiController)
    implementation(libs.google.placeHolderMaterial)
    implementation(libs.androidx.hiltNavigationCompose)
    implementation(libs.google.playServiceLocation)
    implementation(libs.androidx.roomRuntime)
    annotationProcessor(libs.androidx.roomCompiler)
    implementation(libs.androidx.roomKtx)
    kapt(libs.androidx.roomCompiler)
    implementation(libs.androidx.ktx)
    implementation(libs.androidx.lifecycleRuntimeKtx)
    implementation(libs.androidx.lifecycleRuntimeCompose)
    implementation(libs.androidx.navigationCompose)
    implementation(libs.androidx.runtimeLiveData)
    implementation(libs.coil.compose)
    implementation(libs.squareup.retrofit)
    implementation(libs.google.hiltAndroid)
    implementation(libs.google.hiltCompoiler)
    implementation(libs.jetbrains.kotlinxCoroutineAndroid)
    implementation(libs.androidx.activityCompose)
    implementation(libs.androidx.composeUi)
    implementation(libs.androidx.uiToolingPreview)
    implementation(libs.androidx.material3)
    implementation(libs.google.mapsCompose)
    implementation(libs.google.composeMapsUtils)
    implementation(libs.google.composeMapsWidgets)
    implementation(libs.google.place)
    implementation(libs.google.placeKtx)
    implementation(libs.google.playServiceMaps)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espressoCore)
    androidTestImplementation(libs.androidx.uiTestJunit)
    debugImplementation(libs.androidx.uiTooling)
    debugImplementation(libs.androidx.uiTestManifest)
}