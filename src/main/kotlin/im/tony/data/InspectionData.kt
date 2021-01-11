package im.tony.data

import im.tony.utils.empty
import io.konform.validation.Validation
import io.konform.validation.jsonschema.*
import org.intellij.lang.annotations.MagicConstant
import tornadofx.toObservable

data class InspectionData(
  val streetNumber: String,
  val streetName: String,
  val isGood: Boolean,
  val issues: MutableList<String> = mutableListOf()
) {
  val streetId: String = streetName.split(" ").map { it.trim().first().toUpperCase() }.joinToString("")
  val homeId: String = "${streetNumber.trim()}$streetId"
  val issueCount: Int = issues.size

  val issuesObservable by lazy { issues.toObservable() }

  fun mapToString(
    @MagicConstant(stringValues = ["%STREET_NUM%", "%STREET_NAME%", "%ISSUE_LIST%"])
    input: String
  ): String = when (input) {
    "%STREET_NUM%" -> this.streetNumber
    "%STREET_NAME%" -> this.streetName
    "%ISSUE_LIST%" -> this.issues.joinToString("\n")
    else -> String.empty
  }

  override fun toString() =
    "Inspection $homeId | $streetNumber $streetName | Is Good: $isGood" + if (isGood) "" else " | Issues: ${issues.joinToString(" / ")}"

  companion object {
    fun parse(cells: Collection<String>): InspectionData {
      require(cells.isNotEmpty()) { "Cell array should not be empty." }
      require(cells.size in 3..9) { "Cell array size should be between 3 and 9." }

      val mut = cells.toMutableList()

      val num = mut.removeFirst()
      val name = mut.removeFirst()
      val good = mut.removeFirst().let { it == "Yes" }

      return InspectionData(num, name, good, mut)
    }
  }
}

val InspectionDataValidator = Validation<InspectionData> {
  InspectionData::homeId required {
    pattern(Regex(RegexPatterns.homeIdRegexExact)) hint "Home ID should be the house number followed by the street name abbreviation. I.e. 18451GW or 8454TRD or 14CJC."
  }
  InspectionData::streetId required {
    pattern(Regex(RegexPatterns.streetIdRegexExact, RegexOption.IGNORE_CASE))
    this.minLength(2)
    this.maxLength(3)
    this.enum("CJC", "CJW", "FHW", "GC", "GW", "TRC", "TRD", "TRP")
  }
  InspectionData::streetNumber required {
    pattern(Regex(RegexPatterns.streetNumberRegex)) hint "Street number should be a 1-5 digit number in string form."
  }
  InspectionData::streetName required {
    pattern(
      Regex(
        RegexPatterns.streetNameRegexExact,
        RegexOption.IGNORE_CASE
      )
    ) hint "Street name should match one of the valid Westwind street names."
  }

  this.addConstraint(
    "If property isGood there should be no issues, " +
      "if isGood is false there should be at least one issue."
  ) { (it.isGood && it.issues.size < 1) || (!it.isGood && it.issues.size >= 1) }

  InspectionData::isGood required {
    addConstraint("Should be true when there are no issues, and false when there is one or more issues.") { i: InspectionData ->
      (i.isGood && i.issues.size < 1) || (!i.isGood && i.issues.size >= 1)
    }
  }
  InspectionData::issues required {
    this.minItems(0) hint "Issues should have at least 0 items."
    this.maxItems(5) hint "Issues should have at most 5 items."
  }
  InspectionData::issueCount required {
    this.minimum(0) hint "IssueCount should be at least 0."
    this.maximum(5) hint "IssueCount should be at most 5."
  }
}


