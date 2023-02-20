plugins {
    kotlin("jvm")
    id("maven-publish")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    //api(project(":compose-nav-destinations-shared"))

    implementation("com.google.devtools.ksp:symbol-processing-api:1.7.10-1.0.6")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                from(components["java"])
                groupId = "com.github.luwe"
                artifactId = "compose_nav_destinations_ksp"
                version = "1.0.0"
            }
        }
    }
}

//afterEvaluate {
//    publishing {
//        publications {
//            release(MavenPublication) {
//                from components.release
//
//                        groupId = 'com.github.lucaweinmann'
//                artifactId = 'android-coroutine-flow-utils'
//                version = '1.0'
//            }
//        }
//    }
//}