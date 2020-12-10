package im.tony.data

import im.tony.utils.isOneOf

/*
Street Number
Street Name
IsGood
Issues
 */

private typealias StringCreator = (InspectionData) -> String
public typealias InspectionDataValidatorType = (InspectionData) -> Boolean

public data class InspectionDataValidator(public val failureMessage: StringCreator, public val validator: InspectionDataValidatorType)

public data class InspectionData(val streetNumber: String, val streetName: String, val isGood: Boolean, val issues: List<String> = listOf()) {
  val streetId: String = streetName.split(" ").map { it.trim().first().toUpperCase() }.joinToString("")
  val homeId: String = "${streetNumber.trim()}$streetId"
  val issueCount: Int = if (isGood) 0 else issues.size
  val isValid: Boolean = validators.all { it.validator.invoke(this) }
  var failures: MutableList<String> = mutableListOf()
    private set

  private fun checkAgainst(idv: InspectionDataValidator): Boolean {
    val result = idv.validator.invoke(this)

    if (!result) {
      failures.add(idv.failureMessage.invoke(this))
    }

    return result
  }

  public companion object {
    public fun parse(cells: MutableList<String>): InspectionData {
      require(cells.isNotEmpty()) { "Cell array should not be empty." }
      require(cells.size in 3..9) { "Cell array size should be between 3 and 9." }

      val num = cells.removeAt(0)
      val name = cells.removeAt(0)
      val good = cells.removeAt(0).let { it.toLowerCase() == "yes" }

      return InspectionData(num, name, good, cells)
    }

    private fun idv(message: String, validator: InspectionDataValidatorType): InspectionDataValidator =
      InspectionDataValidator({ message }, validator)

    private fun idv(validator: InspectionDataValidatorType, msgGetter: StringCreator) = InspectionDataValidator(msgGetter, validator)

    public val validators: MutableList<InspectionDataValidator> = mutableListOf(
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
      }) { "Recieved invalid street abbreviation '${it.streetId}'." },
    )
  }
}


