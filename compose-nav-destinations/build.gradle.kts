plugins {
    id("com.android.library")
    id("kotlin-android")
    //TODO -> Entfernen und Selbst schreiben
    id("kotlin-parcelize")
    kotlin("plugin.serialization") version Versions.kotlinSerialisationPluginVersion

    id("maven-publish")
}

group = "com.github.LuWe98"

android {
    compileSdk = Versions.compileSdk
    namespace = "com.welu.composenavdestinations"

    defaultConfig {
        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles.add(File("consumer-rules.pro"))
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.composeCompilerExtensionVersion
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

//    lint {
//        checkDependencies = true
//        textReport = true
//        abortOnError = true
//        ignoreWarnings = false
//        // Produce report for CI:
//        // https://docs.github.com/en/github/finding-security-vulnerabilities-and-errors-in-your-code/sarif-support-for-code-scanning
//        sarifOutput = file("../lint-results.sarif")
//    }
}

dependencies {
//    implementation(project(":compose-nav-destinations-lint"))
//    lintPublish(project(":compose-nav-destinations-lint"))

    //ComposeNavigation
    implementation("androidx.navigation:navigation-compose:${Versions.composeNavigationVersion}")

    //Accompanist
    implementation("com.google.accompanist:accompanist-navigation-animation:${Versions.accompanistVersion}")
    implementation("com.google.accompanist:accompanist-navigation-material:${Versions.accompanistVersion}")

    //Ktx Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.ktxSerializableVersion}")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.LuWe98"
            artifactId = "core"
            version = "1.0.0"

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}