import com.android.build.gradle.internal.packaging.createDefaultDebugStore

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("androidx.navigation.safeargs")

}

android {
    namespace = "com.app.indianic"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.app.indianic"
        minSdk = 24
        targetSdk = 33
        var versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField(
                "String",
                "SECRET_KEY",
                "\"rIaBLeTiMeNdRaRanEpONtRaptIVerti\""
            )
            buildConfigField(
                "String",
                "SECRET_IV_KEY",
                "\"GhtErEmPkINdeolm\""
            )
        }
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
            isDebuggable = true
        }
        debug {
            signingConfig = signingConfigs.getByName("debug")
            isDebuggable = true
            //isMinifyEnabled = true
            buildConfigField("Boolean", "DEBUG_MODE", "true")
            buildConfigField(
                "String",
                "SECRET_KEY",
                "\"rIaBLeTiMeNdRaRanEpONtRaptIVerti\""
            )
            buildConfigField(
                "String",
                "SECRET_IV_KEY",
                "\"GhtErEmPkINdeolm\""
            )
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
        viewBinding = true
        buildConfig = true
    }

   signingConfigs {
       getByName("debug") {
           keyAlias = "dev_keystore"
           keyPassword = "dev_keystore"
           storeFile = file("../dev_keystore.jks")
           storePassword = "dev_keystore"
       }
       create("release") {
           keyAlias = "dev_keystore"
           keyPassword = "dev_keystore"
           storeFile = file("../dev_keystore.jks")
           storePassword = "dev_keystore"
       }
   }


}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("com.google.dagger:hilt-android:2.48.1")
    implementation("com.google.android.libraries.places:places:3.2.0")
    implementation("com.google.android.gms:play-services-identity:18.0.1")
    implementation("com.google.firebase:firebase-auth:22.3.0")
    implementation("com.google.firebase:firebase-messaging-ktx:23.3.1")
    kapt("com.google.dagger:hilt-compiler:2.48.1")

    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")

    implementation("androidx.navigation:navigation-fragment-ktx:2.7.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.3")
    implementation("androidx.navigation:navigation-common:2.7.3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.11")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.11")
    implementation("com.google.android.gms:play-services-maps:18.0.1")
    implementation("com.google.android.gms:play-services-location:19.0.0")
    implementation("com.google.android.libraries.places:places:3.3.0")
    implementation("com.google.android.gms:play-services-auth:18.1.0")
    implementation("com.google.firebase:firebase-messaging")
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-crashlytics")


}


kapt {
    correctErrorTypes = true
}
