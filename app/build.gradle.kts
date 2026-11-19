import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.0"
}

android {
    namespace = "com.example.newsapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.newsapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }

        val properties = Properties()

        val localProperties =
            rootProject.file("local.properties")

        if (localProperties.exists()) {
            localProperties.inputStream().use { stream ->
                properties.load(stream)
            }
        }

        val apiKey =
            properties.getProperty("API_KEY") ?: ""

        buildConfigField(
            type = "String",
            name = "API_KEY",
            value = "\"$apiKey\""
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility =
            JavaVersion.VERSION_1_8

        targetCompatibility =
            JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
        viewBinding = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.runtime.android)
    implementation(libs.androidx.storage)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.filament.android)
    implementation(libs.protolite.well.known.types)
    implementation(libs.volley)

    androidTestImplementation(
        platform(libs.androidx.compose.bom)
    )

    androidTestImplementation(
        libs.androidx.ui.test.junit4
    )

    val appcompatVersion = "1.7.0"

    implementation(
        "androidx.appcompat:appcompat:$appcompatVersion"
    )

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    testImplementation(libs.junit)

    androidTestImplementation(
        libs.androidx.junit
    )

    androidTestImplementation(
        libs.androidx.espresso.core
    )

    implementation(
        "com.google.android.material:material:1.9.0"
    )

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(
        libs.androidx.ui.test.manifest
    )

    implementation(
        "com.squareup.retrofit2:retrofit:2.9.0"
    )

    implementation(
        "com.squareup.retrofit2:converter-gson:2.9.0"
    )

    implementation(
        "com.github.bumptech.glide:glide:4.12.0"
    )

    annotationProcessor(
        "com.github.bumptech.glide:compiler:4.12.0"
    )

    implementation(
        "androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0"
    )

    implementation(
        "androidx.lifecycle:lifecycle-livedata-ktx:2.4.0"
    )

    implementation(
        "androidx.recyclerview:recyclerview:1.2.1"
    )

    implementation(
        "androidx.cardview:cardview:1.0.0"
    )

    implementation(
        "com.squareup.okhttp3:logging-interceptor:4.10.0"
    )

    implementation(
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1"
    )

    implementation(
        "org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0"
    )
    implementation("io.coil-kt:coil-compose:2.6.0")
}