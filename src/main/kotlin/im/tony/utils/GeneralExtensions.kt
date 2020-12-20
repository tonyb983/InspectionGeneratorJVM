@file:Suppress("unused")

package im.tony.utils

import javafx.util.Duration
import java.lang.StrictMath.*

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

/**
 * Recreation of **C#'s** [String].Empty
 */
val String.Companion.empty: String by lazy { "" }

/**
 * Gets a string ready to be *'hyphenized'* by splitting it in two.
 * If the [CharSequence.length] is an even number it will split directly
 * in half, if it is odd the first half will be one character shorter (to
 * allow for the dash).
 */
public fun CharSequence.splitInHalf(): Pair<CharSequence, CharSequence> {
  val mid = if (this.length % 2 != 0) (this.length - 1) / 2 else this.length / 2
  val first = this.subSequence(0, mid)
  val second = this.subSequence(mid, this.length)

  return first to second
}

/**
 * Breaks text at least every [shortestLine] [Char] and not more than
 * every [longestLine] [Char] using [breakWith] to create a new line.
 * The function uses [breakOn] and [ignoreCase] to split the initial
 * [String] with [String.split].<br/>
 * *If [shortestLine] or [longestLine] are less than zero or [longestLine] is
 * less than [shortestLine] it will fail fast and silently and return the original [String]*
 */
public fun String.breakTextEvery(
  shortestLine: Int,
  longestLine: Int,
  breakOn: String = " ",
  ignoreCase: Boolean = false,
  breakWith: String = System.lineSeparator()
): String {
  if (shortestLine < 0 || longestLine < 0 || longestLine < shortestLine) {
    return this
  }

  val words = this.split(breakOn, ignoreCase = ignoreCase)
  val wordCount = words.size
  val sb = StringBuilder()
  var currentLineLength = 0

  fun appendSpaceIfMore(isLast: Boolean) {
    if (isLast) return

    sb.append(" ")
    currentLineLength += 1
  }

  for ((index, word) in words.withIndex()) {
    val isLastWord = index + 1 >= wordCount
    when (val afterCurrentSize = currentLineLength + word.length) {
      in 0 until shortestLine -> { // Too short.
        sb.append(word)
        currentLineLength += word.length
        appendSpaceIfMore(isLastWord)
      }
      in shortestLine..longestLine -> { // In Range
        when {
          isLastWord -> {
            // If this is the last word we will append and be done
            sb.append(word)
            // Unnecessary but safe
            currentLineLength += word.length
          }
          words[index + 1].length + afterCurrentSize + 1 < longestLine -> {
            // If the next word will fit we won't break
            sb.append("$word ")
            currentLineLength += (word.length + 1)
          }
          else -> {
            // If the next word won't fit we will break here
            sb.append("$word$breakWith")
            currentLineLength = 0
          }
        }
      }
      // If we're too big to fit
      else -> {
        // Here half the word will fit on the current line so we hyphen.
        if ((word.length / 2) + currentLineLength <= longestLine) {
          val (first, second) = word.splitInHalf()
          sb.append("$first-$breakWith")
          sb.append(second)
          currentLineLength = second.length
          appendSpaceIfMore(isLastWord)
        } else {
          // Otherwise we break immediately
          sb.append(breakWith)
          currentLineLength = 0
          if (word.length > longestLine) {
            if ((word.length / 2) > longestLine) {
              // Word is huge so we multi-hyphen
              val targetLength = ((longestLine - shortestLine) * 0.9).toInt() + shortestLine
              val parts = (word.length / ((shortestLine + longestLine) * 0.5)).toInt()
              for (i in 0 until parts) {
                val part = word.subSequence(i * targetLength, (i + 1) * targetLength)
                sb.append("$part-$breakWith")
              }
              val last = word.subSequence((parts - 1) * shortestLine, word.length)
              sb.append(last)
              currentLineLength = last.length
              appendSpaceIfMore(isLastWord)
            } else {
              val (first, second) = word.splitInHalf()
              sb.append("$first-$breakWith")
              sb.append(second)
              currentLineLength = second.length
              appendSpaceIfMore(isLastWord)
            }
          } else {
            sb.append(word)
            currentLineLength = word.length
            appendSpaceIfMore(isLastWord)
          }
        }
      }
    }
  }

  return sb.toString()
}

/**
 * Calculates the **Levenshtein Distance** between this [CharSequence] and another [CharSequence].
 * Returns an [Int] between 0 and N (the length of the shortest string), with 0 representing
 * an exact match and N being the maximum.
 */
public fun CharSequence.levenshtein(other: CharSequence): Int {
  val lhsLength = this.length
  val rhsLength = other.length

  var cost = IntArray(lhsLength + 1) { it }
  var newCost = IntArray(lhsLength + 1) { 0 }

  for (i in 1..rhsLength) {
    newCost[0] = i

    for (j in 1..lhsLength) {
      val editCost = if (this[j - 1] == other[i - 1]) 0 else 1

      val costReplace = cost[j - 1] + editCost
      val costInsert = cost[j] + 1
      val costDelete = newCost[j - 1] + 1

      newCost[j] = minOf(costInsert, costDelete, costReplace)
    }

    val swap = cost
    cost = newCost
    newCost = swap
  }

  return cost[lhsLength]
}

/**
 * ### Value = *0.000001*
 * Const value used to compare doubles.
 */
val Double.Companion.epsilon: Double by lazy { 0.00000001 }

/**
 * Calculates the [abs] of (this - [other]) and compares them to [epsilon].
 */
fun Double.equalsDeltaSimple(other: Double) = abs(this - other) < Double.epsilon

/**
 * Convenience function to call [Double.equalsDeltaSimple].
 */
infix fun Double.eqDs(other: Double) = this.equalsDeltaSimple(other)

/**
 * Convenience function to compare greater than or call [Double.equalsDeltaSimple].
 */
infix fun Double.gteDs(other: Double) = this > other || this.eqDs(other)

/**
 * Calculates the [abs] of (this - [other]) and compares to [epsilon] times
 * the [max] of [abs] of this and [other].
 */
fun Double.equalsDeltaComplex(other: Double) = abs(this - other) < Double.epsilon * max(abs(this), abs(other))

/**
 * Convenience function to call [Double.equalsDeltaComplex].
 */
infix fun Double.eqDc(other: Double) = this.equalsDeltaComplex(other)

/**
 * Convenience function to compare greater than or call [Double.equalsDeltaComplex].
 */
infix fun Double.gteDc(other: Double) = this > other || this.eqDc(other)

/**
 * Calculates [abs] of (this - [other]) and compares to [max] of [ulp] of
 * this and [other] times 2.
 */
fun Double.equalsRelative(other: Double) = abs(this - other) < max(ulp(this), ulp(other)) * 2

/**
 * Convenience function to call [Double.equalsRelative].
 */
infix fun Double.eqRel(other: Double) = this.equalsRelative(other)

/**
 * Convenience function to compare greater than or call [Double.equalsRelative].
 */
infix fun Double.gteRel(other: Double) = this > other || this.eqRel(other)

/**
 * Calculates the [abs] of (this/[other] - 1) and compares to [epsilon].
 */
fun Double.equalsFixedDeltaSimple(other: Double) = abs(this / other - 1) < Double.epsilon

/**
 * Convenience function to call [Double.equalsFixedDeltaSimple].
 */
infix fun Double.eqFds(other: Double) = this.equalsFixedDeltaSimple(other)

/**
 * Convenience function to compare greater than or call [Double.equalsFixedDeltaSimple].
 */
infix fun Double.gteFds(other: Double) = this > other || this.eqFds(other)

/**
 * Calculates the [abs] of (this/[other] - 1) and compares to [epsilon] times
 * [max] of [abs] of this and [other].
 */
fun Double.equalsFixedDeltaComplex(other: Double) = abs(this / other - 1) < Double.epsilon * max(abs(this), abs(other))

/**
 * Convenience function to call [Double.equalsFixedDeltaComplex].
 */
infix fun Double.eqFdc(other: Double) = this.equalsFixedDeltaComplex(other)

/**
 * Convenience function to compare greater than or call [Double.equalsFixedDeltaComplex].
 */
infix fun Double.gteFdc(other: Double) = this > other || this.eqFdc(other)

/**
 * Calculates the [abs] value of (this/[other] - 1) and compares to [max] of [ulp]
 * of this and [other] times 2.
 */
fun Double.equalsFixedRelative(other: Double) = abs(this / other - 1) < max(ulp(this), ulp(other)) * 2

/**
 * Convenience function to call [Double.equalsFixedRelative].
 */
infix fun Double.eqFRel(other: Double) = this.equalsFixedRelative(other)

/**
 * Convenience function to compare greater than or call [Double.equalsFixedRelative].
 */
infix fun Double.gteFRel(other: Double) = this > other || this.eqFRel(other)

/**
 * Returns *true* if this duration is greater than zero, *false* otherwise.
 */
public fun Duration.isPositive(): Boolean = this.toMillis() > 0

/**
 * Returns *true* if this duration is less than zero, *false* otherwise.
 */
public fun Duration.isNegative(): Boolean = this.toMillis() < 0

/**
 * If this [Duration] is zero or less than zero the given [default] [Duration] will be returned,
 * otherwise this [Duration] will be returned unchanged.
 * #### Uses [Duration.isPositive] to determine if the this duration is positive.
 */
public fun Duration.ensurePositive(default: Duration = Duration.seconds(3.0)): Duration = if (this.isPositive()) this else default

