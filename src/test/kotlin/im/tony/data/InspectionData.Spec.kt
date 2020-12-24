package im.tony.data

import im.tony.utils.empty
import io.kotest.assertions.konform.shouldBeInvalid
import io.kotest.assertions.konform.shouldBeValid
import io.kotest.assertions.konform.shouldContainError
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

private val testInspection1 by lazy {
  InspectionData(
    "18400",
    "Flower Hill Way",
    true,
  )
}
private val testInspection2 by lazy {
  InspectionData(
    "1",
    "Cape Jasmine Court",
    false,
    mutableListOf(
      "Siding - Mildew / Dirt on siding.",
      "Rotten wood trim needs to be repaired / replaced.",
      "Non-compliant fence on side facing Westwind neighbor.",
    )
  )
}
private val testInspection3 by lazy {
  InspectionData(
    "0",
    "Atlanta Bread Court",
    true,
    mutableListOf(
      "This should not be here.",
      "No seriously your house is beautiful this is a mistake.",
      "ABANDON ALL HOPE YE WHO ENTER HERE",
    )
  )
}

class InspectionDataTests : DescribeSpec({
  describe("InspectionData Tests") {
    describe("General Tests") {
      it("Should create a valid InspectionData object.") {
        val id = InspectionData("12345", "Holland Drive", isGood = true)

        id.streetNumber shouldBe "12345"
        id.streetName shouldBe "Holland Drive"
      }

      it("Should create correct calculated fields.") {
        val id = InspectionData("12345", "Holland Drive", isGood = true)

        id.streetId shouldBe "HD"
        id.homeId shouldBe "12345HD"
        id.issueCount shouldBe 0
      }

      it("Maps correctly to strings") {
        testInspection1.mapToString("%STREET_NUM%") shouldBe testInspection1.streetNumber
        testInspection1.mapToString("%STREET_NAME%") shouldBe testInspection1.streetName
        testInspection1.mapToString("%ISSUE_LIST%") shouldBe testInspection1.issues.joinToString("\n")
        testInspection1.mapToString("%BAD_INPUT%") shouldBe String.empty
      }

      it("Should parse a valid String Array into a valid object.") {
        val array1 = listOf("18400", "Gardenia Way", "Yes")
        val array2 = listOf("8454", "Tea Rose Drive", "No", "Mildew on siding.", "Rotting / deteriorating wooden trim at roofline.")
        val i1 = InspectionData.parse(array1)
        val i2 = InspectionData.parse(array2)
        InspectionDataValidator shouldBeValid i1
        InspectionDataValidator shouldBeValid i2
      }
    }

    describe("Validator Tests") {
      it("Should properly validate the test inspections.") {
        InspectionDataValidator shouldBeValid testInspection1
        InspectionDataValidator shouldBeValid testInspection2
        InspectionDataValidator shouldBeInvalid testInspection3
      }

      it("Should catch invalid home and street IDs.") {
        InspectionDataValidator.shouldBeInvalid(
          InspectionData(
            "123456",
            "Club House Way",
            isGood = true,
            issues = mutableListOf(
              "This shouldn't be here.",
              "I don't belong!",
              "I'm extra.",
              "Like, really extra.",
              "Extra af you might say.",
              "As the kids would say.",
              "Immmmmmmm Oolllllddddddd"
            )
          )
        ) {
          it.shouldContainError(
            InspectionData::homeId,
            "Home ID should be the house number followed by the street name abbreviation. I.e. 18451GW or 8454TRD or 14CJC."
          )
          it.shouldContainError(InspectionData::streetId, "must match the expected pattern")
          it.shouldContainError(InspectionData::streetId, "must be one of: 'CJC', 'CJW', 'FHW', 'GC', 'GW', 'TRC', 'TRD', 'TRP'")
          it.shouldContainError(InspectionData::streetNumber, "Street number should be a 1-5 digit number in string form.")
          it.shouldContainError(InspectionData::streetName, "Street name should match one of the valid Westwind street names.")
          it.shouldContainError(InspectionData::issues, "Issues should have at most 5 items.")
          it.shouldContainError(InspectionData::issueCount, "IssueCount should be at most 5.")
        }
      }
    }
  }
})
