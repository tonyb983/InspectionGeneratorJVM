package im.tony.utils

public class FixedSizeDeque<T>(public val capacity: Int) : MutableCollection<T> {
  private val deque: ArrayDeque<T> = ArrayDeque()

  /**
   * Returns the size of the collection.
   */
  override val size: Int
    get() = deque.size

  /**
   * Checks if the specified element is contained in this collection.
   */
  override fun contains(element: T): Boolean = deque.contains(element)

  /**
   * Checks if all elements in the specified collection are contained in this collection.
   */
  override fun containsAll(elements: Collection<T>): Boolean = deque.containsAll(elements)

  /**
   * Returns `true` if the collection is empty (contains no elements), `false` otherwise.
   */
  override fun isEmpty(): Boolean = deque.isEmpty()

  /**
   * Adds the specified element to the collection.
   *
   * @return `true` if the element has been added, `false` if the collection does not support duplicates
   * and the element is already contained in the collection.
   */
  override fun add(element: T): Boolean {
    if (deque.size >= capacity) {
      deque.removeFirst()
    }

    val before = deque.size
    deque.addLast(element)
    return deque.size > before
  }

  /**
   * Adds all of the elements of the specified collection to this collection.
   *
   * @return `true` if any of the specified elements was added to the collection, `false` if the collection was not modified.
   */
  override fun addAll(elements: Collection<T>): Boolean {
    val targetSize = elements.size
    val currentSize = deque.size
    var success = false

    if (targetSize == capacity) {
      deque.clear()
      val s1 = deque.size
      deque.addAll(elements)
      success = deque.size > s1
    } else if (targetSize > capacity) {
      deque.clear()
      val s1 = deque.size
      deque.addAll(elements.drop(targetSize - capacity))
      success = deque.size > s1
    } else if (targetSize + currentSize > capacity) {
      while ((targetSize + deque.size) > capacity) {
        deque.removeFirstOrNull()
      }
      val s1 = deque.size
      deque.addAll(elements)
      success = deque.size > s1
    } else {
      val s1 = deque.size
      deque.addAll(elements)
      success = deque.size > s1
    }

    require(capacity > deque.size) { "Deque size is greater than capacity. Start Size: $currentSize Target Size: $targetSize Capacity: $capacity End Size: ${deque.size}" }
    return success
  }

  /**
   * Removes all elements from this collection.
   */
  override fun clear(): Unit = deque.clear()

  override fun iterator(): MutableIterator<T> = deque.iterator()

  /**
   * Removes a single instance of the specified element from this
   * collection, if it is present.
   *
   * @return `true` if the element has been successfully removed; `false` if it was not present in the collection.
   */
  override fun remove(element: T): Boolean = deque.remove(element)

  /**
   * Removes all of this collection's elements that are also contained in the specified collection.
   *
   * @return `true` if any of the specified elements was removed from the collection, `false` if the collection was not modified.
   */
  override fun removeAll(elements: Collection<T>): Boolean = deque.removeAll(elements)

  /**
   * Retains only the elements in this collection that are contained in the specified collection.
   *
   * @return `true` if any element was removed from the collection, `false` if the collection was not modified.
   */
  override fun retainAll(elements: Collection<T>): Boolean = deque.retainAll(elements)

  public fun addFirst(element: T): Unit {
    if (deque.size >= capacity) {
      deque.removeFirstOrNull()
    }

    deque.addFirst(element)
  }

  public fun addLast(element: T): Unit {
    if (deque.size >= capacity) {
      deque.removeFirstOrNull()
    }

    deque.addLast(element)
  }

  public fun removeFirst(): T = deque.removeFirst()
  public fun removeFirstOrNull(): T? = deque.removeFirstOrNull()
  public fun removeLast(): T = deque.removeLast()
  public fun removeLastOrNull(): T? = deque.removeLastOrNull()
}
