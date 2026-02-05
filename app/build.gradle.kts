plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("ru.practicum.android.diploma.plugins.developproperties")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp") version "1.9.22-1.0.17"
}

android {
    namespace = "ru.practicum.android.diploma"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "ru.practicum.android.diploma"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(type = "String", name = "API_ACCESS_TOKEN", value = "\"${developProperties.apiAccessToken}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }
}

dependencies {
    implementation("com.airbnb.android:lottie-compose:6.4.0")
    implementation(libs.converter.gson)
    implementation(libs.material)
    implementation(libs.navigation.compose)
    implementation(libs.androidX.core)
    implementation(libs.androidX.appCompat)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.activity.ktx)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.glide)
    implementation(libs.koin.android)
    implementation("io.insert-koin:koin-androidx-compose:3.5.0")
    implementation(libs.navigation.compose)
    implementation(libs.material3)
    implementation(libs.coil.compose)
    implementation(libs.ui.tooling.preview)
    implementation(libs.foundation.layout)
    implementation(libs.ui.graphics)
    implementation(libs.foundation)
    implementation(libs.androidx.foundation.layout)
    implementation(libs.androidx.junit.ktx)
    debugImplementation(libs.ui.tooling)
    ksp(libs.compiler)
    ksp(libs.room.compiler)

    // UI layer libraries
    implementation(libs.ui.material)
    implementation(libs.ui.constraintLayout)
    implementation(libs.ui)
    implementation(libs.material)
    implementation(libs.activity.compose)

    // region Unit tests
    testImplementation(libs.unitTests.junit)
    // endregion

    // region UI tests
    androidTestImplementation(libs.uiTests.junitExt)
    androidTestImplementation(libs.uiTests.espressoCore)
    // endregion
}
