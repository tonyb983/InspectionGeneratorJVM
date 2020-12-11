package im.tony.utils.cache

import kotlin.time.*

/**
 * A timestamped value that cannot be reset.
 */
@ExperimentalTime
public open class TimedValue<TValue>(
  protected var lifetimeLength: Duration = MutableTimedValue.DEFAULT_DURATION,
  protected var timeSource: TimeSource = MutableTimedValue.DEFAULT_TIMESOURCE,
) {
  protected var lastSet: TimeMark = timeSource.markNow()
  protected var innerValue: TValue? = null
  protected var hasExpired: Boolean = false

  public constructor(
    value: TValue,
    lifetimeLength: Duration = DEFAULT_DURATION,
    timeSource: TimeSource = DEFAULT_TIMESOURCE
  ) : this(lifetimeLength, timeSource) {
    this.innerValue = value
  }

  init {
    require(!lifetimeLength.isNegative()) { "TimedValue values cannot be created with a negative lifetimeLength." }

    isUpToDate()
  }

  /**
   * Checks if the value [isValid], if the value [isNotNull] and [isUpToDate]
   * returns the [innerValue] otherwise returns null.
   */
  public fun get(): TValue? = if (isValid()) innerValue!! else null

  /**
   * If the value [isValid] returns it, otherwise returns the [default].
   */
  public fun getOrDefault(default: TValue): TValue = if (isValid()) innerValue!! else default

  /**
   * Gets the length of time that this value remains valid.
   */
  public fun getLifetime(): Duration = lifetimeLength

  /**
   * Checks whether this value is up to date based on it's lifetime
   * and the current time. Uses the [hasExpired] flag to cache the
   * result of calling [TimeMark.hasNotPassedNow] on [lastSet] plus [lifetimeLength].
   */
  public fun isUpToDate(): Boolean {
    if (hasExpired) return false

    hasExpired = lastSet.plus(lifetimeLength).hasNotPassedNow()
    return hasExpired
  }

  /** Whether the inner value is null (not including it's expiration). */
  public fun isNull(): Boolean = innerValue == null

  /** Whether the inner value is not null (not including it's expiration). */
  public fun isNotNull(): Boolean = innerValue != null

  /**
   * Whether this value [isNotNull] and [isUpToDate].
   */
  public fun isValid(): Boolean = isNotNull() && isUpToDate()

  /**
   * Gets the inner value regardless of its expiration.
   */
  public fun getValue(): TValue? = innerValue

  public companion object {
    public val DEFAULT_DURATION: Duration = 3.seconds
    public val DEFAULT_TIMESOURCE: TimeSource = TimeSource.Monotonic
  }
}

/**
 * A timestamped value that can be reset and updated.
 */
@ExperimentalTime
public class MutableTimedValue<TValue>(
  lifetimeLength: Duration = DEFAULT_DURATION,
  timeSource: TimeSource = DEFAULT_TIMESOURCE
) : TimedValue<TValue>(lifetimeLength, timeSource) {

  public constructor(
    initialValue: TValue?,
    lifetimeLength: Duration = DEFAULT_DURATION,
    timeSource: TimeSource = DEFAULT_TIMESOURCE
  ) : this(lifetimeLength, timeSource) {
    innerValue = initialValue
    lastSet = timeSource.markNow()
  }

  init {
    require(!lifetimeLength.isNegative()) { "TimedValue values cannot be created with a negative lifetimeLength." }
  }

  /**
   * This sets the [innerValue] of this [MutableTimedValue],
   * resets the [hasExpired] flag and sets a new [lastSet].
   */
  public fun set(newValue: TValue) {
    this.innerValue = newValue
    hasExpired = false
    lastSet = timeSource.markNow()
  }

  /**
   * If the inner value is not null and not expired, returns it,
   * other it sets the inner value to the given value and returns it.
   */
  public fun getOrUpdate(updated: TValue): TValue {
    if (isValid())
      return innerValue!!

    set(updated)
    return updated
  }

  /**
   * If the inner value is not null and not expired, returns it,
   * other it sets the inner value to the given value and returns it.
   */
  public fun getOrUpdate(getter: () -> TValue): TValue {
    return getOrUpdate(getter.invoke())
  }

  public companion object {
    public val DEFAULT_DURATION: Duration = 3.seconds
    public val DEFAULT_TIMESOURCE: TimeSource = TimeSource.Monotonic
  }
}
