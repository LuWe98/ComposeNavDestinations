pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ComposeNavDestinations"

include(":app")
include(":compose-nav-destinations")
include(":compose-nav-destinations-ksp")
include(":compose-nav-destinations-lint")
include(":compose-nav-destinations-shared")