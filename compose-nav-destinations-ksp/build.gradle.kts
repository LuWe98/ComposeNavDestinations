plugins {
    kotlin("jvm")
    id("maven-publish")
}

group = "com.github.LuWe98"

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
}

java {
    sourceCompatibility = JavaVersion.VERSION_16
    targetCompatibility = JavaVersion.VERSION_16
}

dependencies {
    //api(project(":compose-nav-destinations-shared"))
    implementation("com.google.devtools.ksp:symbol-processing-api:1.7.20-1.0.6")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.LuWe98"
            artifactId = "ksp"
            version = "1.0.0"

            afterEvaluate {
                from(components["java"])
            }
        }
    }
}