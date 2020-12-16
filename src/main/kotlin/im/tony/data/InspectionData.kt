package im.tony.data

import im.tony.utils.empty
import im.tony.utils.isOneOf
import tornadofx.toObservable

/*
Street Number
Street Name
IsGood
Issues
 */

private typealias StringCreator = (InspectionData) -> String
typealias InspectionDataValidatorType = (InspectionData) -> Boolean

data class InspectionDataValidator(val failureMessage: StringCreator, val validator: InspectionDataValidatorType)

data class InspectionData(
  val streetNumber: String,
  val streetName: String,
  val isGood: Boolean,
  val issues: MutableList<String> = mutableListOf()
) {
  val streetId: String = streetName.split(" ").map { it.trim().first().toUpperCase() }.joinToString("")
  val homeId: String = "${streetNumber.trim()}$streetId"
  val issueCount: Int = if (isGood) 0 else issues.size
  val isValid: Boolean = validators.all { it.validator.invoke(this) }
  val failures: MutableList<String> = mutableListOf()

  val issuesObservable = issues.toObservable()
  val failuresObservable = failures.toObservable()

  private fun checkAgainst(idv: InspectionDataValidator): Boolean {
    val result = idv.validator.invoke(this)

    if (!result) {
      failures.add(idv.failureMessage.invoke(this))
    }

    return result
  }

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

    private fun idv(message: String, validator: InspectionDataValidatorType): InspectionDataValidator =
      InspectionDataValidator({ message }, validator)

    private fun idv(validator: InspectionDataValidatorType, msgGetter: StringCreator) = InspectionDataValidator(msgGetter, validator)

    val validators: MutableList<InspectionDataValidator> = mutableListOf(
      idv("Strings aren't empty.") {
        listOf(
          it.streetNumber.isNotBlank(),
          it.streetName.isNotBlank(),
          it.streetId.isNotBlank(),
          it.homeId.isNotBlank(),
        ).all { true }
      },
      idv("Street number is out of range.") { data ->
        data.streetNumber.toIntOrNull()?.run { this in 1..30 && this in 8300..8500 && this in 18300..18600 } ?: false
      },
      idv({ d -> (d.isGood && d.issueCount <= 0) || (!d.isGood && d.issueCount in 1..5) }) { "Issue count invalid. IsGood: ${it.isGood} | Issue Count: ${it.issueCount}" },
      idv({ d ->
        d.streetId.isOneOf(
          "CJC",
          "CJW",
          "GC",
          "GW",
          "TRD",
          "TRC",
          "TRP",
          "FHW"
        )
      }) { "Received invalid street abbreviation '${it.streetId}'." },
    )

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


