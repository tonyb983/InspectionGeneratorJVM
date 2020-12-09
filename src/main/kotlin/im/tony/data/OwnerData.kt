package im.tony.data

/*
Account #
Owner Type
Owner Display Name
Property Street Name
Property Street Number
Property City
Property State
Property Postal Code
ID
HomeownerName
TO_ADDRESS_1
TO_ADDRESS_2
ALT_ADDRESS_1
ALT_ADDRESS_2
 */

data class OwnerData(
  val accountNumber: String,
  val ownerType: String,
  val ownerName: String,
  val propertyStreetNum: String,
  val propertyStreetName: String,
  val propertyCity: String,
  val propertyState: String,
  val propertyZip: String,
  val homeId: String,
  val familyName: String,
  val propAddressLine1: String,
  val propAddressLine2: String,
  val altAddressLine1: String?,
  val altAddressLine2: String?
) {
  companion object {
    fun parse(cells: MutableList<String>): OwnerData {
      require(cells.isNotEmpty()) { "Cell array should not be empty." }
      require(cells.size in 1..14) { "Cell array size should be between 1 and 14." }

      return OwnerData(
        accountNumber = cells.removeAt(0),
        ownerType = cells.removeAt(0),
        ownerName = cells.removeAt(0),
        propertyStreetNum = cells.removeAt(0),
        propertyStreetName = cells.removeAt(0),
        propertyCity = cells.removeAt(0),
        propertyState = cells.removeAt(0),
        propertyZip = cells.removeAt(0),
        homeId = cells.removeAt(0),
        familyName = cells.removeAt(0),
        propAddressLine1 = cells.removeAt(0),
        propAddressLine2 = cells.removeAt(0),
        altAddressLine1 = if (cells.size > 0) cells.removeAt(0) else null,
        altAddressLine2 = if (cells.size > 0) cells.removeAt(0) else null
      )
    }
  }
}
