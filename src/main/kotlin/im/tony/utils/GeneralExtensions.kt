package im.tony.utils

import java.lang.ref.WeakReference

inline fun <T> T.andThen(block: T.() -> Unit) {
  block(this)
}

sealed class StringGetter {
  abstract val value: String;

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
