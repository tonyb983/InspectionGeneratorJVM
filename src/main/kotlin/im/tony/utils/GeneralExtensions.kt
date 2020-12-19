@file:Suppress("unused")

package im.tony.utils

import io.github.serpro69.kfaker.provider.ESport

inline fun <T> T.andThen(block: T.() -> Unit) {
  block(this)
}

sealed class StringGetter {
  abstract val value: String

  class Value(override val value: String) : StringGetter()
  class Getter(private val getter: () -> String, callNow: Boolean) : StringGetter() {
    override val value: String by lazy { getter() }
    private var nothing: Int = 0

    init {
      if (callNow) {
        nothing = value.hashCode()
      }
    }
  }

  companion object {
    fun create(value: String): StringGetter = Value(value)
    fun create(getter: () -> String, asLazy: Boolean = true): StringGetter = Getter(getter, asLazy)
  }
}

fun String.asStringGetter(): StringGetter = StringGetter.create(this)

fun <T> T.isOneOf(vararg ts: T): Boolean = ts.contains(this)

fun <TValue, TIter : MutableCollection<TValue>> TIter.removeAnd(element: TValue): TIter {
  this.remove(element)
  return this
}

fun <T> T?.or(default: T): T = this ?: default

fun ESport.quote(): String =
  "Today ${players()} of ${this.teams()} played ${this.teams()} in ${this.games()} during the ${this.events()} in the ${this.leagues()}"

val String.Companion.empty: String by lazy { "" }
