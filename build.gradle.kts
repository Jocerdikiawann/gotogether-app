buildscript {
}// Top-level build file where you can add configuration options common to all sub-projects/modules.

ext["grpcVersion"] = "1.54.1"
ext["grpcKotlinVersion"] = "1.3.0" // CURRENT_GRPC_KOTLIN_VERSION
ext["protobufVersion"] = "3.22.3"
ext["coroutinesVersion"] = "1.7.0"

plugins {
    id ("com.android.application") version "7.4.2" apply false
    id ("com.android.library") version "7.4.2" apply false
    id ("org.jetbrains.kotlin.android") version "1.7.0" apply false
    id ("org.jetbrains.kotlin.jvm") version "1.8.0" apply false
    id("com.google.dagger.hilt.android") version "2.44" apply false
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
    id("com.google.protobuf") version "0.9.3" apply false
}