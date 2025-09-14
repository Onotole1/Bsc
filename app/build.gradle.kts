plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "ru.bsvyazi.bsconnect"
    compileSdk = 36

    defaultConfig {
        applicationId = "ru.bsvyazi.bsconnect"
        minSdk = 22
        //noinspection EditedTargetSdkVersion
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
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
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    sourceSets {
        getByName("main") {
            java {
                srcDirs("src/main/java", "src/main/java/ru.bsvyazi.ru",
                    "src/main/kotlin+java/retrofit", "src/main/java/2"
                )
            }
            aidl {
                srcDirs("src/main/aidl")
            }
        }
    }
}

buildscript {
    dependencies {
        // Consider updating this if you use a newer Hilt version in your app module
        classpath(libs.hilt.android.gradle.plugin) // This uses 2.44
    }
}

dependencies {
    implementation(libs.androidx.activity)

    val appcompatVersion = "1.7.1"
    val mdcVersion = "1.13.0"
    val constraintlayoutVersion = "2.2.1"
    val recyclerviewVersion = "1.4.0"
    val junitVersion = "4.13.2"
    val extJunitVersion = "1.2.1"
    val espressoCoreVersion = "3.6.1"
    val activityVersion = "1.11.0"
    val lifecycleVersion = "2.9.3"
    val gson_version = "2.13.2"
    val retrofit_version = "3.0.0"
    val okhttp_version = "5.1.0"
    val core_version = "1.16.0"
    val work_runtime_version = "2.10.4"
    val hilt_version = "2.56.2"

    implementation("com.google.code.gson:gson:$gson_version")
    implementation("com.squareup.okhttp3:okhttp:$okhttp_version")
    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit_version")
    implementation(libs.androidx.core.ktx)
    implementation("androidx.appcompat:appcompat:$appcompatVersion")
    implementation("com.google.android.material:material:$mdcVersion")
    implementation("androidx.constraintlayout:constraintlayout:$constraintlayoutVersion")
    implementation("androidx.recyclerview:recyclerview:$recyclerviewVersion")
    implementation("androidx.activity:activity-ktx:$activityVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("com.google.code.gson:gson:$gson_version")
    implementation("androidx.work:work-runtime-ktx:$work_runtime_version")
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttp_version")
    implementation(libs.hilt.android)
    kapt (libs.hilt.android.compiler)
    testImplementation("junit:junit:$junitVersion")
    androidTestImplementation("androidx.test.ext:junit:$extJunitVersion")
    androidTestImplementation("androidx.test.espresso:espresso-core:$espressoCoreVersion")

}