plugins {
    kotlin("android")
    kotlin("plugin.serialization") version Versions.kotlinSerialisationPluginVersion
    id("com.android.application")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp") version Versions.kspVersion
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
}

android {
    compileSdk = Versions.compileSdk
    namespace = "com.welu.composenavdestinations.app"

    defaultConfig {
        applicationId = "com.welu.composenavdestinations.app"
        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdk
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = Versions.sourceCompatibility
        targetCompatibility = Versions.targetCompatibility
    }

    kotlinOptions {
        jvmTarget = Versions.jvmTarget
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.composeCompilerExtensionVersion
    }

    packaging {
        resources.excludes.add("/META-INF/{AL2.0,LGPL2.1}")
    }
    
    //Das ist nötig um die generated KSP files zu sehen
    applicationVariants.all {
        kotlin.sourceSets {
            getByName(name) {
                kotlin.srcDir("build/generated/ksp/$name/kotlin")
            }
        }
    }

//    lint {
//        checkDependencies = true
//        textReport = true
//        // Produce report for CI:
//        // https://docs.github.com/en/github/finding-security-vulnerabilities-and-errors-in-your-code/sarif-support-for-code-scanning
//        sarifOutput = file("../lint-results.sarif")
//    }
}

dependencies {
    implementation(project(":compose-nav-destinations"))
    ksp(project(":compose-nav-destinations-ksp"))

    implementation("androidx.core:core-ktx:${Versions.anndroidCoreVersion}")
    implementation("androidx.compose.ui:ui:${Versions.composeVersion}")
    implementation("androidx.compose.material:material:${Versions.composeVersion}")
    implementation("androidx.compose.ui:ui-tooling-preview:${Versions.composeVersion}")
    implementation("androidx.activity:activity-compose:${Versions.composeActivityVersion}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${Versions.androidLifecycleVersion}")

    //Navigation
    implementation("androidx.navigation:navigation-compose:${Versions.composeNavigationVersion}")

    //Accompanist
    implementation("com.google.accompanist:accompanist-navigation-material:${Versions.accompanistVersion}")

    //Ktx Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.ktxSerializableVersion}")

    //Tests
    testImplementation("junit:junit:${Versions.jUnitVersion}")
    androidTestImplementation("androidx.test.ext:junit:${Versions.androidJUnitVersion}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${Versions.androidEspressoVersion}")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${Versions.composeVersion}")
    debugImplementation("androidx.compose.ui:ui-tooling:${Versions.composeVersion}")
    debugImplementation("androidx.compose.ui:ui-test-manifest:${Versions.composeVersion}")
}