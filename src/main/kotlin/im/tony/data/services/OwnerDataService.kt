package im.tony.data.services

import im.tony.data.OwnerData
import im.tony.google.services.SheetsService
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableMap

public object OwnerDataService {
  public val owners: ImmutableMap<String, OwnerData> by lazy {
    SheetsService
      .topsData
      .map { anyArrays ->
        anyArrays
          .map { it as String }
          .toMutableList()
          .let { OwnerData.parse(it) }
      }
      .distinctBy { it.homeId }
      .associateBy { it.homeId }
      .toImmutableMap()
  }
}
