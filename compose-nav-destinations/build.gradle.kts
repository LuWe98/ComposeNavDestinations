plugins {
    id("com.android.library")
    id("kotlin-android")
    //TODO -> Entfernen und Selbst schreiben
    id("kotlin-parcelize")
    kotlin("plugin.serialization") version "1.7.10"

    id("maven-publish")
}

group = "com.github.LuWe98"

android {
    compileSdk = 33
    namespace = "com.welu.composenavdestinations"

    defaultConfig {
        minSdk = 21
        targetSdk = 33

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
        kotlinCompilerExtensionVersion = "1.3.0-beta01"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    lint {
        checkDependencies = true
        textReport = true
        abortOnError = true
        ignoreWarnings = false
        // Produce report for CI:
        // https://docs.github.com/en/github/finding-security-vulnerabilities-and-errors-in-your-code/sarif-support-for-code-scanning
        sarifOutput = file("../lint-results.sarif")
    }
}

dependencies {
//    implementation(project(":compose-nav-destinations-lint"))
//    lintPublish(project(":compose-nav-destinations-lint"))

    //ComposeNavigation
    val composeNavigationVersion = "2.5.3"
    implementation("androidx.navigation:navigation-compose:$composeNavigationVersion")

    //Accompanist
    val accompanistVersion = "0.26.4-beta"
    implementation("com.google.accompanist:accompanist-navigation-animation:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-navigation-material:$accompanistVersion")

    //Ktx Serialization
    val ktxSerializableVersion = "1.4.0"
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$ktxSerializableVersion")
}

publishing {
    publications {
        create<MavenPublication>("core") {
            groupId = "com.github.LuWe98"
            artifactId = "ComposeNavDestinations"
            version = "1.0.0"

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}