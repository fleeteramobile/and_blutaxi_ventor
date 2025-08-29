plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.draggerHilt)
    alias(libs.plugins.kotlinKapt)
    alias(libs.plugins.ksp) apply false


}

android {
    namespace = "com.blueventor"
    compileSdk = 34
    flavorDimensions += "environment"

    productFlavors {
        create("dev")
        {
            dimension = "environment"
            applicationIdSuffix = ".dev"

            buildConfigField(
                "String",
                "BASE_URL",
                "\"https://devbluetaxi.fleetera.io/vendorapi201/index/\""
            )
            buildConfigField("String", "API_KEY", "\"devbluetaxi\"")
            buildConfigField("String", "COMPANY_MAIN_DOMAIN", "\"devbluetaxi\"")
            buildConfigField("String", "Flavors", "\"dev\"")

        }

        create("uat")
        {
            dimension = "environment"
            applicationIdSuffix = ".uat"

            buildConfigField(
                "String",
                "BASE_URL",
                "\"https://uatbluetaxi.fleetera.io/vendorapi201/index/\""
            )
            buildConfigField("String", "API_KEY", "\"uatbluetaxi\"")
            buildConfigField("String", "COMPANY_MAIN_DOMAIN", "\"devbluetaxi\"")
            buildConfigField("String", "Flavors", "\"uat\"")


        }

        create("qatesting")
        {
            dimension = "environment"
            applicationIdSuffix = ".qatesting"

            buildConfigField(
                "String",
                "BASE_URL",
                "\"https://testingonewaytriptaxi.ardhas.com/vendorapi201/index/\""
            )
            buildConfigField("String", "API_KEY", "\"testingonewaytriptaxi\"")
            buildConfigField("String", "COMPANY_MAIN_DOMAIN", "\"devbluetaxi\"")
            buildConfigField("String", "Flavors", "\"testing\"")


        }
        create("production")
        {
            dimension = "environment"
            buildConfigField(
                "String",
                "BASE_URL",
                "\"https://onewaytriptaxi.com/vendorapi201/index/\""
            )
            buildConfigField("String", "API_KEY", "\"liveonewaytriptaxi\"")
            buildConfigField("String", "COMPANY_MAIN_DOMAIN", "\"devbluetaxi\"")
            buildConfigField("String", "Flavors", "\"live\"")

        }

    }

    defaultConfig {
        applicationId = "com.blueventor"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
buildFeatures{
    buildConfig = true
    viewBinding = true
}
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"

        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    viewBinding {
        enable = true
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.lifecycle.extensions)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.logging.interceptor)
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.coroutines.android)
    implementation(libs.coroutines.core)
    implementation(libs.glide)
    kapt(libs.glide.compiler)


}