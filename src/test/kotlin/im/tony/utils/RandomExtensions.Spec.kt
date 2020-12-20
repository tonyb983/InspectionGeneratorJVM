package im.tony.utils

import io.github.serpro69.kfaker.Faker
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.string.shouldContainInOrder
import io.kotest.matchers.string.shouldHaveMinLength
import io.kotest.property.Arb
import io.kotest.property.arbitrary.constant
import io.kotest.property.checkAll
import java.util.logging.Level

class RandomExtensionTests : DescribeSpec({
  describe("Random Extension Tests") {
    describe("ESports quote extension function") {
      it("Works") {
        val s = Faker().eSport.quote()
        s shouldHaveMinLength 1
        s.shouldContainInOrder("Today", "of", "played", "in", "during the", "in the")
      }

      it("Works ... a lot") {
        checkAll(50, Arb.constant(0)) {
          Faker()
            .eSport
            .quote()
            .shouldContainInOrder("Today", "of", "played", "in", "during the", "in the")
        }
      }
    }

    describe("Random Log Level function") {
      val levels: List<Level> by lazy { listOf(Level.FINER, Level.FINE, Level.FINEST, Level.INFO, Level.WARNING, Level.SEVERE, Level.CONFIG) }
      it("Works") {
        val level = getRandomLogLevel(true)
        level shouldBeIn levels
      }
    }

    describe("Random Quote Function") {
      it("Works") {
        getRandomQuote(true) shouldHaveMinLength 1
        getRandomQuote(false) shouldHaveMinLength 1
      }
    }
  }
})
