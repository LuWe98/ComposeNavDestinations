//plugins {
//    id("com.android.application") version "7.2.1" apply false
//    id("com.android.library") version "7.2.1" apply false
//    id("org.jetbrains.kotlin.android") version "1.7.10" apply false
//    id("org.jetbrains.kotlin.jvm") version "1.7.10" apply false
//}

buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.2.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.7.10")
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}