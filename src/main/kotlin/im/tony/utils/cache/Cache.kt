package im.tony.utils.cache

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource
import kotlin.time.seconds

public interface DataSource<TKey, TVal> {
  public fun get(key: TKey): TVal
  public fun getOrDefault(key: TKey, default: TVal): TVal
  public fun has(key: TKey): Boolean
}

public interface MutableDataSource<TKey, TVal> : DataSource<TKey, TVal> {
  public fun set(key: TKey, value: TVal): Boolean
}

@ExperimentalTime
public interface CachedMap<TKey, TVal, TDataSource : DataSource<TKey, TVal>> {
  public val isInit: Boolean
  public val hasDataSource: Boolean

  /**
   * Gets the [TVal], updating it if it has expired. Returns null if
   * value is not found in the [DataSource].
   */
  public fun get(key: TKey): TVal?

  /**
   * Loads the given [keys] from the [DataSource]. Returns the number of values loaded.
   */
  public fun load(vararg keys: TKey): Int
}

@ExperimentalTime
abstract class CacheBase<TKey, TVal, TDataSource : DataSource<TKey, TVal>> : CachedMap<TKey, TVal, TDataSource> {
  abstract inner class Entry<TKey, TVal>(public val key: TKey, public var value: TVal? = null) {
    public var hasValue: Boolean = value != null
      private set
  }

  lateinit var map: Map<TKey, TVal>

}

@ExperimentalTime
public open class TimestampedCache<TKey, TVal, TDataSource : DataSource<TKey, TVal>>(
  protected open val dataSource: DataSource<TKey, TVal>,
  protected open var lifetime: Duration = defaultDuration,
  protected open val timeSource: TimeSource = defaultTimeSource
) : CachedMap<TKey, TVal, TDataSource> {
  override val isInit: Boolean = true
  override val hasDataSource: Boolean = true

  override fun get(key: TKey): TVal? {
    return null
  }

  override fun load(vararg keys: TKey): Int {
    return 0
  }

  public companion object {
    public var defaultDuration: Duration = 3.seconds
    public var defaultTimeSource: TimeSource = TimeSource.Monotonic
  }
}
