package im.tony.data

import im.tony.utils.empty
import io.konform.validation.Validation
import io.konform.validation.jsonschema.maxLength
import io.konform.validation.jsonschema.minLength
import io.konform.validation.jsonschema.pattern

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
  val altAddressLine1: String? = null,
  val altAddressLine2: String? = null,
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
    fun parse(cells: Collection<String>): OwnerData {
      require(cells.isNotEmpty()) { "Cell array should not be empty." }
      require(cells.size in 1..6) { "Cell array size should be between 1 and 6." }

      val mut = cells.toMutableList()

      val homeId = mut.removeFirstOrNull()
      require(homeId != null) { "Home ID could not be retrieved from mut." }
      val familyName = mut.removeFirstOrNull()
      require(familyName != null) { "Family Name could not be retrieved from mut." }
      val propAddressLine1 = mut.removeFirstOrNull()
      require(propAddressLine1 != null) { "Property Address Line 1 could not be retrieved from mut." }
      val propAddressLine2 = mut.removeFirstOrNull()
      require(propAddressLine2 != null) { "Property Address Line 2 could not be retrieved from mut." }
      val altAddressLine1 = mut.removeFirstOrNull()
      val altAddressLine2 = mut.removeFirstOrNull()

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

val OwnerDataValidator = Validation<OwnerData> {
  OwnerData::homeId required {
    pattern(Regex(RegexPatterns.homeIdRegexExact)) hint "Home ID should be the house number followed by the street name abbreviation. I.e. 18451GW or 8454TRD or 14CJC."
    minLength(3) hint "The shortest Home ID possible is 1GC (and the like)."
    maxLength(8) hint "The longest Home ID possible is 18400CJC (and the like)."
  }
  OwnerData::familyName required {
    pattern(Regex(RegexPatterns.properCase)) hint "Family name should be in proper case."
    minLength(2) hint "Family name should probably be longer than 1 character."
  }
  OwnerData::propAddressLine1 required {
    pattern(Regex(RegexPatterns.addressLine1, RegexOption.IGNORE_CASE)) hint "Property address line 1 should be in the format 12345 Street Name Way"
    minLength(13) hint "Address line 1 should be at least 13 characters, probably more. (13 is based on '1 Gardenia Ct', the shortest Westwind address)"
    maxLength(23) hint "Address line 1 should be at most 23 characters. (23 is based on '18401 Cape Jasmine Way', which should be the longest Westwind address)"
  }
  OwnerData::propAddressLine2 required {
    pattern(Regex("Gaithersburg, MD 2087[94]"))
  }
}
