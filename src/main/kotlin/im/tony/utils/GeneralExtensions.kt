package im.tony.utils

public inline fun <T> T.andThen(block: T.() -> Unit) {
  block(this)
}

public sealed class StringGetter {
  public abstract val value: String

  public class Value(override val value: String) : StringGetter()
  public class Getter(private val getter: () -> String, callNow: Boolean) : StringGetter() {
    override val value: String by lazy { getter() }
    private var nothing: Int = 0

    init {
      if (callNow) {
        nothing = value.hashCode()
      }
    }
  }

  public companion object {
    public fun create(value: String): StringGetter = Value(value)
    public fun create(getter: () -> String, asLazy: Boolean = true): StringGetter = Getter(getter, asLazy)
  }
}

public fun String.asStringGetter(): StringGetter = StringGetter.create(this)

public inline fun <T> T.isOneOf(vararg ts: T): Boolean = ts.contains(this)
