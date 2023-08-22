@file:Suppress("UnstableApiUsage")
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
    versionCatalogs {
        create("libs"){
            from(files("libs.versions.toml"))
        }
    }
}
rootProject.name = "LiveTracking"
include(":app")
