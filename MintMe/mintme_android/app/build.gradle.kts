plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.official.mintme"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.official.mintme"
        minSdk = 24
        targetSdk = 34
        versionCode = 3
        versionName = "1.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file("/keystore/mintme.jks")
            storePassword = ""
            keyAlias = "Mintme"
            keyPassword = ""
        }
        create("release") {
            storeFile = file("/keystore/mintme.jks")
            storePassword = ""
            keyAlias = "Mintme"
            keyPassword = ""
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isDebuggable = false
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.app.update)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}