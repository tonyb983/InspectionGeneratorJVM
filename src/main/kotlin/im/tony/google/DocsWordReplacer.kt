package im.tony.google

public class ReplacementMapper<T> {
  inner class Rule(public val key: String, public val rule: (T) -> String)

  private val rules: MutableMap<String, Rule> = mutableMapOf()

  public constructor(vararg rules: ReplacementMapper<T>.Rule) {
    val distinct = rules.distinctBy { it.key }
    if (distinct.size != rules.size) {
      println("Error, overlapping rules in ReplacementMapper constructor.")
    }

    distinct.associateByTo(this.rules, { it.key })
  }
}

class DocsWordReplacer
