package im.tony.google

import im.tony.utils.andThen
import java.util.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlin.time.*

fun interface Provider<K, V> {
  fun get(key: K): V
}

@ExperimentalTime
data class TimedValue<TValue>(
  private var duration: Duration,
  private var timeSource: TimeSource = TimeSource.Monotonic
) {
  private var lastSet: TimeMark = timeSource.markNow()
  private var value: Optional<TValue> = Optional.empty()

  constructor(initialValue: TValue?, duration: Duration, timeSource: TimeSource = TimeSource.Monotonic) : this(duration, timeSource) {
    value = Optional.ofNullable(initialValue)
    lastSet = timeSource.markNow()
  }

  init {
    require(!duration.isNegative()) { "TimedValue values cannot be created with a negative duration." }
  }

  fun isUpToDate(): Boolean = (lastSet.plus(duration).hasNotPassedNow())

  fun set(newValue: TValue) {
    this.value = Optional.ofNullable(newValue)
    lastSet = timeSource.markNow()
  }

  fun getOrIfExpired(defaultValue: TValue): TValue = if (value.isPresent && isUpToDate()) value.get() else defaultValue

  fun <TResult> ifUpdated(action: TValue.() -> TResult): Optional<TResult> {
    return if (isUpToDate()) Optional.ofNullable(action(value.get()))
    else Optional.empty()
  }

  fun <TResult> ifUpdatedOrElse(updatedAction: TValue.() -> TResult, outdatedAction: () -> TResult): TResult =
    if (isUpToDate()) updatedAction.invoke(value.get()) else outdatedAction.invoke()

  fun getOrUpdate(updated: TValue): TValue {
    if (isUpToDate() && value.isPresent)
      return value.get()

    set(updated)
    return value.get()
  }

  fun getOrUpdate(getter: () -> TValue): TValue {
    return getOrUpdate(getter.invoke())
  }

  fun <TFrom> getOrUpdate(getFrom: TFrom, getter: TFrom.() -> TValue): TValue {
    return getOrUpdate(getter.invoke(getFrom))
  }

  fun withTimeSource(timeSource: TimeSource) = this.andThen { this@TimedValue.timeSource = timeSource }
}

typealias KvPairs<TKey, TValue> = Collection<Pair<TKey, TValue>>

@ExperimentalTime
typealias CacheMap<TKey, TValue> = MutableMap<TKey, TimedValue<TValue>>

@ExperimentalTime
open class Cached<TKey, TValue, TValueProvider : Provider<TKey, TValue>, TOwner>(
  protected open val valueProvider: TValueProvider,
  protected open var duration: Duration = 3.seconds,
  protected open val timeSource: TimeSource = TimeSource.Monotonic,
  initialValues: KvPairs<TKey, TValue> = emptyList()
) : ReadWriteProperty<TOwner, TValue> {
  private val valueMap: CacheMap<TKey, TValue> = mutableMapOf()

  init {
    valueMap += initialValues.map { (k, v) -> Pair(k, TimedValue(v, duration, timeSource)) }
  }

  constructor(
    valueProvider: TValueProvider,
    duration: Duration = 3.seconds,
    timeSource: TimeSource = TimeSource.Monotonic,
    lazyInit: Boolean,
    initialKeys: Collection<TKey> = emptyList()
  ) : this(valueProvider, duration, timeSource) {

  }

  private var lazyKeyValueInitializer: Lazy<(KvPairs<TKey, TValue>) -> CacheMap<TKey, TValue>>? = null
  private var lazyKeyInitializer: Lazy<(Collection<TKey>) -> CacheMap<TKey, TValue>>? = null

  /**
   * Sets the value of the property for the given object.
   * @param thisRef the object for which the value is requested.
   * @param property the metadata for the property.
   * @param value the value to set.
   */
  override fun setValue(thisRef: TOwner, property: KProperty<*>, value: TValue) {
    TODO("Not yet implemented")
  }

  override fun getValue(thisRef: TOwner, property: KProperty<*>): TValue {
    TODO("Not yet implemented")
  }
}

