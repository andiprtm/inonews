import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

fun loadProperties(): Properties {
    val localProperties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localProperties.load(localPropertiesFile.inputStream())
    }
    return localProperties
}

val localProperties = loadProperties()

android {
    namespace = "com.cpp.inonews"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.cpp.inonews"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "NEWS_API", "\"${localProperties.getProperty("news_api", "")}\"")
    }

    signingConfigs {
        create("release") {
            storeFile = file(localProperties.getProperty("keystore.file", ""))
            storePassword = localProperties.getProperty("keystore.password", "")
            keyAlias = localProperties.getProperty("keystore.alias", "")
            keyPassword = localProperties.getProperty("keystore.key_password", "")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
            buildConfigField("String", "BASE_URL", "\"${localProperties.getProperty("base_url", "")}\"")
        }
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = ".debug"
            buildConfigField("String", "BASE_URL", "\"${localProperties.getProperty("dev_base_url", "")}\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures{
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    // Core & UI
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.splashscreen)

    // Architecture (MVVM essentials)
    implementation(libs.bundles.architecture)

    // Navigation
    implementation(libs.bundles.navigation)

    // Network (Retrofit + Gson + OkHttp)
    implementation(libs.bundles.network)

    // Image Loading (Glide)
    implementation(libs.bundles.imageloading)

    // Data Persistence (DataStore + Coroutines)
    implementation(libs.bundles.persistence)

    // Paging
    implementation(libs.androidx.paging.runtime.ktx)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core)

}