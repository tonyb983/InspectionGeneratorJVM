pluginManagement {
  plugins {
    idea
    application
    kotlin("jvm") version "1.4.20"
    //id("org.jetbrains.kotlin.plugin.kotlin-sam-with-receiver") version "1.4.20"
    id("org.jetbrains.kotlin.plugin.noarg") version "1.4.20"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.4.20"
    kotlin("kapt") version "1.4.20"
    id("org.openjfx.javafxplugin") version "0.0.9"

    id("io.kotest") version "0.2.6"
    //id("com.bnorm.power.kotlin-power-assert") version "0.6.1"
    id("dev.ahmedmourad.nocopy.nocopy-gradle-plugin") version "1.2.0"
    // See https://github.com/ansman/auto-plugin
    // This should probably be moved to the buildSrc module if anything.
    // Also see https://github.com/google/ksp
    // and https://github.com/tschuchortdev/kotlin-compile-testing
    id("se.ansman.autoplugin") version "0.4.1"

    id("org.jetbrains.dokka") version "1.4.20"
    `build-dashboard`
  }
  // resolutionStrategy {  }
  repositories {
    gradlePluginPortal()
    mavenCentral()
    jcenter()
    maven(url = "https://dl.bintray.com/kotlin/kotlinx")
    maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
    maven(url = "https://dl.bintray.com/kotlin/kotlin-plugin/")
    maven(url = "https://kotlin.bintray.com/kotlin-dependencies")
    maven(url = "https://jetbrains.bintray.com/intellij-third-party-dependencies/")
  }
}
rootProject.name = "InspectionGeneratorJVM"

