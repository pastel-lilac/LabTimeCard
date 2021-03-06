object Libs {
    object Versions {
        const val kotlin = "1.3.61"
        const val kotlinx = "1.3.3"
        const val coreKtx = "1.1.0"
        const val appcompat = "1.1.0"
        const val firebaseAnalyticsVersion = "17.2.2"
        const val firebaseDatabaseVersion = "19.2.0"
        const val fireabaseAuthenticationVersion = "19.2.0"
        const val firebaseUiAuth = "6.2.0"
        const val epoxy = "3.8.0"
        const val constraintLayout = "2.0.0-beta4"
        const val material = "1.0.0"
        const val lifecycle = "2.2.0"
        const val legacy = "1.0.0"
        const val junit = "4.12"
        const val androidJunit = "1.1.1"
        const val espresso = "3.2.0"
        const val butterKnifePlugin = "10.1.0"
        const val koin = "2.0.1"
        const val timber = "4.7.1"
        const val okHttp = "4.2.1"
        const val stetho = "1.5.1"
        const val retrofit = "2.6.2"
        const val moshi = "1.9.1"
        const val coil = "0.9.5"
        const val threeTen = "1.2.2"
        const val calendar = "0.3.2"
    }

    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    const val kotlinx = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinx}"
    const val kotlinxAndroid =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinx}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val coroutinePlayServices =
        "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:${Versions.kotlinx}"


    const val firebaseAnalytics =
        "com.google.firebase:firebase-analytics:${Versions.firebaseAnalyticsVersion}"
    const val firebaseDatabase =
        "com.google.firebase:firebase-database:${Versions.firebaseDatabaseVersion}"
    const val firebaseAuthentication =
        "com.google.firebase:firebase-auth:${Versions.fireabaseAuthenticationVersion}"
    const val firebaseUiAuth = "com.firebaseui:firebase-ui-auth:${Versions.firebaseUiAuth}"


    const val epoxy = "com.airbnb.android:epoxy:${Versions.epoxy}"
    const val epoxyApt = "com.airbnb.android:epoxy-processor:${Versions.epoxy}"
    const val epoxyDatabinding = "com.airbnb.android:epoxy-databinding:${Versions.epoxy}"


    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val lifecycle = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle}"
    const val lifecycleViewModel =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    const val legacy = "androidx.legacy:legacy-support-v4:${Versions.legacy}"

    const val junit = "junit:junit:${Versions.junit}"
    const val androidJunit = "androidx.test.ext:junit:${Versions.androidJunit}"
    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"

    const val butterKnifePlugin =
        "com.jakewharton:butterknife-gradle-plugin:${Versions.butterKnifePlugin}"

    const val koinAndroid = "org.koin:koin-android:${Versions.koin}"
    const val koinAndroidxScope = "org.koin:koin-androidx-scope:${Versions.koin}"
    const val koinViewModel = "org.koin:koin-androidx-viewmodel:${Versions.koin}"

    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"

    const val okHttp = "com.squareup.okhttp3:okhttp:${Versions.okHttp}"
    const val stetho = "com.facebook.stetho:stetho-okhttp3:${Versions.stetho}"
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val moshi = "com.squareup.moshi:moshi-kotlin:${Versions.moshi}"
    const val retrofitMoshiConverter = "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}"

    const val coil = "io.coil-kt:coil:${Versions.coil}"

    const val threeTebAbp = "com.jakewharton.threetenabp:threetenabp:${Versions.threeTen}"
    const val calendar = "com.github.kizitonwose:CalendarView:${Versions.calendar}"
}