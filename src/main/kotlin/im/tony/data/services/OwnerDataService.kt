package im.tony.data.services

import im.tony.data.OwnerData
import im.tony.google.services.SheetsService
import javafx.collections.ObservableMap
import tornadofx.toObservable

interface IOwnerDataService {
  val owners: Map<String, OwnerData>
  val ownersObservable: ObservableMap<String, OwnerData>
}

private object OwnerDataImpl : IOwnerDataService {
  override val owners: Map<String, OwnerData> by lazy {
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

  override val ownersObservable: ObservableMap<String, OwnerData> by lazy {
    owners.toObservable()
  }
}

val OwnerDataService: IOwnerDataService = OwnerDataImpl
