import org.gradle.api.JavaVersion

object Versions {
    //SDKs
    const val compileSdk = 34
    const val minSdk = 21
    const val targetSdk = compileSdk

    const val kotlinVersion = "1.9.10"
    const val gradleVersion = "8.1.2"

    //Compose
    const val composeCompilerExtensionVersion = "1.5.3"
    const val composeVersion = "1.5.3"
    const val composeNavigationVersion = "2.7.4"
    const val composeActivityVersion = "1.8.0"
    const val accompanistVersion = "0.33.2-alpha"

    const val anndroidCoreVersion = "1.12.0"
    const val androidLifecycleVersion = "2.6.2"
    
    const val kotlinSerialisationPluginVersion = kotlinVersion
    const val ktxSerializableVersion = "1.4.0"

    const val kspVersion = "1.9.10-1.0.13"

    const val kotlinPoetVersion = "1.12.0"

    const val lintVersion = "31.1.2"

    //Tests
    const val jUnitVersion = "4.13.2"
    const val androidJUnitVersion = "1.1.5"
    const val androidEspressoVersion = "3.5.1"

    //Java
    val sourceCompatibility get() = JavaVersion.VERSION_17
    val targetCompatibility get() = sourceCompatibility
    const val jvmTarget = "17"
}