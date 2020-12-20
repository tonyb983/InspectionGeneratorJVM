package im.tony.data

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe

class InspectionDataTests : DescribeSpec({
  describe("InspectionData Tests") {
    it("Should create a valid InspectionData object.") {
      val id = InspectionData("12345", "Holland Drive", isGood = true)

      id.should {
        it.streetNumber shouldBe "12345"
        it.streetName shouldBe "Holland Drive"
      }
    }

    it("Should create correct calculated fields.") {
      val id = InspectionData("12345", "Holland Drive", isGood = true)

      id.streetId shouldBe "HD"
      id.homeId shouldBe "12345HD"
      id.issueCount shouldBe 0
    }

    it("Should parse a String Array into a valid object.") {

    }
  }
})
