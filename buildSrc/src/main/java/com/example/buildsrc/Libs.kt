import org.gradle.kotlin.dsl.provideDelegate

object Libs {
    private const val composeVersion = "1.3.3"
    private const val roomVersion = "2.5.0"

    object AndroidX {
        val androidxCore by lazy { "androidx.core:core-ktx:1.7.0" }
        val androidXLifeCycleRuntime by lazy { "androidx.lifecycle:lifecycle-runtime-ktx:2.3.1" }
        val navigation by lazy { "androidx.navigation:navigation-compose:2.5.3" }
        val multidex by lazy { "androidx.multidex:multidex:2.0.1" }

        object Compose {
            val ui by lazy { "androidx.compose.ui:ui:$composeVersion" }
            val toolingpreview by lazy { "androidx.compose.ui:ui-tooling-preview:$composeVersion" }
            val activityCompose by lazy { "androidx.activity:activity-compose:1.3.1" }
            val materialTree by lazy { "androidx.compose.material3:material3:1.0.0-alpha11" }
            val uiTooling by lazy { "androidx.compose.ui:ui-tooling:$composeVersion" }
            val uiTestManifest by lazy { "androidx.compose.ui:ui-test-manifest:$composeVersion" }
            val liveData by lazy { "androidx.compose.runtime:runtime-livedata:1.3.2" }
            val util by lazy {"androidx.compose.ui:ui-util:1.3.3"}
        }

        object Test {
            val junit by lazy { "androidx.test.ext:junit:1.1.5" }
            val espresso by lazy { "androidx.test.espresso:espresso-core:3.5.1" }
            val composeTestJunit by lazy { "androidx.compose.ui:ui-test-junit4:$composeVersion" }
        }

        object Room {
            val roomRuntime by lazy { "androidx.room:room-runtime:$roomVersion" }
            val roomCompiler by lazy { "androidx.room:room-compiler:$roomVersion" }
            val roomKtx by lazy { "androidx.room:room-ktx:$roomVersion" }
        }
    }

    object SquareUp {
        val retrofit by lazy { "com.squareup.retrofit2:retrofit:2.9.0" }
        val okhttpBOM by lazy { "com.squareup.okhttp3:okhttp-bom:4.10.0" }
        val okhttp by lazy { "com.squareup.okhttp3:okhttp" }
        val logging by lazy { "com.squareup.okhttp3:logging-interceptor" }
    }

    object Google {
        val gson by lazy { "com.google.code.gson:gson:2.10.1" }
        val gsonConverter by lazy { "com.squareup.retrofit2:converter-gson:2.9.0" }
        val dagger by lazy { "com.google.dagger:hilt-android:2.44" }
        val hiltNavigationCompose by lazy { "androidx.hilt:hilt-navigation-compose:1.0.0" }
        val daggerCompiler by lazy { "com.google.dagger:hilt-android-compiler:2.44" }
        val systemUi by lazy { "com.google.accompanist:accompanist-systemuicontroller:0.29.1-alpha" }
        val shimmer by lazy { "com.google.accompanist:accompanist-placeholder-material:0.29.1-alpha" }
        val location by lazy { "com.google.android.gms:play-services-location:21.0.1" }
    }

    object Coil {
        val coil by lazy { "io.coil-kt:coil-compose:1.4.0" }
    }

    object Kotlinx {
        val coroutine by lazy { "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9" }
    }

    object Maps {
        val mapsCompose by lazy { "com.google.maps.android:maps-compose:2.10.0" }
        val utils by lazy { "com.google.maps.android:android-maps-utils:3.4.0" }
        val playServiceMaps by lazy { "com.google.android.gms:play-services-maps:18.1.0" }
        val place by lazy { "com.google.android.libraries.places:places:3.0.0" }
        val placeKtx by lazy { "com.google.maps.android:places-ktx:0.4.0" }
    }

    object Joda {
        val time by lazy {"joda-time:joda-time:2.12.2"}
    }

    object Junit {
        val junit by lazy { "junit:junit:4.13.2" }
    }
}