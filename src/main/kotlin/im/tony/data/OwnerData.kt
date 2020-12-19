package im.tony.data

import im.tony.utils.empty

/*
ID
HomeownerName
TO_ADDRESS_1
TO_ADDRESS_2
ALT_ADDRESS_1
ALT_ADDRESS_2
 */

data class OwnerData(
  val homeId: String,
  val familyName: String,
  val propAddressLine1: String,
  val propAddressLine2: String,
  val altAddressLine1: String?,
  val altAddressLine2: String?
) {
  fun hasAltAddress() = altAddressLine1 != null && altAddressLine2 != null

  fun mapToString(input: String): String = when (input) {
    "%FAMILY_NAME%" -> "$familyName Family"
    "%ADDRESS_LINE1%" -> propAddressLine1
    "%ADDRESS_LINE2%" -> propAddressLine2
    "%ALT_ADDRESS_LINE1%" -> altAddressLine1 ?: String.empty
    "%ALT_ADDRESS_LINE2%" -> altAddressLine1 ?: String.empty
    else -> String.empty
  }

  @Suppress("UNCHECKED_CAST")
  fun getAddress(alt: Boolean = false): Pair<String, String> =
    if (alt)
      if (hasAltAddress())
        Pair(altAddressLine1, altAddressLine2) as Pair<String, String>
      else
        Pair("", "")
    else Pair(
      propAddressLine1,
      propAddressLine2
    )

  companion object {
    fun parse(cells: MutableList<String>): OwnerData {
      require(cells.isNotEmpty()) { "Cell array should not be empty." }
      require(cells.size in 1..6) { "Cell array size should be between 1 and 6." }

      val homeId = cells.removeFirstOrNull()
      require(homeId != null) { "Home ID could not be retrieved from cells." }
      val familyName = cells.removeFirstOrNull()
      require(familyName != null) { "Family Name could not be retrieved from cells." }
      val propAddressLine1 = cells.removeFirstOrNull()
      require(propAddressLine1 != null) { "Property Address Line 1 could not be retrieved from cells." }
      val propAddressLine2 = cells.removeFirstOrNull()
      require(propAddressLine2 != null) { "Property Address Line 2 could not be retrieved from cells." }
      val altAddressLine1 = cells.removeFirstOrNull()
      val altAddressLine2 = cells.removeFirstOrNull()

      return OwnerData(
        homeId,
        familyName,
        propAddressLine1,
        propAddressLine2,
        altAddressLine1,
        altAddressLine2,
      )
    }

    val tester1 by lazy {
      OwnerData(
        "18400GW",
        "Johnson",
        "18400 Gardenia Way",
        "Gaithersburg, MD 20879",
        null,
        null,
      )
    }

    val tester2 by lazy {
      OwnerData(
        "1CJC",
        "Michaelson",
        "1 Cape Jasmine Court",
        "Gaithersburg, MD 20879",
        "701 Monroe St. #203",
        "Rockville, MD 20850",
      )
    }

    val tester3 by lazy {
      OwnerData(
        "0ABC",
        "Satanson",
        "0-0 Atlanta Bread Court",
        "Wonderland, ZQ 69696",
        "Hey girl heyyyy",
        "Where you get all that from?!",
      )
    }
  }
}
