plugins {
    id("java-library")
    id("kotlin")
    id("com.android.lint")
}

java {
    sourceCompatibility = Versions.sourceCompatibility
    targetCompatibility = Versions.targetCompatibility
}

lint {
    htmlReport =  true
    htmlOutput = file("lint-report.html")
    textReport = true
    absolutePaths = false
    ignoreTestSources = true
}

dependencies {
    //compileOnly(project(":compose-nav-destinations-shared"))

    compileOnly("com.android.tools.lint:lint-api:${Versions.lintVersion}")
    compileOnly("com.android.tools.lint:lint-checks:${Versions.lintVersion}")

    compileOnly("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlinVersion}")
}