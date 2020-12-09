object Globs {
  val KotlinSources: List<String> = listOf(
    "**/main/kotlin/**",
    "**/*.kt",
    "**/*.kt"
  )

  val KotlinResources: List<String> = listOf(
    "**/main/resources/**"
  )

  val ResourceIncludedExt: List<String> = listOf(
    "*.ttf",
    "*.otf",
    "*.json",
    "*.png"
  )

  val ResourceExcludedExt: List<String> = listOf(
    "*.ttf",
    "*.otf",
    "*.json",
    "*.png"
  )

  val Tests: List<String> = listOf(
    "**/test/kotlin/**",
    "**/*.Spec.kt",
    "**/*.Test.kt",
    "**/*Spec.kt",
    "**/*Test.kt"
  )

  val TestResources: List<String> = listOf(
    "**/test/resources/**"
  )
}
