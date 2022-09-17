plugins {
    id("com.android.library")
    id("kotlin-android")
    //TODO -> Entfernen und Selbst schreiben
    id("kotlin-parcelize")
}

android {
    compileSdk = 33

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


//    lint {
//        checkDependencies = true
//        textReport = true
//        // Produce report for CI:
//        // https://docs.github.com/en/github/finding-security-vulnerabilities-and-errors-in-your-code/sarif-support-for-code-scanning
//        sarifOutput = file("../lint-results.sarif")
//    }
}

dependencies {
//    api(project(":compose-nav-destinations-lint"))
//    lintPublish(project(":compose-nav-destinations-lint"))

    implementation("androidx.navigation:navigation-compose:2.5.2")
}