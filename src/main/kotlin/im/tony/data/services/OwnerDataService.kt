package im.tony.data.services

import im.tony.data.OwnerData
import im.tony.google.services.SheetsService
import tornadofx.toObservable

object OwnerDataService {
  val owners: Map<String, OwnerData> by lazy {
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
      .toMap()
  }

  val ownersObservable by lazy {
    owners.toObservable()
  }
}
