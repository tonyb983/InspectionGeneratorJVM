import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  `kotlin-dsl`
  `java-library`
}

repositories {
  mavenCentral()
  google()
  jcenter()
}

kotlinDslPluginOptions {
  experimentalWarning.set(false)
}

dependencies {
  /* Example Dependency */
  /* Depend on the kotlin plugin, since we want to access it in our plugin */
  implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.20")

  /* Depend on the default Gradle API's since we want to build a custom plugin */
  implementation(gradleKotlinDsl())
  implementation(gradleApi())
  implementation(localGroovy())
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
  freeCompilerArgs = listOf("-Xinline-classes")
  languageVersion = "1.4"
}
