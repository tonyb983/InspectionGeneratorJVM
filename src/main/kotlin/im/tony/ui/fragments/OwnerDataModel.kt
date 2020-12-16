package im.tony.ui.fragments

import im.tony.data.OwnerData
import javafx.beans.property.ReadOnlyBooleanProperty
import javafx.beans.property.ReadOnlyStringProperty
import tornadofx.ItemViewModel

class OwnerDataModel : ItemViewModel<OwnerData>() {
  val homeIdProperty = bind(OwnerData::homeId) as ReadOnlyStringProperty
  val familyNameProperty = bind(OwnerData::familyName) as ReadOnlyStringProperty
  val propAddressLine1Property = bind(OwnerData::propAddressLine1) as ReadOnlyStringProperty
  val propAddressLine2Property = bind(OwnerData::propAddressLine2) as ReadOnlyStringProperty
  val altAddressLine1Property = bind(OwnerData::altAddressLine1) as ReadOnlyStringProperty
  val altAddressLine2Property = bind(OwnerData::altAddressLine2) as ReadOnlyStringProperty
  val hasAltAddressProperty = bind(OwnerData::hasAltAddress) as ReadOnlyBooleanProperty
}
