package im.tony.data

import im.tony.data.services.IInspectionDataService

sealed class InspectionDataQuery {
  open fun bestMatch(inspectionDataService: IInspectionDataService): InspectionData? = allMatches(inspectionDataService).firstOrNull()
  abstract fun allMatches(inspectionDataService: IInspectionDataService): List<InspectionData>

  sealed class GenericTextQuery(val searchText: String) : InspectionDataQuery() {
    class StreetNameSearch(searchText: String) : GenericTextQuery(searchText) {
      override fun allMatches(inspectionDataService: IInspectionDataService) = inspectionDataService
        .inspections
        .values
        .filter { it.streetName.contains(searchText) }
    }

    class StreetNumberSearch(searchText: String) : GenericTextQuery(searchText) {
      constructor(num: Int) : this(num.toString())

      override fun allMatches(inspectionDataService: IInspectionDataService) = inspectionDataService
        .inspections
        .values
        .filter { it.streetNumber.contains(searchText) }
    }
  }

}
