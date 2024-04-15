plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {


        signingConfigs {
        getByName("debug") {
            storeFile = file("C:\\Users\\Dell 5570\\StudioProjects\\WearNN\\WearNNKey.jks")
            storePassword = "WearNNKey!"
            keyPassword = "WearNNKey!"
            keyAlias = "WearNNKey"
        }
        create("NNKey") {
            storeFile = file("C:\\Users\\Dell 5570\\StudioProjects\\WearNN\\WearNNKey.jks")
            storePassword = "WearNNKey!"
            keyPassword = "WearNNKey!"
            keyAlias = "WearNNKey"
        }
    }
    namespace = "com.example.wearnn"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.wearnn"
        minSdk = 33
        targetSdk = 33
        versionCode = 1
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
            signingConfig = signingConfigs.getByName("NNKey")
        }
    }

    buildFeatures {
        compose = true
        viewBinding = true
        dataBinding = true
    }
    composeOptions {
        // Update the Kotlin Compiler Extension Version to match the Kotlin version needed
        kotlinCompilerExtensionVersion = "1.5.11"
    }
    kotlinOptions {
        jvmTarget = "19"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_19
        targetCompatibility = JavaVersion.VERSION_19
    }

    buildToolsVersion = "34.0.0"
    ndkVersion = "25.1.8937393"
}

dependencies {
    implementation("com.google.android.gms:play-services-wearable:18.1.0")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.play.services.wearable)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    wearApp(project(":wear"))

}