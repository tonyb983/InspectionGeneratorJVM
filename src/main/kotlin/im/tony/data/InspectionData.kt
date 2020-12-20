package im.tony.data

import im.tony.utils.empty
import io.konform.validation.Validation
import io.konform.validation.jsonschema.pattern
import tornadofx.toObservable

private typealias StringCreator = (InspectionData) -> String
typealias InspectionDataValidatorType = (InspectionData) -> Boolean

class OwnerNotFoundException(inspectionData: InspectionData) : RuntimeException("Unable to find Owner matching Inspection Data: $inspectionData")

data class InspectionData(
  val streetNumber: String,
  val streetName: String,
  val isGood: Boolean,
  val issues: MutableList<String> = mutableListOf()
) {
  val streetId: String = streetName.split(" ").map { it.trim().first().toUpperCase() }.joinToString("")
  val homeId: String = "${streetNumber.trim()}$streetId"
  val issueCount: Int = if (isGood) 0 else issues.size
  val failures: MutableList<String> = mutableListOf()

  val issuesObservable = issues.toObservable()
  val failuresObservable = failures.toObservable()

  fun mapToString(input: String): String = when (input) {
    "%STREET_NUM%" -> this.streetNumber
    "%STREET_NAME%" -> this.streetName
    "%ISSUE_LIST%" -> this.issues.joinToString("\n")
    else -> String.empty
  }

  companion object {
    fun parse(cells: MutableList<String>): InspectionData {
      require(cells.isNotEmpty()) { "Cell array should not be empty." }
      require(cells.size in 3..9) { "Cell array size should be between 3 and 9." }

      val num = cells.removeAt(0)
      val name = cells.removeAt(0)
      val good = cells.removeAt(0).let { it == "Yes" }

      return InspectionData(num, name, good, cells)
    }

    val tester1 by lazy {
      InspectionData(
        "18400",
        "",
        true,
      )
    }

    val tester2 by lazy {
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

    val tester3 by lazy {
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
  }
}

val InspectionDataValidator = Validation<InspectionData> {
  InspectionData::homeId required {
    pattern(Regex("[0-9]{1,5}[A-Z]{2,3}")) hint "Home ID should be the house number followed by the street name abbreviation i.e. 18451GW or 8454TRD or 14CJC"
  }
}


