plugins {
    id(Plugins.androidApp)
    id(Plugins.kotlinAndroidApp)
    id(Plugins.googleServicesPlugin)
    id(Plugins.crashlyticsPlugin)
    kotlin(Plugins.kapt)
}

android {
    buildToolsVersion = Config.androidBuildTools
    compileSdkVersion(Config.androidCompileSdk)

    signingConfigs {
        getByName("debug") {
            keyAlias = "debug-key"
            keyPassword = "bynthdm."
            storeFile = file("debug-keystore.jks")
            storePassword = "bynthdm."
        }
    }
    defaultConfig {
        applicationId = Config.applicationId
        minSdkVersion(Config.androidMinSdk)
        targetSdkVersion(Config.androidTargetSdk)
        versionCode = Config.versionCode
        versionName = Config.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            signingConfig = signingConfigs["debug"]
            isDebuggable = true
            buildConfigField("String", "BASE_URL", """"https://yasen.hotellook.com/"""")
        }
    }
    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(Libs.appcompat)
    implementation(Libs.material)
    implementation(Libs.constraintLayout)
    implementation(Libs.coroutines)
    implementation(Libs.lifecycle)
    implementation(Libs.viewModel)
    implementation(Libs.fragmentKtx)
    platform(Libs.firebase)
    implementation(Libs.crashlytics)

    implementation(Libs.adapterDelegates)
    kapt(Libs.moshiCodeGen)
    implementation(Libs.retrofit)
    implementation(Libs.okhttp)
    implementation(Libs.okhttpLogging)
    implementation(Libs.timber)
    implementation(Libs.moshiAdapters)
    implementation(Libs.retrofitConverter)
    implementation(Libs.viewBindingProperty)
    implementation(Libs.dagger)
    kapt(Libs.daggerProcessor)
    implementation(Libs.cicerone)
    implementation(Libs.playServicesMaps)
    implementation(Libs.mapsUtils)

    testImplementation(Libs.junit)
    testImplementation(Libs.junitEngine)
    testImplementation(Libs.junitParams)
    testImplementation(Libs.mockito)
    testImplementation(Libs.mockitoKotlin)
}