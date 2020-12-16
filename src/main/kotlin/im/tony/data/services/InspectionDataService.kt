package im.tony.data.services

import im.tony.data.InspectionData
import im.tony.google.services.SheetsService
import tornadofx.toObservable

object InspectionDataService {
  val inspections: Map<String, InspectionData> by lazy {
    SheetsService
      .inspectionData
      .map { anyArrays ->
        anyArrays
          .map { it as String }
          .toMutableList()
          .let { InspectionData.parse(it) }
      }
      .distinctBy { it.homeId }
      .associateBy { it.homeId }
      .toMap()
  }

  val inspectionsObservable by lazy {
    inspections.toObservable()
  }
}
