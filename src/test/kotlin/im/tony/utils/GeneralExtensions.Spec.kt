package im.tony.utils

import io.kotest.assertions.withClue
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.ints.shouldBeInRange
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldHaveLength
import io.kotest.matchers.string.shouldHaveLineCount
import io.kotest.matchers.string.shouldHaveMaxLength
import io.kotest.matchers.string.shouldHaveMinLength
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import io.kotest.property.checkAll
import javafx.util.Duration

class GeneralExtensionTests : DescribeSpec({
  describe("General Extension Tests") {
    describe("String extension functions:") {
      describe("- levenshtein") {
        it("Should return 0 when comparing the same string.") {
          val string = "String"
          val result = string.levenshtein("String")
          result shouldBeExactly 0
        }
        it("Should return 100 when comparing completely dissimilar strings.") {
          val string1 = "abcdefghijklm".repeat(8)
          val string2 = "nopqrstuvwxyz".repeat(8)
          val result = string1.levenshtein(string2)
          println("Levenshtein Result: $result")
          result shouldBeGreaterThan 100
        }
        it("It should be in range for any two strings.") {
          checkAll<String, String> { s1: String, s2: String ->
            val result = s1.levenshtein(s2)
            result shouldBeInRange 0..(maxOf(s1.length, s2.length))
          }
        }
      }

      describe("- splitInHalf") {
        it("Should correctly split a string in half(ish).") {
          val (s1a, s2a) = "Sometime".splitInHalf()
          s1a.toString().also { s1 ->
            s2a.toString().also { s2 ->
              s1 shouldHaveLineCount 1
              s2 shouldHaveLineCount 1
              s1 shouldHaveLength 4
              s2 shouldHaveLength 4
              s1 shouldBe "Some"
              s2 shouldBe "time"
            }
          }

          val (s1b, s2b) = "Something".splitInHalf()
          s1b.toString().also { s1 ->
            s2b.toString().also { s2 ->
              s1 shouldHaveLineCount 1
              s2 shouldHaveLineCount 1
              s1 shouldHaveLength 4
              s2 shouldHaveLength 5
              s1 shouldBe "Some"
              s2 shouldBe "thing"
            }
          }

          val (s1c, s2c) = "I".splitInHalf()
          s1c.toString().also { s1 ->
            s2c.toString().also { s2 ->
              s1 shouldHaveLineCount 0
              s2 shouldHaveLineCount 1
              s1 shouldHaveLength 0
              s2 shouldHaveLength 1
              s1 shouldBe ""
              s2 shouldBe "I"
            }
          }

          val (s1d, s2d) = "".splitInHalf()
          s1d.toString().also { s1 ->
            s2d.toString().also { s2 ->
              s1 shouldHaveLineCount 0
              s2 shouldHaveLineCount 0
              s1 shouldHaveLength 0
              s2 shouldHaveLength 0
              s1 shouldBe ""
              s2 shouldBe ""
            }
          }
        }

        it("Part length should always be equal or off by one.") {
          checkAll<String> { string ->
            val (first, last) = string.splitInHalf()

            if (string.length % 2 == 0) {
              first.toString() shouldHaveLength last.length
            } else {
              last.toString() shouldHaveLength (first.length + 1)
            }
          }
        }
      }

      describe("- breakTextEvery") {
        // 581 Chars total
        // 75 Chars
        val longParagraph = "RegExr was created by gskinner.com, and is proudly hosted by Media Temple. " +
          // 184 Chars
          "Edit the Expression & Text to see matches. Roll over matches or the expression for details. PCRE & JavaScript flavors of RegEx are supported. Validate your expression with Tests mode. " +
          // 165 Chars
          "The side bar includes a Cheatsheet, full Reference, and Help. You can also Save & Share with the Community, and view patterns you create or favorite in My Patterns. " +
          // 157 Chars
          "Explore results with the Tools below. Replace & List output custom results. Details lists capture groups. Explain describes your expression in plain English."

        // 80 chars
        val shortParagraph = "Here is some text. It is not very long. It should probably be longer, but I am -"

        // 195 Chars
        val shortWords = "" +
          "It it it it it it it it it it " +
          "it it it it it it it it it it " +
          "it it it it it it it it it it " +
          "it it it it it it it it it it " +
          "it it it it it it it it it it " +
          "it it it it it it it it it it " +
          "it it it it"

        val mediumWords = "Somethingsomethingsometh Somethingsomethingsometh"

        // 147 Chars, word = 36
        val bigWords =
          "Somethingsomethingsomethingsomething Somethingsomethingsomethingsomething Somethingsomethingsomethingsomething Somethingsomethingsomethingsomething"

        it("Should fail fast and silent") {
          "Something".breakTextEvery(-1, 100) shouldBe "Something"
          "Something".breakTextEvery(1, -100) shouldBe "Something"
          "Something".breakTextEvery(5, 4) shouldBe "Something"
          "Something".breakTextEvery(5, 5) shouldBe "Some-${System.lineSeparator()}thing"
        }

        //======================================
        //    WARNING - BRITTLE TESTS AHEAD
        //======================================

        it("Breaks the short paragraph correctly.") {
          val short = shortParagraph.breakTextEvery(20, 22)
          short shouldHaveMinLength shortParagraph.length
          short shouldHaveMaxLength shortParagraph.length + 6
          short shouldHaveLineCount 4
        }

        it("Should put smaller words on the same line when the word count allows it.") {
          val broken = shortWords.breakTextEvery(20, 25)
          broken shouldHaveLineCount 8
          broken shouldHaveMinLength (shortWords.length + 7)
        }

        it("Should correctly break big ass words.") {
          val broken = bigWords.breakTextEvery(10, 15)
          broken shouldHaveLineCount 13
        }

        it("Should work with very small limits.") {
          val broken = mediumWords.breakTextEvery(5, 8)
          broken shouldHaveLineCount 9
          broken shouldHaveLength (mediumWords.length + 44)
        }

        it("Big word but half will fit.") {
          val broken = mediumWords.breakTextEvery(10, 12)
          broken shouldHaveLineCount 4
          broken shouldHaveLength (mediumWords.length + 8)
        }

        it("Should hit the final if block.") {
          val broken = "This will hopefullywo.".breakTextEvery(11, 13)
          broken shouldHaveLineCount 2
        }
      }
    }

    describe("javafx.Distance extension function tests:") {
      describe("- isPositive") {
        it("Should return true when duration is positive.") {
          withClue("1.0 seconds Duration") {
            Duration.seconds(1.0).isPositive().shouldBeTrue()
          }
          withClue("MAX_VALUE seconds Duration") {
            Duration.seconds(Double.MAX_VALUE).isPositive().shouldBeTrue()
          }
          withClue("Indefinite Duration") {
            Duration.INDEFINITE.isPositive().shouldBeTrue()
          }
          withClue("Negative Duration") {
            Duration.seconds(-5.0).isPositive().shouldBeFalse()
          }
        }
        it("Should be correct for all possible Doubles.") {
          checkAll<Double> { double ->
            if (double > (Double.epsilon * Double.epsilon) || double < 0) {
              Duration.millis(double).isPositive() shouldBe (double > 0)
              Duration.seconds(double).isPositive() shouldBe (double > 0)
              Duration.minutes(double).isPositive() shouldBe (double > 0)
              Duration.hours(double).isPositive() shouldBe (double > 0)
            }
          }
        }
      }

      describe("- isNegative") {
        it("Should return true when duration is negative.") {
          withClue("-1.0 seconds Duration") {
            Duration.seconds(-1.0).isNegative().shouldBeTrue()
          }
          withClue("MAX_VALUE seconds Duration") {
            Duration.seconds(Double.MAX_VALUE).isNegative().shouldBeFalse()
          }
          withClue("Indefinite Duration") {
            Duration.INDEFINITE.isNegative().shouldBeFalse()
          }
          withClue("Negative Duration") {
            Duration.seconds(-5.0).isNegative().shouldBeTrue()
          }
          withClue("Positive Duration") {
            Duration.seconds(5.0).isNegative().shouldBeFalse()
          }
        }
        it("Should be correct for all possible Doubles.") {
          checkAll<Double> { double ->
            if (double >= (Double.epsilon * 2) || double < 0) {
              Duration.millis(double).isNegative() shouldBe (double < 0)
              Duration.seconds(double).isNegative() shouldBe (double < 0)
              Duration.minutes(double).isNegative() shouldBe (double < 0)
              Duration.hours(double).isNegative() shouldBe (double < 0)
            }
          }
        }
      }

      describe("- ensurePositive") {
        it("Should return this when positive.") {
          val original = Duration.millis(1000.0)
          val checked = original.ensurePositive()

          checked shouldBeSameInstanceAs original
        }
        it("Should return a new duration when negative.") {
          val original = Duration.millis(-1000.0)
          val checked = original.ensurePositive()

          checked shouldNotBeSameInstanceAs original
        }
      }
    }

    describe("Double equality functions:") {
      describe("- equalsDeltaSimple") {
        it("Should be equal for doubles that should be the same.") {
          val d1 = 0.8 + 0.5 + 0.2 + 0.3 + 0.7 // 25.0
          val d2 = 0.3 + 0.3 + 0.3 + 0.3 + 0.3 + 0.25 + 0.25 + 0.25 + 0.25 // 25.0

          withClue("Regular Comparison") {
            d1.equalsDeltaSimple(d2).shouldBeTrue()
          }
          withClue("Infix Eq") { (d1 eqDs d2).shouldBeTrue() }
          withClue("Infix Gte") { (d1 gteDs d2).shouldBeTrue() }
          val d3 = d1 - (Double.epsilon * 10)
          withClue("$d3 gte $d2") {
            (d3 gteDs d2).shouldBeFalse()
          }
        }
      }
      describe("- equalsDeltaComplex") {
        it("Should be equal for doubles that should be the same.") {
          val d1 = 0.8 + 0.5 + 0.2 + 0.3 + 0.7 // 25.0
          val d2 = 0.3 + 0.3 + 0.3 + 0.3 + 0.3 + 0.25 + 0.25 + 0.25 + 0.25 // 25.0

          withClue("Regular Comparison") {
            d1.equalsDeltaComplex(d2).shouldBeTrue()
          }
          withClue("Infix Eq") { (d1 eqDc d2).shouldBeTrue() }
          withClue("Infix Gte") { (d1 gteDc d2).shouldBeTrue() }
          val d3 = d1 - (Double.epsilon * 10)
          withClue("$d3 gte $d2") {
            (d3 gteDc d2).shouldBeFalse()
          }
        }
      }
      describe("- equalsRelative") {
        it("Should be equal for doubles that should be the same.") {
          val d1 = 0.8 + 0.5 + 0.2 + 0.3 + 0.7 // 25.0
          val d2 = 0.3 + 0.3 + 0.3 + 0.3 + 0.3 + 0.25 + 0.25 + 0.25 + 0.25 // 25.0

          withClue("Regular Comparison") {
            d1.equalsRelative(d2).shouldBeTrue()
          }
          withClue("Infix Eq") { (d1 eqRel d2).shouldBeTrue() }
          withClue("Infix Gte") { (d1 gteRel d2).shouldBeTrue() }
          val d3 = d1 - (Double.epsilon * 10)
          withClue("$d3 gte $d2") {
            (d3 gteRel d2).shouldBeFalse()
          }
        }
      }
      describe("- equalsFixedDeltaSimple") {
        it("Should be equal for doubles that should be the same.") {
          val d1 = 0.8 + 0.5 + 0.2 + 0.3 + 0.7 // 25.0
          val d2 = 0.3 + 0.3 + 0.3 + 0.3 + 0.3 + 0.25 + 0.25 + 0.25 + 0.25 // 25.0

          withClue("Regular Comparison") {
            d1.equalsFixedDeltaSimple(d2).shouldBeTrue()
          }
          withClue("Infix Eq") { (d1 eqFds d2).shouldBeTrue() }
          withClue("Infix Gte") { (d1 gteFds d2).shouldBeTrue() }
          val d3 = d1 - (Double.epsilon * 10)
          withClue("$d3 gte $d2") {
            (d3 gteFds d2).shouldBeFalse()
          }
        }
      }
      describe("- equalsFixedDeltaComplex") {
        it("Should be equal for doubles that should be the same.") {
          val d1 = 0.8 + 0.5 + 0.2 + 0.3 + 0.7 // 25.0
          val d2 = 0.3 + 0.3 + 0.3 + 0.3 + 0.3 + 0.25 + 0.25 + 0.25 + 0.25 // 25.0

          withClue("Regular Comparison") {
            d1.equalsFixedDeltaComplex(d2).shouldBeTrue()
          }
          withClue("Infix Eq") { (d1 eqFdc d2).shouldBeTrue() }
          withClue("Infix Gte") { (d1 gteFdc d2).shouldBeTrue() }
          val d3 = d1 - (Double.epsilon * 10)
          withClue("$d3 gte $d2") {
            (d3 gteFdc d2).shouldBeFalse()
          }
        }
      }
      describe("- equalsFixedRelative") {
        it("Should be equal for doubles that should be the same.") {
          val d1 = 0.8 + 0.5 + 0.2 + 0.3 + 0.7 // 25.0
          val d2 = 0.3 + 0.3 + 0.3 + 0.3 + 0.3 + 0.25 + 0.25 + 0.25 + 0.25 // 25.0

          withClue("Regular Comparison") {
            d1.equalsFixedRelative(d2).shouldBeTrue()
          }
          withClue("Infix Eq") { (d1 eqFRel d2).shouldBeTrue() }
          withClue("Infix Gte") { (d1 gteFRel d2).shouldBeTrue() }
          val d3 = d1 - (Double.epsilon * 10)
          withClue("$d3 gte $d2") {
            (d3 gteFRel d2).shouldBeFalse()
          }
        }
      }
    }

  }
})
