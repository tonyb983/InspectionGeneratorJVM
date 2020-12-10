package im.tony.data

/*
ID
HomeownerName
TO_ADDRESS_1
TO_ADDRESS_2
ALT_ADDRESS_1
ALT_ADDRESS_2
 */

public data class OwnerData(
  val homeId: String,
  val familyName: String,
  val propAddressLine1: String,
  val propAddressLine2: String,
  val altAddressLine1: String?,
  val altAddressLine2: String?
) {
  public companion object {
    public fun parse(cells: MutableList<String>): OwnerData {
      require(cells.isNotEmpty()) { "Cell array should not be empty." }
      require(cells.size in 1..6) { "Cell array size should be between 1 and 14." }

      val homeId = cells.removeFirstOrNull()
      require(homeId != null) { "Home ID could not be retrieved from cells." }
      val familyName = cells.removeFirstOrNull()
      require(familyName != null) { "Family Name could not be retrieved from cells." }
      val propAddressLine1 = cells.removeFirstOrNull()
      require(propAddressLine1 != null) { "Property Address Line 1 could not be retrieved from cells." }
      val propAddressLine2 = cells.removeFirstOrNull()
      require(propAddressLine2 != null) { "Property Address Line 2 could not be retrieved from cells." }
      val altAddressLine1 = if (cells.size > 0) cells.removeAt(0) else null
      val altAddressLine2 = if (cells.size > 0) cells.removeAt(0) else null

      return OwnerData(
        homeId,
        familyName,
        propAddressLine1,
        propAddressLine2,
        altAddressLine1,
        altAddressLine2,
      )
    }
  }
}
