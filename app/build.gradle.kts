plugins {
    id(Plugins.androidApp)
    id(Plugins.kotlinAndroidApp)
    kotlin(Plugins.kapt)
}

android {
    buildToolsVersion = Config.androidBuildTools
    compileSdkVersion(Config.androidCompileSdk)

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

        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
//    implementation(Libs.room)
//    implementation(Libs.roomKtx) // needed by coroutines
    implementation(Libs.fragmentKtx)
//    kapt(Libs.roomCompiler)

    implementation(Libs.adapterDelegates)
    implementation(Libs.glide)
    kapt(Libs.glideCompiler)
    kapt(Libs.moshiCodeGen) // migrate to kotlin serialization - better for multiplatform
    implementation(Libs.glideOkhttp)
    implementation(Libs.glideRecyclerView)
    implementation(Libs.retrofit)
    implementation(Libs.okhttp)
    implementation(Libs.okhttpLogging)
    implementation(Libs.timber)
//    implementation(Libs.workManager)
    implementation(Libs.moshiAdapters)
    implementation(Libs.retrofitConverter)
    implementation(Libs.viewBindingProperty)
    implementation(Libs.dagger)
    kapt(Libs.daggerProcessor)

    testImplementation(Libs.junit)
    testImplementation(Libs.junitEngine)
    testImplementation(Libs.junitParams)
    testImplementation(Libs.mockito)
    testImplementation(Libs.mockitoKotlin)
}