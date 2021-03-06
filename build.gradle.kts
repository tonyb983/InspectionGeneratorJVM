plugins {
  idea
  application
  kotlin("jvm")
  //id("org.jetbrains.kotlin.plugin.kotlin-sam-with-receiver")
  id("org.jetbrains.kotlin.plugin.noarg")
  id("org.jetbrains.kotlin.plugin.allopen")
  kotlin("kapt")

  id("org.openjfx.javafxplugin")

  // id("io.kotest")
  //id("com.bnorm.power.kotlin-power-assert")

  id("org.jetbrains.dokka")
  `build-dashboard`
}
group = "im.tony"
version = "1.0-SNAPSHOT"


repositories {
  mavenCentral()
  google()
  jcenter()
  maven(url = "https://dl.bintray.com/kotlin/kotlinx")
  maven(url = "https://kotlin.bintray.com/kotlinx/")
  maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
  maven(url = "https://dl.bintray.com/kotlin/kotlin-plugin/")
  maven(url = "https://kotlin.bintray.com/kotlin-dependencies")
  maven(url = "https://jetbrains.bintray.com/intellij-third-party-dependencies/")
  maven(url = "https://dl.bintray.com/arrow-kt/arrow-kt/")
  maven(url = "https://dl.bintray.com/konform-kt/konform")
  // For ArrowFx snapshot builds
  // maven(url = "https://oss.jfrog.org/artifactory/oss-snapshot-local/")
}

idea {
  module {
    sourceDirs.plusAssign(
      files(
        "build/generated/source/kapt/main",
        "build/generated/source/kapt/debug",
        "build/generated/source/kapt/release",
        "build/generated/source/kaptKotlin/main",
        "build/generated/source/kaptKotlin/debug",
        "build/generated/source/kaptKotlin/release",
        "build/tmp/kapt/main/kotlinGenerated"
      )
    )
    generatedSourceDirs.plusAssign(
      files(
        "build/generated/source/kapt/main",
        "build/generated/source/kapt/debug",
        "build/generated/source/kapt/release",
        "build/generated/source/kaptKotlin/main",
        "build/generated/source/kaptKotlin/debug",
        "build/generated/source/kaptKotlin/release",
        "build/tmp/kapt/main/kotlinGenerated"
      )
    )
  }
}

application {
  kotlin {
    // this.explicitApi = org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode.Warning
    this.coreLibrariesVersion = "1.4"

    mainClass.set("im.tony.MainKt")

    sourceSets {
      main {
        this.kotlin.srcDir("${project.projectDir}/src/main/kotlin")
        this.kotlin.excludes += Globs.Tests
        this.resources.srcDir("${project.projectDir}/src/main/resources")
        this.resources.includes += Globs.ResourceIncludedExt
      }
      test {
        this.kotlin.srcDir("${project.projectDir}/src/test/kotlin")
        this.kotlin.includes += Globs.Tests
        this.resources.srcDir("${project.projectDir}/src/test/resources")
        this.resources.includes += Globs.ResourceIncludedExt
      }
    }
  }
}

allOpen {
  annotation("im.tony.annotations.MakeVirtual")
}

noArg {
  annotation("im.tony.annotations.DefCon")
  invokeInitializers = false
}

javafx {
  this.version = "15.0.1"
  this.modules(
    "javafx.controls",
    "javafx.graphics",
    "javafx.fxml",
    "javafx.media",
    "javafx.swing"
  )
}

dependencies {

  // = TornadoFX =
  implementation(Deps.TornadoFx.Core)
  implementation(Deps.TornadoFx.ControlsFxExtensions)

  // = Kotlin Core =
  implementation(Deps.KotlinX.Coroutines.Core)
  implementation(Deps.KotlinX.Coroutines.Jdk8)
  implementation(Deps.KotlinX.Coroutines.Jdk9)
  implementation(Deps.KotlinX.Coroutines.JavaFx)
  implementation(Deps.KotlinX.Coroutines.Debug)
  implementation(Deps.KotlinX.Collections.ImmutableJvmOnly)
  implementation(Deps.Kotlin.Stdlib.Core)
  implementation(Deps.Kotlin.Stdlib.Common)
  implementation(Deps.Kotlin.Stdlib.Jdk8)
  implementation(Deps.KotlinX.Serialization.Core)
  implementation(Deps.KotlinX.Serialization.Json)
  implementation(Deps.KotlinX.Serialization.Properties)
  implementation(kotlin("reflect", "1.4.20"))
  implementation(Deps.KotlinX.DateTime)

  // = JFXtras - JavaFX Extensions =
  implementation(Deps.JfExtras.Agenda)
  implementation(Deps.JfExtras.Window)
  implementation(Deps.JfExtras.Fxml)
  implementation(Deps.JfExtras.iCalendarFx)
  implementation(Deps.JfExtras.Menu)
  implementation(Deps.JfExtras.Common)
  implementation(Deps.JfExtras.FontRoboto)
  implementation(Deps.JfExtras.Parent)
  implementation(Deps.JfExtras.GaugeLinear)
  implementation(Deps.JfExtras.Controls)
  implementation(Deps.JfExtras.JMetro)

  implementation(Deps.FontAwesomeFxCommons)

  implementation(Deps.iKonli.Core)
  implementation(Deps.iKonli.JavaFx)
  implementation(Deps.iKonli.DevkitIconsPack)
  implementation(Deps.iKonli.MaterialDesignPack)

  implementation(Deps.ControlsFX)

  implementation(Deps.Faker)

  implementation(Deps.Google.JimFs)
  implementation(Deps.Google.ApiServices.Docs)
  implementation(Deps.Google.ApiServices.Drive)
  implementation(Deps.Google.ApiServices.Sheets)
  implementation(Deps.Google.ApiServices.File)
  implementation(Deps.Google.ApiServices.Logging)
  implementation(Deps.Google.Clients.Api.Base)
  implementation(Deps.Google.Clients.Api.GSON)
  implementation(Deps.Google.Clients.OAuth.Base)
  implementation(Deps.Google.Clients.OAuth.Jetty)

  implementation(Deps.ScenicView)

  implementation(Deps.JavaStringSimilarity)

  implementation(Deps.Validation.Konform)

  implementation(Deps.Arrow.Core)
  implementation(Deps.Arrow.CoreData)
  implementation(Deps.Arrow.Syntax)
  implementation(Deps.Arrow.FxCoroutines)
  implementation(Deps.Arrow.Optics)
  implementation(Deps.Arrow.Mtl)
  implementation(Deps.Arrow.MtlData)
  implementation(Deps.Arrow.OpticsMtl)
  implementation(Deps.Arrow.Free)
  implementation(Deps.Arrow.FreeData)
  implementation(Deps.Arrow.Aql)

  kapt(Deps.Arrow.Kapt.Meta)
  kapt(Deps.Arrow.Kapt.Generic)

  // =====================
  // = Test Dependencies =
  // =====================
  testImplementation(Deps.JfExtras.TestSupport)
  testImplementation(Deps.TestFx.Core)
  testImplementation(Deps.TestFx.JUnit5)

  testImplementation(Deps.Testing.Kotest.Runner.JUnit5) // for kotest framework
  // testImplementation(Deps.Testing.Kotest.Runner.KotestEngine)
  testImplementation(Deps.Testing.Kotest.Assertions.Core) // for kotest core jvm assertions
  testImplementation(Deps.Testing.Kotest.Assertions.Compiler)
  testImplementation(Deps.Testing.Kotest.Assertions.Json)
  testImplementation(Deps.Testing.Kotest.Assertions.Sql)
  testImplementation(Deps.Testing.Kotest.Assertions.Konform)
  testImplementation(Deps.Testing.Kotest.Assertions.Arrow)
  testImplementation(Deps.Testing.Kotest.Property) // for kotest property test
  testImplementation(Deps.Testing.Kotest.Extensions.Http)
  testImplementation(Deps.Testing.Kotest.Extensions.MockServer)
  testImplementation(Deps.Testing.Kotest.Extensions.TestContainers)
  // testImplementation(Deps.Testing.Kotest.Plugins.PiTest)


  testImplementation(Deps.Testing.MockK.Common)

  testImplementation(Deps.Kotlin.Test.Common)
  testImplementation(Deps.Kotlin.Test.Junit)
  testImplementation(Deps.Kotlin.Test.Junit5)
  testImplementation(Deps.KotlinX.Coroutines.Core)
  testImplementation(Deps.KotlinX.Coroutines.Test)
}

tasks {
  compileKotlin {
    kotlinOptions {
      languageVersion = "1.4"
      jvmTarget = "11"
      noStdlib = false
      noReflect = false
      includeRuntime = true
      freeCompilerArgs += listOf(
        "-Xinline-classes"
      )
    }
  }
  compileTestKotlin {
    kotlinOptions {
      languageVersion = "1.4"
      jvmTarget = "11"
      noStdlib = false
      noReflect = false
      includeRuntime = true
      freeCompilerArgs += listOf(
        "-Xinline-classes"
      )
    }
  }
  test {
    useJUnitPlatform()

    ignoreFailures = true
    testLogging {
      showStandardStreams = true
    }
  }
}
