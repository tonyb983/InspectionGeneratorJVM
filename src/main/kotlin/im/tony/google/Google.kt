package im.tony.google

import im.tony.utils.cache.TimedValue
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource
import kotlin.time.seconds

private typealias KvPairs<TKey, TValue> = Collection<Pair<TKey, TValue>>

public interface CacheDataSource<TKey, TValue> {
  public fun get(key: TKey): TValue?
}

@ExperimentalTime
private typealias CacheMap<TKey, TValue> = MutableMap<TKey, TimedValue<TValue>>

@ExperimentalTime
public open class Cached<TKey, TValue, TValueProvider : Map<TKey, TValue>>(
  protected open val valueProvider: TValueProvider,
  protected open var duration: Duration = 3.seconds,
  protected open val timeSource: TimeSource = TimeSource.Monotonic,
  initialValues: KvPairs<TKey, TValue> = emptyList()
) : ReadWriteProperty<Any, TValue> {
  private val valueMap: CacheMap<TKey, TValue> = mutableMapOf()

  init {
    valueMap += initialValues.map { (k, v) -> Pair(k, TimedValue(v, duration, timeSource)) }
  }

  public constructor(
    valueProvider: TValueProvider,
    duration: Duration = 3.seconds,
    timeSource: TimeSource = TimeSource.Monotonic,
    lazyInit: Boolean,
    initialKeys: Collection<TKey> = emptyList()
  ) : this(valueProvider, duration, timeSource)

  private var lazyKeyValueInitializer: Lazy<(KvPairs<TKey, TValue>) -> CacheMap<TKey, TValue>>? = null
  private var lazyKeyInitializer: Lazy<(Collection<TKey>) -> CacheMap<TKey, TValue>>? = null

  /**
   * Sets the value of the property for the given object.
   * @param thisRef the object for which the value is requested.
   * @param property the metadata for the property.
   * @param value the value to set.
   */
  override fun setValue(thisRef: Any, property: KProperty<*>, value: TValue) {
    TODO("Not yet implemented")
  }

  override fun getValue(thisRef: Any, property: KProperty<*>): TValue {
    TODO("Not yet implemented")
  }
}

@ExperimentalTime
public open class OwnedCached<TKey, TValue, TValueProvider : Map<TKey, TValue>, TOwner> : Cached<TKey, TValue, TValueProvider>()

