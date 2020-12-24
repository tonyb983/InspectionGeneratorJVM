package im.tony.ui.fragments

import im.tony.data.InspectionData
import tornadofx.ItemViewModel

class InspectionDataModel : ItemViewModel<InspectionData>() {
  val streetNumberProperty = bind(InspectionData::streetNumber)
  val streetNameProperty = bind(InspectionData::streetName)
  val isGoodProperty = bind(InspectionData::isGood)
  val issuesProperty = bind(InspectionData::issuesObservable)
  val streetIdProperty = bind(InspectionData::streetId)
  val homeIdProperty = bind(InspectionData::homeId)
  val issueCountProperty = bind(InspectionData::issueCount)
}


