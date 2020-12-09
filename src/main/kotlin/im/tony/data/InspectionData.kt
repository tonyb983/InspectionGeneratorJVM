package im.tony.data

/*
Street Number
Street Name
IsGood
Issues
 */

typealias InspectionDataValidatorType = (InspectionData) -> Boolean
inline class InspectionDataValidator(val validator: InspectionDataValidatorType)

data class InspectionData(val streetNumber: String, val streetName: String, val isGood: Boolean, val issues: List<String> = listOf()) {
  val streetId = streetName.split(" ").map { it.trim().first().toUpperCase() }.joinToString("")
  val homeId: String = "${streetNumber.trim()}$streetId"
  val issueCount = if(isGood) 0 else issues.size
  val isValid: Boolean

  init {
      isValid = validators.all { it.validator.invoke(this) }
  }

  private fun validate() {

  }

  companion object {
    fun parse(cells: MutableList<String>): InspectionData {
      require(cells.isNotEmpty()) { "Cell array should not be empty." }
      require(cells.size in 3..9) { "Cell array size should be between 3 and 9." }

      val num = cells.removeAt(0)
      val name = cells.removeAt(0)
      val good = cells.removeAt(0).let { it.toLowerCase() == "yes" }

      return InspectionData(num, name, good, cells)
    }

    private fun idv(validator: InspectionDataValidatorType): InspectionDataValidator = InspectionDataValidator(validator)

    val validators: MutableList<InspectionDataValidator> = mutableListOf(
      idv { data -> data.streetNumber.toIntOrNull()?.run { this in 1..30 && this in 8300..8500 && this in 18300..18600 } ?: false },
      idv { d -> (d.isGood && d.issueCount <= 0) || (!d.isGood && d.issueCount in 1..5)}
    )
  }
}


