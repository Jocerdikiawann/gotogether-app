plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("kotlin-parcelize")
    id("com.google.protobuf")
}

val mapsApiKeys = findProperty("MAPS_API_KEYS")
val baseUrlGoogle = findProperty("BASE_URL_GOOGLE")
val baseUrlRoutesApi = findProperty("BASE_URL_ROUTES_API")
val baseUrlRoadsApi = findProperty("BASE_URL_ROADS")
val clientId = findProperty("GOOGLE_CLIENT_ID")
val labelApp = findProperty("LABEL_APP")

configurations.all {
    resolutionStrategy {
        force("androidx.appcompat:appcompat:1.3.1")
        force("androidx.appcompat:appcompat-resources:1.3.1")
    }
}

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
            manifestPlaceholders["LABEL"] = "$labelApp"
            buildConfigField("String", "MAPS_API_KEYS", "\"${mapsApiKeys}\"")
            buildConfigField("String", "BASE_URL_GOOGLE", "\"$baseUrlGoogle\"")
            buildConfigField("String", "BASE_URL_ROUTES_API", "\"$baseUrlRoutesApi\"")
            buildConfigField("String", "BASE_URL_ROADS", "\"$baseUrlRoadsApi\"")
            buildConfigField("String", "GOOGLE_CLIENT_ID", "\"$clientId\"")
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

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${rootProject.ext["protobufVersion"]}"
    }
    plugins {
        create("java") {
            artifact = "io.grpc:protoc-gen-grpc-java:${rootProject.ext["grpcVersion"]}"
        }
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${rootProject.ext["grpcVersion"]}"
        }
        create("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:${rootProject.ext["grpcKotlinVersion"]}:jdk8@jar"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                create("java") {
                    option("lite")
                }
                create("grpc") {
                    option("lite")
                }
                create("grpckt") {
                    option("lite")
                }
            }
            it.builtins {
                create("kotlin") {
                    option("lite")
                }
            }
        }
    }
}

dependencies {
    runtimeOnly(Libs.Io.grpcOkHttp)
    api(Libs.Io.grpcStub)
    api(Libs.Io.protobuf)
    api(Libs.Io.kotlinStub)
    api(Libs.Io.protobufKotlin)
    implementation(Libs.Joda.time)
    implementation(platform(Libs.SquareUp.okhttpBOM))
    implementation(Libs.SquareUp.okhttp)
    implementation(Libs.SquareUp.logging)
    implementation(Libs.AndroidX.Compose.util)
    implementation(Libs.Google.gsonConverter)
    implementation(Libs.Google.auth)
    implementation(Libs.Maps.utils)
    implementation(Libs.Maps.utilsKtx)
    implementation(Libs.Maps.mapsKtx)
    implementation(Libs.AndroidX.multidex)
    implementation(Libs.Google.systemUi)
    implementation(Libs.Google.shimmer)
    implementation(Libs.Google.hiltNavigationCompose)
    implementation(Libs.Google.location)
    implementation(Libs.AndroidX.Room.roomRuntime)
    implementation(Libs.AndroidX.Room.roomKtx)
    annotationProcessor(Libs.AndroidX.Room.roomCompiler)
    kapt(Libs.AndroidX.Room.roomCompiler)
    implementation(Libs.AndroidX.androidxCore)
    implementation(Libs.AndroidX.androidXLifeCycleRuntime)
    implementation(Libs.AndroidX.lifecycleComposeRuntime)
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
    implementation(Libs.Maps.composeMapsUtils)
    implementation(Libs.Maps.composeMapsWidgets)
    implementation(Libs.Maps.place)
    implementation(Libs.Maps.placeKtx)
    implementation(Libs.Maps.playServiceMaps)
    testImplementation(Libs.Junit.junit)
    androidTestImplementation(Libs.AndroidX.Test.junit)
    androidTestImplementation(Libs.AndroidX.Test.espresso)
    androidTestImplementation(Libs.AndroidX.Test.composeTestJunit)
    debugImplementation(Libs.AndroidX.Compose.uiTooling)
    debugImplementation(Libs.AndroidX.Compose.uiTestManifest)
}