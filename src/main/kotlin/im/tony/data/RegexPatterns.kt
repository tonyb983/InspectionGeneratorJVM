package im.tony.data

import org.intellij.lang.annotations.Language

object RegexPatterns {
  @Language("RegExp")
  const val homeIdRegexExact = "[0-9]{1,5}(CJC|CJW|FHW|GC|GW|TRC|TRD|TRP)"

  //                        or "$streetNumberRegex$streetIdRegexExact"
  @Language("RegExp")
  const val homeIdRegexGeneric = "[0-9]{1,5}[A-Z]{2,3}"

  //                          or "$streetNumberRegex$streetIdRegexGeneric"
  @Language("RegExp")
  const val streetNumberRegex = "[0-9]{1,5}"

  @Language("RegExp")
  const val streetNameRegexGeneric = "(?:[A-Za-z]+ ){1,2}(court|way|place|drive)"

  @Language("RegExp")
  const val streetNameRegexExact =
    "(tea rose drive|tea rose place|tea rose court|gardenia way|gardenia court|flower hill way|cape jasmine court|cape jasmine way)"

  @Language("RegExp")
  const val streetIdRegexGeneric = "[A-Z]{2,3}"

  @Language("RegExp")
  const val streetIdRegexExact = "(CJC|CJW|FHW|GC|GW|TRC|TRD|TRP)"

  @Language("RegExp")
  const val isGoodRegex = "(Yes|No)"

  @Language("RegExp")
  const val addressLine1 =
    "^((?:[0-9]{1,5} ?) ?(?:[a-z] ?)+ ?(?:street|st\\.?|road|rd\\.?|way|court|ct\\.?|place|pl\\.?|drive|dr\\.?|avenue|ave\\.?|terrace|terr?\\.?))\$" // /gi

  // At least 8 characters, 1 uppercase, 1 lowercase, 1 number, can contain special characters
  @Language("RegExp")
  const val password = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}\$"// /gm

  const val properCase = "(?:\\b[A-Z][a-z]+)"
}
