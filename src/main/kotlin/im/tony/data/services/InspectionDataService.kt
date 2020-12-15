package im.tony.data.services

import im.tony.data.InspectionData
import im.tony.google.services.SheetsService
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableMap
import tornadofx.toObservable

public object InspectionDataService {
  public val inspections: ImmutableMap<String, InspectionData> by lazy {
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
      .toImmutableMap()
  }

  public val inspectionsObservable by lazy {
    inspections.toObservable()
  }
}
