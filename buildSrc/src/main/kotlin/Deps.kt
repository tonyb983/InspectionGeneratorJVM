@file:Suppress("unused", "SpellCheckingInspection")

import org.gradle.api.Incubating

object Deps {

  object TestFx {
    const val Core = "org.testfx:testfx-core:${Versions.TestFx}"
    const val JUnit5 = "org.testfx:testfx-junit5:${Versions.TestFx}"
  }

  object TornadoFx {
    const val Core = "no.tornado:tornadofx:${Versions.TornadoFx.Core}"
    const val ControlsFxExtensions = "no.tornado:tornadofx-controlsfx:${Versions.ControlsFx.TornadoExtensions}"
  }

  const val ControlsFX = "org.controlsfx:controlsfx:${Versions.ControlsFx.Core}"

  const val ScenicView = "net.raumzeitfalle.fx:scenic-view:${Versions.ScenicView}"

  object JfExtras {
    private const val group = "org.jfxtras"
    private const val std = "$group:jfxtras"

    const val Agenda = "$std-agenda:${Versions.JFExtras.Core}"
    const val Common = "$std-common:${Versions.JFExtras.Core}"
    const val Controls = "$std-controls:${Versions.JFExtras.Core}"
    const val FontRoboto = "$std-font-roboto:${Versions.JFExtras.Core}"
    const val Fxml = "$std-fxml:${Versions.JFExtras.Core}"
    const val GaugeLinear = "$std-gauge-linear:${Versions.JFExtras.Core}"
    const val iCalendarFx = "$std-icalendarfx:${Versions.JFExtras.Core}"
    const val Menu = "$std-menu:${Versions.JFExtras.Core}"
    const val Parent = "$std-parent:${Versions.JFExtras.Core}"
    const val TestSupport = "$std-test-support:${Versions.JFExtras.Core}"
    const val Window = "$std-window:${Versions.JFExtras.Core}"
    const val JMetro = "$group:jmetro:${Versions.JFExtras.JMetro}"
  }

  object iKonli {
    private const val group = "org.kordamp.ikonli"
    private const val prefix = "$group:ikonli"

    const val Core = "$prefix-core:${Versions.iKonli}"
    const val DevkitIconsPack = "$prefix-devicons-pack:${Versions.iKonli}"
    const val JavaFx = "$prefix-javafx:${Versions.iKonli}"
    const val MaterialDesignPack = "$prefix-materialdesign-pack:${Versions.iKonli}"
  }

  const val FontAwesomeFxCommons = "de.jensd:fontawesomefx-commons:${Versions.FontAwesomeFxCommons}"

  const val Faker = "io.github.serpro69:kotlin-faker:${Versions.KotlinFaker}"

  object Exposed {
    private const val group = "org.jetbrains.exposed"
    private const val prefix = "$group:exposed-"

    const val Core = "$prefix-core:${Versions.Exposed}"
    const val Dao = "$prefix-dao:${Versions.Exposed}"
    const val Jdbc = "$prefix-jdbc:${Versions.Exposed}"
    const val JodaTime = "$prefix-jodatime:${Versions.Exposed}"
    const val JavaTime = "$prefix-java-time:${Versions.Exposed}"
  }

  object Db {
    const val H2 = "com.h2database:h2:${Versions.Db.H2}"
    const val Sqlite = "org.xerial:sqlite-jdbc:${Versions.Db.Sqlite}"
  }

  @Incubating
  object Kotlin {
    private const val group = "org.jetbrains.kotlin"

    /**
     * Kotlin Standard Library
     *
     * [API reference](https://kotlinlang.org/api/latest/jvm/stdlib/)
     */
    object Stdlib {
      private const val prefix = "$group:kotlin-stdlib"

      const val Core = "$prefix:${Versions.Kotlin.Lang}"
      const val Jdk7 = "$prefix-jdk7:${Versions.Kotlin.Lang}"

      const val Jdk8 = "$prefix-jdk8:${Versions.Kotlin.Lang}"

      const val Js = "$prefix-js:${Versions.Kotlin.Lang}"

      const val Common = "$prefix-common:${Versions.Kotlin.Lang}"
    }


    /**
     * The `kotlin.test` library provides annotations to mark test functions,
     * and a set of utility functions for performing assertions in tests,
     * independently of the test framework being used.
     *
     * [Documentation and API reference](https://kotlinlang.org/api/latest/kotlin.test/)
     */
    object Test {
      private const val prefix = "$group:kotlin-test"
      const val AnnotationsCommon = "$prefix-annotations-common:${Versions.Kotlin.Lang}"
      const val Common = "$prefix-common:${Versions.Kotlin.Lang}"
      const val Js = "$prefix-js:${Versions.Kotlin.Lang}"
      const val JsRunner = "$prefix-js-runner:${Versions.Kotlin.Lang}"

      const val Junit = "$prefix-junit:${Versions.Kotlin.Lang}"
      const val Junit5 = "$prefix-junit5:${Versions.Kotlin.Lang}"
    }
  }

  @Incubating
  object KotlinX {
    private const val group = "org.jetbrains.kotlinx"
    private const val artifactBase = "$group:kotlinx"

    const val DateTime = "$artifactBase-datetime:${Versions.Kotlinx.DateTime}"

    /**
     * Library support for Kotlin coroutines.
     *
     * Brings structured concurrency and reactive programming with `Flow`.
     *
     * [Coroutines Guide on Kotlin's website](https://kotlinlang.org/docs/reference/coroutines/coroutines-guide.html)
     *
     * Kotlin coroutines on Android: [d.android.com/kotlin/coroutines](http://d.android.com/kotlin/coroutines)
     *
     * Talks by Roman Elizarov (co-author of kotlinx.coroutines):
     * - [Structured concurrency](https://www.youtube.com/watch?v=Mj5P47F6nJg)
     * - [Asynchronous Data Streams with Kotlin Flow](https://www.youtube.com/watch?v=tYcqn48SMT8)
     *
     * [Change log](https://github.com/Kotlin/kotlinx.coroutines/blob/master/CHANGES.md)
     *
     * GitHub page: [Kotlin/kotlinx.coroutines](https://github.com/Kotlin/kotlinx.coroutines)
     */
    object Coroutines {
      private const val artifactPrefix = "$artifactBase-coroutines"

      const val Core = "$artifactPrefix-core:${Versions.Kotlinx.Coroutines}"
      const val CoreJs = "$artifactPrefix-core-js:${Versions.Kotlinx.Coroutines}"

      const val JavaFx = "$artifactPrefix-javafx:${Versions.Kotlinx.Coroutines}"

      const val Jdk8 = "$artifactPrefix-jdk8:${Versions.Kotlinx.Coroutines}"
      const val Jdk9 = "$artifactPrefix-jdk9:${Versions.Kotlinx.Coroutines}"
      const val Slf4J = "$artifactPrefix-slf4j:${Versions.Kotlinx.Coroutines}"
      const val Guava = "$artifactPrefix-guava:${Versions.Kotlinx.Coroutines}"

      const val Reactive = "$artifactPrefix-reactive:${Versions.Kotlinx.Coroutines}"
      const val Reactor = "$artifactPrefix-reactor:${Versions.Kotlinx.Coroutines}"
      const val Rx2 = "$artifactPrefix-rx2:${Versions.Kotlinx.Coroutines}"
      const val Rx3 = "$artifactPrefix-rx3:${Versions.Kotlinx.Coroutines}"

      const val Debug = "$artifactPrefix-debug:${Versions.Kotlinx.Coroutines}"
      const val Test = "$artifactPrefix-test:${Versions.Kotlinx.Coroutines}"
    }

    /**
     * Kotlin multiplatform / multi-format serialization.
     *
     * [Page on Kotlin's website](https://kotlinlang.org/docs/reference/serialization.html)
     *
     * [Kotlin Serialization Guide](https://github.com/Kotlin/kotlinx.serialization/blob/master/docs/serialization-guide.md)
     *
     * [Change log](https://github.com/Kotlin/kotlinx.serialization/blob/master/CHANGELOG.md)
     *
     * GitHub page: [Kotlin/kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization)
     */
    object Serialization {
      private const val artifactPrefix = "$artifactBase-serialization"

      const val Core = "$artifactPrefix-core:${Versions.Kotlinx.Serialization}"
      const val Json = "$artifactPrefix-json:${Versions.Kotlinx.Serialization}"
      const val Protobuf = "$artifactPrefix-protobuf:${Versions.Kotlinx.Serialization}"
      const val Cbor = "$artifactPrefix-cbor:${Versions.Kotlinx.Serialization}"
      const val Properties = "$artifactPrefix-properties:${Versions.Kotlinx.Serialization}"
      //TODO: Add hocon Artifact once documented.
    }

    object Collections {
      private const val artPrefix = "$artifactBase-collections-immutable"

      /**
       * Immutable persistent collections for Kotlin.
       *
       * [Library API proposal](https://github.com/Kotlin/kotlinx.collections.immutable/blob/master/proposal.md)
       *
       * [Change log](https://github.com/Kotlin/kotlinx.collections.immutable/blob/master/CHANGELOG.md)
       *
       * GitHub page: [Kotlin/kotlinx.collections.immutable](https://github.com/Kotlin/kotlinx.collections.immutable)
       */
      const val Immutable = "$artPrefix:${Versions.Kotlinx.Collections}"

      /**
       * Immutable persistent collections for Kotlin.
       *
       * [Library API proposal](https://github.com/Kotlin/kotlinx.collections.immutable/blob/master/proposal.md)
       *
       * [Change log](https://github.com/Kotlin/kotlinx.collections.immutable/blob/master/CHANGELOG.md)
       *
       * GitHub page: [Kotlin/kotlinx.collections.immutable](https://github.com/Kotlin/kotlinx.collections.immutable)
       */
      const val ImmutableJvmOnly = "$artPrefix-jvm:${Versions.Kotlinx.Collections}"
    }

    /**
     * Kotlin DSL for HTML.
     *
     * [Wiki](https://github.com/kotlin/kotlinx.html/wiki)
     *
     * [GitHub releases](https://github.com/Kotlin/kotlinx.html/releases)
     *
     * GitHub page: [Kotlin/kotlinx.html](https://github.com/Kotlin/kotlinx.html)
     */
    object Html {
      private const val artifactPrefix = "$artifactBase-html"

      const val Jvm = "$artifactPrefix-jvm:${Versions.Kotlinx.Html}"
      const val Js = "$artifactPrefix-js:${Versions.Kotlinx.Html}"
    }

    /**
     * Kotlin multiplatform I/O library. (Experimental as of 2020-09-14)
     *
     * [Change log](https://github.com/Kotlin/kotlinx-io/blob/master/CHANGELOG.md)
     *
     * GitHub page: [Kotlin/kotlinx-io](https://github.com/Kotlin/kotlinx-io)
     */
    object Io {
      private const val artifactPrefix = "$artifactBase-io"

      const val Jvm = "$artifactPrefix-jvm:${Versions.Kotlinx.Io}"
    }

    /**
     * Lightweight library allowing to introspect basic stuff about Kotlin symbols.
     *
     * _As of version 1.0, it only supports getting names of parameters and the nullability of
     * their types._
     *
     * [Documentation (of full reflection)](https://kotlinlang.org/docs/reference/reflection.html)
     *
     * [API reference (of full reflection)](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/)
     *
     * [Change log](https://github.com/Kotlin/kotlinx-nodejs/blob/master/CHANGELOG.md)
     *
     * GitHub page: [Kotlin/kotlinx.reflect.lite](https://github.com/Kotlin/kotlinx.reflect.lite)
     */
    object Reflect {
      const val Lite = "$artifactBase.reflect.lite:${Versions.Kotlinx.ReflectLite}"
    }

    object Benchmark {
      private const val artifactPrefix = "$artifactBase.benchmark"

      const val Plugin = "kotlinx.benchmark"

      const val Runtime = "$artifactPrefix.runtime:${Versions.Kotlinx.Benchmark}"
      const val RuntimeJvmOnly = "$artifactPrefix.runtime-jvm:${Versions.Kotlinx.Benchmark}"
    }
  }

  object Logging {
    object KotlinLogger {
      private const val group = "io.github.microutils"

      const val Lib = "$group:kotlin-logging-jvm:${Versions.Logging.KotlinLogger}"
    }

    const val PlayLogback = "com.typesafe.play:play-logback_2.13:${Versions.Logging.PlayLogback}"

    object Slf4 {
      private const val group = "org.slf4j"
      const val Api = "$group:slf4j-api:${Versions.Logging.Slf4j.Api}"
      const val Simple = "$group:slf4j-simple:${Versions.Logging.Slf4j.Api}"
    }

    object Logback {
      private const val group = "ch.qos.logback"
      const val Core = "$group:logback-core:${Versions.Logging.Logback.Core}"
      const val Classic = "$group:logback-classic:${Versions.Logging.Logback.Core}"
      const val Access = "$group:logback-access:${Versions.Logging.Logback.Core}"
    }
  }

  object Google {
    private const val groupBase = "com.google"

    const val JimFs = "$groupBase.jimfs:jimfs:${Versions.Google.JimFs}"

    object ApiServices  {
      private const val group = "$groupBase.apis:google-api-services"

      const val Docs = "$group-docs:${Versions.Google.Apis.Docs}"
      const val Sheets = "$group-sheets:${Versions.Google.Apis.Sheets}"
      const val Drive = "$group-drive:${Versions.Google.Apis.Drive}"
      const val Logging = "$group-logging:${Versions.Google.Apis.Logging}"
      const val File = "$group-file:${Versions.Google.Apis.File}"
    }

    object Clients {
      private const val group = groupBase

      object Api {
        private const val apiBase: String = "$group.api-client"

        const val Base = "$apiBase:google-api-client:${Versions.Google.ApiClient}"
        const val Java6 = "$apiBase:google-api-client-java6:${Versions.Google.ApiClient}"
        const val Jackson2 = "$apiBase:google-api-client-jackson2:${Versions.Google.ApiClient}"
        const val GSON = "$apiBase:google-api-client-gson:${Versions.Google.ApiClient}"
      }

      object OAuth {
        private const val oauthBase = "$group.oauth-client"

        const val Base = "$oauthBase:google-oauth-client:${Versions.Google.OAuthClient}"
        const val Java6 = "$oauthBase:google-oauth-client-java6:${Versions.Google.OAuthClient}"
        const val Jetty = "$oauthBase:google-oauth-client-jetty:${Versions.Google.OAuthClient}"
      }
    }
  }

  @Incubating
  object Testing {
    /**
     * Run unit tests in the JVM with the Android environment.
     *
     * GitHub page: [robolectric/robolectric](https://github.com/robolectric/robolectric)
     */
    const val RoboElectric = "org.robolectric:robolectric:${Versions.RoboElectric}"

    /**
     * JUnit 5: The new major version of the programmer-friendly testing framework for Java
     *
     * Official website: [junit.org/junit5](https://junit.org/junit5/)
     *
     * [User Guide](https://junit.org/junit5/docs/current/user-guide/)
     *
     * [Release Notes](https://junit.org/junit5/docs/current/release-notes/)
     *
     * GitHub page: [junit-team/junit5](https://github.com/junit-team/junit5)
     *
     * [API reference (JavaDoc)](https://junit.org/junit5/docs/current/api/)
     */
    object JUnit {
      private const val group = "org.junit.jupiter"
      private const val artifactPrefix = "$group:junit-jupiter"

      const val Api = "$artifactPrefix-api:${Versions.JUnit}"
      const val Engine = "$artifactPrefix-engine:${Versions.JUnit}"
      const val Params = "$artifactPrefix-params:${Versions.JUnit}"
      const val MigrationSupport = "$artifactPrefix-migrationsupport:${Versions.JUnit}"
    }

    /**
     * Powerful, elegant and flexible test framework for Kotlin
     *
     * Official website: [kotest.io](https://kotest.io/)
     *
     * [Change log](https://kotest.io/changelog/)
     *
     * GitHub page: [kotest/kotest](https://github.com/kotest/kotest)
     */
    object Kotest {
      private const val group = "io.kotest"
      private const val artifactBase = "$group:kotest"

      const val Core = "$artifactBase-core:${Versions.Kotest.Release}"
      const val Property = "$artifactBase-property:${Versions.Kotest.Release}"
      const val PropertyArrow = "$artifactBase-property-arrow:${Versions.Kotest.Release}"

      object Runner {
        private const val artifactPrefix = "$artifactBase-runner"

        const val KotestEngine = "${artifactBase}-framework-engine-jvm:${Versions.Kotest.Release}"
        const val JUnit5 = "$artifactPrefix-junit5:${Versions.Kotest.Release}"
      }

      object Plugins {
        private const val artifactPrefix = "$artifactBase-plugins"

        const val PiTest = "$artifactPrefix-pitest:${Versions.Kotest.Release}"
      }

      object Extensions  {
        private const val artifactPrefix = "$artifactBase-extensions"

        const val Spring = "$artifactPrefix-spring:${Versions.Kotest.Release}"
        const val Koin = "$artifactPrefix-koin:${Versions.Kotest.Release}"
        const val Allure = "$artifactPrefix-allure:${Versions.Kotest.Release}"
        const val TestContainers = "$artifactPrefix-testcontainers:${Versions.Kotest.Release}"
        const val Http = "$artifactPrefix-http:${Versions.Kotest.Release}"
        const val MockServer = "$artifactPrefix-mockserver:${Versions.Kotest.Release}"
      }

      object Assertions  {
        private const val artifactPrefix = "$artifactBase-assertions"

        const val Core = "$artifactPrefix-core:${Versions.Kotest.Release}"
        const val Ktor = "$artifactPrefix-ktor:${Versions.Kotest.Release}"
        const val Json = "$artifactPrefix-json:${Versions.Kotest.Release}"
        const val Arrow = "$artifactPrefix-arrow:${Versions.Kotest.Release}"
        const val Konform = "$artifactPrefix-konform:${Versions.Kotest.Release}"
        const val JSoup = "$artifactPrefix-jsoup:${Versions.Kotest.Release}"
        const val Klock = "$artifactPrefix-klock:${Versions.Kotest.Release}"
        const val Sql = "$artifactPrefix-sql:${Versions.Kotest.Release}"
        const val Compiler = "$artifactPrefix-compiler:${Versions.Kotest.Release}"
      }
    }

    /**
     * A specification framework for Kotlin
     *
     * Official website: [spekframework.org](https://www.spekframework.org/)
     *
     * GitHub page: [spekframework/spek](https://github.com/spekframework/spek)
     *
     * [GitHub releases](https://github.com/spekframework/spek/releases)
     */
    object Spek {
      private const val group = "org.spekframework.spek2"
      private const val artifactBase = "$group:spek"

      object Dsl {
        private const val prefix = "$artifactBase-dsl"
        const val Jvm = "$prefix-jvm:${Versions.Spek.Release}"
        const val Js = "$prefix-js:${Versions.Spek.Release}"
        const val Metadata = "$prefix-metadata:${Versions.Spek.Release}"
      }

      object Runner {
        private const val prefix = "$artifactBase-runner"
        const val JUnit5 = "$prefix-junit5:${Versions.Spek.Release}"
      }

      object Runtime {
        private const val prefix = "$artifactBase-runtime"
        const val Jvm = "$prefix-jvm:${Versions.Spek.Release}"
        const val Metadata = "$prefix-metadata:${Versions.Spek.Release}"
      }
    }

    /**
     * Strikt is an assertion library for Kotlin intended for use with a test runner such as JUnit or Spek.
     *
     * Official website: [strikt.io](https://strikt.io/)
     *
     * [Change log](https://strikt.io/changelog/)
     *
     * [GitHub releases](https://github.com/robfletcher/strikt/releases)
     *
     * GitHub page: [robfletcher/strikt](https://github.com/robfletcher/strikt)
     */
    object Strikt {
      private const val group = "io.strikt:strikt"

      const val Bom = "$group-bom:${Versions.Strikt}"
      const val Core = "$group-core:${Versions.Strikt}"
      const val Arrow = "$group-arrow:${Versions.Strikt}"
      const val Gradle = "$group-gradle:${Versions.Strikt}"
      const val Jackson = "$group-jackson:${Versions.Strikt}"
      const val JavaTime = "$group-java-time:${Versions.Strikt}"
      const val Mockk = "$group-mockk:${Versions.Strikt}"
      const val Protobuf = "$group-protobuf:${Versions.Strikt}"
      const val Spring = "$group-spring:${Versions.Strikt}"
    }

    /**
     * Mocking library for Kotlin.
     *
     * Official Website: [mockk.io](https://mockk.io/)
     *
     * [GitHub releases](https://github.com/mockk/mockk/releases)
     *
     * GitHub page: [mockk/mockk](https://github.com/mockk/mockk)
     */
    object MockK {
      private const val group = "io.mockk"

      const val Common = "$group:mockk-common:${Versions.Mockk}"
    }
  }
}
