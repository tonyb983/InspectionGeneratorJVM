package im.tony.utils

import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.TimeUnit

public class NonBlockingFixedSizeQueue<E>(
  override val size: Int
) : ArrayBlockingQueue<E>(size) {

  @Synchronized
  override fun add(element: E): Boolean {
    if (super.size == size) {
      this.poll()
    }

    return this.offer(element)
  }

  /**
   * Inserts the specified element at the tail of this queue if it is
   * possible to do so immediately without exceeding the queue's capacity,
   * returning `true` upon success and `false` if this queue
   * is full.  This method is generally preferable to method [.add],
   * which can fail to insert an element only by throwing an exception.
   *
   * @throws NullPointerException if the specified element is null
   */
  override fun offer(e: E): Boolean {
    return super.offer(e)
  }

  /**
   * Inserts the specified element at the tail of this queue, waiting
   * up to the specified wait time for space to become available if
   * the queue is full.
   *
   * @throws InterruptedException {@inheritDoc}
   * @throws NullPointerException {@inheritDoc}
   */
  override fun offer(e: E, timeout: Long, unit: TimeUnit): Boolean {
    return super.offer(e, timeout, unit)
  }

  override fun poll(timeout: Long, unit: TimeUnit): E? {
    return super.poll(timeout, unit)
  }

  /**
   * Inserts the specified element at the tail of this queue, waiting
   * for space to become available if the queue is full.
   *
   * @throws InterruptedException {@inheritDoc}
   * @throws NullPointerException {@inheritDoc}
   */
  override fun put(e: E) {
    super.put(e)
  }

  public companion object {
    private const val serialVersionUID = -7772085623838075506L
  }
}
