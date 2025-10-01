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
        create("uat")
        {
            dimension = "environment"
            applicationIdSuffix = ".uat"

            buildConfigField(
                "String",
                "BASE_URL",
                "\"https://devbluetaxi.fleetera.io/vendorapi201/index/\""
            )
            buildConfigField("String", "API_KEY", "\"uatbluetaxi\"")
            buildConfigField("String", "COMPANY_MAIN_DOMAIN", "\"devbluetaxi\"")
            buildConfigField("String", "Flavors", "\"uat\"")


        }

        create("dev")
        {
            dimension = "environment"
            applicationIdSuffix = ".dev"

            buildConfigField(
                "String",
                "BASE_URL",
                "\"https://uatbluetaxi.fleetera.io/vendorapi201/index/\""
            )
            buildConfigField("String", "API_KEY", "\"uatbluetaxi\"")
            buildConfigField("String", "COMPANY_MAIN_DOMAIN", "\"uatbluetaxi \"")
            buildConfigField("String", "Flavors", "\"dev\"")

        }



        create("qatesting")
        {
            dimension = "environment"
            applicationIdSuffix = ".qatesting"

            buildConfigField(
                "String",
                "BASE_URL",
                "\"https://testingbluetaxi.fleetera.io/vendorapi201/index/\""
            )
            buildConfigField("String", "API_KEY", "\"testingbluetaxi\"")
            buildConfigField("String", "COMPANY_MAIN_DOMAIN", "\"testingbluetaxi\"")
            buildConfigField("String", "Flavors", "\"testing\"")


        }
        create("production")
        {
            dimension = "environment"
            buildConfigField(
                "String",
                "BASE_URL",
                "\"https://https://bluetaxi.in//vendorapi201/index/\""
            )
            buildConfigField("String", "API_KEY", "\"bluetaxi\"")
            buildConfigField("String", "COMPANY_MAIN_DOMAIN", "\"bluetaxi\"")
            buildConfigField("String", "Flavors", "\"live\"")

        }

    }
    // dev - dev , uat - uatDebug
    defaultPublishConfig = "uatDebug"

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
    implementation(libs.play.services.location)
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
    implementation(libs.razorpay)


}