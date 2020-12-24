package im.tony.utils

import javafx.beans.value.ObservableBooleanValue
import java.time.LocalDateTime
import java.util.*
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import javafx.util.Duration as JavaFxDuration
import java.time.Duration as JavaDuration


data class SharpEvent<TData>(val sender: Any?, val feed: SharpEventFeed<TData>?, val data: TData?) {
  constructor(data: TData?) : this(null, null, data)
  constructor(feed: SharpEventFeed<TData>, data: TData?) : this(null, feed, data)
  constructor() : this(null, null, null)
}

class SharpEventEmitter<TData> {
  private var listeners: MutableList<SharpEventListener<TData>?> = mutableListOf()

  fun fireEvent(event: SharpEvent<TData>) {
    if (listeners.isEmpty()) {
      return
    }

    listeners.removeAll { it == null }
    for (listener in listeners) {
      listener?.onFire(event)
    }
  }

  fun fireEvent(data: TData) {
    if (listeners.isEmpty()) {
      return
    }

    val event = SharpEvent(null, asFeed(), data).also { fireEvent(it) }
    fireEvent(event)
  }

  fun <TSender> fireEvent(sender: TSender, data: TData) {
    if (listeners.isEmpty()) {
      return
    }

    val event = SharpEvent(sender, asFeed(), data)
    fireEvent(event)
  }

  fun addListener(listener: SharpEventListener<TData>): Boolean = listeners.add(listener)
  fun addListener(listener: EventListener<TData>): Boolean = listeners.add(SharpEventListener.basic(listener))
  fun removeListener(listener: SharpEventListener<TData>): Boolean = listeners.remove(listener)
  fun removeListener(listener: EventListener<TData>): Boolean = listeners.remove(SharpEventListener.basic(listener))

  fun asFeed(): SharpEventFeed<TData> = SharpEventFeed(this)
}

class SharpEventFeed<TData>(private val emitter: SharpEventEmitter<TData>) {
  fun addListener(listener: SharpEventListener<TData>): Boolean = emitter.addListener(listener)
  fun addListener(listener: EventListener<TData>): Boolean = emitter.addListener(listener)
  fun removeListener(listener: SharpEventListener<TData>): Boolean = emitter.removeListener(listener)
  fun removeListener(listener: EventListener<TData>): Boolean = emitter.removeListener(listener)
}

fun interface EventListener<TData> {
  fun onFire(data: SharpEvent<TData>)
}

sealed class SharpEventListener<TData>(protected val listener: EventListener<TData>?) {
  val id: UUID = UUID.randomUUID()

  abstract fun onFire(data: SharpEvent<TData>)

  companion object {
    fun <TData> basic(listener: EventListener<TData>?): SharpEventListener<TData> = BasicEventListener(listener)
    fun <TData> oneTime(listener: EventListener<TData>?): SharpEventListener<TData> = OneTimeListener(listener)
    fun <TData> counted(times: Int, listener: EventListener<TData>?): SharpEventListener<TData> = CountedListener(times, listener)
    fun <TData> timed(expiration: LocalDateTime, listener: EventListener<TData>?): SharpEventListener<TData> = TimedListener(expiration, listener)
    fun <TData> timed(lifetime: JavaDuration, listener: EventListener<TData>?): SharpEventListener<TData> = TimedListener(lifetime, listener)
    fun <TData> timed(lifetime: JavaFxDuration, listener: EventListener<TData>?): SharpEventListener<TData> = TimedListener(lifetime, listener)
  }
}

/**
 * This listener is just a basic event listener that will run until it is removed.
 */
open class BasicEventListener<TData>(listener: EventListener<TData>?) : SharpEventListener<TData>(listener) {

  override fun onFire(data: SharpEvent<TData>) {
    listener?.onFire(data)
  }
}

/**
 * This listener will fire once and then remove itself from the event emitter.
 */
class OneTimeListener<TData>(listener: EventListener<TData>?) : SharpEventListener<TData>(listener) {
  override fun onFire(data: SharpEvent<TData>) {
    listener?.onFire(data)
    data.feed?.removeListener(this)
  }
}

/**
 * This listener will fire until it runs [max] times.
 */
class CountedListener<TData>(private val max: Int, listener: EventListener<TData>?) : SharpEventListener<TData>(listener) {
  var totalRuns = 0
    private set

  val maxRuns: Int get() = max

  override fun onFire(data: SharpEvent<TData>) {
    totalRuns += 1
    if (totalRuns >= maxRuns) {
      data.feed?.removeListener(this)
      return
    }

    listener?.onFire(data)
  }
}

/**
 * This listener will fire until the [expiration] passes. The expiration timestamp will be created if the [JavaDuration]
 * or [JavaFxDuration] are used.
 */
class TimedListener<TData>(private val expiration: LocalDateTime, listener: EventListener<TData>?) : SharpEventListener<TData>(listener) {
  constructor(lifetimeDuration: JavaDuration, listener: EventListener<TData>?) : this(LocalDateTime.now().plus(lifetimeDuration), listener)
  constructor(lifetimeDuration: JavaFxDuration, listener: EventListener<TData>?) : this(
    LocalDateTime.now().plus(JavaDuration.ofMillis(lifetimeDuration.toMillis().toLong())), listener
  )

  override fun onFire(data: SharpEvent<TData>) {
    if (LocalDateTime.now().isAfter(expiration)) {
      data.feed?.removeListener(this)
      return
    }

    listener?.onFire(data)
  }
}

/**
 * This listener will fire until [invalidator] returns true.
 */
class BooleanValueListener<TData>(private val invalidator: ObservableBooleanValue, listener: EventListener<TData>?) :
  SharpEventListener<TData>(listener) {
  override fun onFire(data: SharpEvent<TData>) {
    if (invalidator.get()) {
      data.feed?.removeListener(this)
      return
    }

    listener?.onFire(data)
  }
}

class SharpEmitterDelegate<TOwner : Any?, TData>(private val owner: TOwner?, private val emitter: SharpEventEmitter<TData>) :
  ReadOnlyProperty<TOwner, SharpEventEmitter<TData>> {
  constructor() : this(null, SharpEventEmitter())

  /**
   * Returns the value of the property for the given object.
   * @param thisRef the object for which the value is requested.
   * @param property the metadata for the property.
   * @return the property value.
   */
  override fun getValue(thisRef: TOwner, property: KProperty<*>): SharpEventEmitter<TData> = emitter

}

class SharpFeedDelegate<TOwner : Any?, TData>(private val owner: TOwner?, private val feed: SharpEventFeed<TData>) :
  ReadOnlyProperty<TOwner, SharpEventFeed<TData>> {
  constructor(feed: SharpEventFeed<TData>) : this(null, feed)
  constructor(emitter: SharpEventEmitter<TData>) : this(null, emitter.asFeed())

  /**
   * Returns the value of the property for the given object.
   * @param thisRef the object for which the value is requested.
   * @param property the metadata for the property.
   * @return the property value.
   */
  override fun getValue(thisRef: TOwner, property: KProperty<*>): SharpEventFeed<TData> = feed
}

data class EventInstantiationException(val caller: Any?) :
  Exception("Unable to instantiate event property. Caller: ${caller?.toString() ?: "<Unknown>"}")

fun <TData> sharpEvent(): SharpEmitterDelegate<Any?, TData> = SharpEmitterDelegate()
fun <TData> sharpEmitter(): SharpEmitterDelegate<Any?, TData> = SharpEmitterDelegate()
fun <TOwner : Any?, TData> ownedSharpEmitter(): SharpEmitterDelegate<TOwner, TData> = SharpEmitterDelegate()
fun <TData> sharpFeed(emitter: SharpEventEmitter<TData>): SharpFeedDelegate<Any?, TData> = SharpFeedDelegate(emitter)
fun <TOwner : Any, TData> sharpFeed(
  kClass: KClass<TOwner>,
  getter: KClass<TOwner>.() -> SharpEventEmitter<TData>
): SharpFeedDelegate<TOwner, TData> = SharpFeedDelegate(getter.invoke(kClass).asFeed())

fun <TData> sharpFeed(emitterProp: KProperty<SharpEventEmitter<TData>>): SharpFeedDelegate<Any?, TData> = SharpFeedDelegate(emitterProp.getter.call())

// ===========================================
// Testing Implementation
// ===========================================

private data class CreatedEvent(val value: String)

private class EventTester {
  private val onCreatedEmitter by sharpEvent<CreatedEvent>()
  val onCreatedEvent: SharpEventFeed<CreatedEvent> by sharpFeed(onCreatedEmitter)

  private fun testFunc() {
    for (i in 0..10) {
      onCreatedEmitter.fireEvent(CreatedEvent("Hey girl heyyyy"))
    }
  }
}

private val tester = EventTester()

private class OtherTester(private val eventTester: EventTester?) : AutoCloseable {
  private val listener = EventListener<CreatedEvent> { println("Received Event: $it") }
  private val listener2 = EventListener<CreatedEvent> {
    println("Received Event: $it")
    println("Received Event Twice: $it")
  }
  private val listener3: SharpEventListener<CreatedEvent> = OneTimeListener { println("Received One-Time Event: $it") }

  init {
    eventTester?.onCreatedEvent?.addListener(listener)
    eventTester?.onCreatedEvent?.addListener(listener2)
    eventTester?.onCreatedEvent?.addListener(listener3)
  }

  /**
   * Closes this resource, relinquishing any underlying resources.
   * This method is invoked automatically on objects managed by the
   * `try`-with-resources statement.
   *
   *
   * While this interface method is declared to throw `Exception`, implementers are *strongly* encouraged to
   * declare concrete implementations of the `close` method to
   * throw more specific exceptions, or to throw no exception at all
   * if the close operation cannot fail.
   *
   *
   *  Cases where the close operation may fail require careful
   * attention by implementers. It is strongly advised to relinquish
   * the underlying resources and to internally *mark* the
   * resource as closed, prior to throwing the exception. The `close` method is unlikely to be invoked more than once and so
   * this ensures that the resources are released in a timely manner.
   * Furthermore it reduces problems that could arise when the resource
   * wraps, or is wrapped, by another resource.
   *
   *
   * *Implementers of this interface are also strongly advised
   * to not have the `close` method throw [ ].*
   *
   * This exception interacts with a thread's interrupted status,
   * and runtime misbehavior is likely to occur if an `InterruptedException` is [ suppressed][Throwable.addSuppressed].
   *
   * More generally, if it would cause problems for an
   * exception to be suppressed, the `AutoCloseable.close`
   * method should not throw it.
   *
   *
   * Note that unlike the [close][java.io.Closeable.close]
   * method of [java.io.Closeable], this `close` method
   * is *not* required to be idempotent.  In other words,
   * calling this `close` method more than once may have some
   * visible side effect, unlike `Closeable.close` which is
   * required to have no effect if called more than once.
   *
   * However, implementers of this interface are strongly encouraged
   * to make their `close` methods idempotent.
   *
   * @throws Exception if this resource cannot be closed
   */
  override fun close() {
    eventTester?.onCreatedEvent?.removeListener(listener)
    eventTester?.onCreatedEvent?.removeListener(listener2)
  }
}

