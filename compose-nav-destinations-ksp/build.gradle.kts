plugins {
    kotlin("jvm")
    id("maven-publish")
}

group = "com.github.LuWe98"

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    //api(project(":compose-nav-destinations-shared"))
    implementation("com.google.devtools.ksp:symbol-processing-api:${Versions.kspVersion}")

    implementation("com.squareup:kotlinpoet:${Versions.kotlinPoetVersion}")
    implementation("com.squareup:kotlinpoet-ksp:${Versions.kotlinPoetVersion}")
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