package im.tony.data.services

import im.tony.data.InspectionData
import im.tony.google.services.SheetsService
import javafx.collections.ObservableMap
import tornadofx.toObservable

interface IInspectionDataService {
  val inspections: Map<String, InspectionData>
  val inspectionsObservable: ObservableMap<String, InspectionData>
}

private object InspectionDataImpl : IInspectionDataService {
  override val inspections: Map<String, InspectionData> by lazy {
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

  override val inspectionsObservable: ObservableMap<String, InspectionData> by lazy {
    inspections.toObservable()
  }
}

val InspectionDataService: IInspectionDataService = InspectionDataImpl
