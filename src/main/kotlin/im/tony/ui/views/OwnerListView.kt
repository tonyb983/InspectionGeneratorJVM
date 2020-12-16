package im.tony.ui.views

import im.tony.data.services.OwnerDataService
import im.tony.ui.fragments.OwnerDataModel
import javafx.geometry.Pos
import tornadofx.*
import javafx.scene.layout.Priority
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.TextAlignment

class OwnerListView : View("Owner List") {
  private val owners by lazy { OwnerDataService.owners.values.toList().toObservable() }
  private val model: OwnerDataModel by inject()

  override val root = listview(owners) {
    cellFormat {
      this.graphic = cache {
        vbox {
          text("Home ID: ${it.homeId}") {
            textAlignment = TextAlignment.LEFT
            isUnderline = true
          }
          text("${it.familyName} Family") {
            font = Font.font(18.0)
            textAlignment = TextAlignment.LEFT
          }
          text(it.propAddressLine1) {
            textAlignment = TextAlignment.LEFT
          }
          text("Family ${if (it.hasAltAddress) "does" else "does not"} have an alternate address.") {
            if (it.hasAltAddress) {
              isUnderline = true
            } else {
              isStrikethrough = true
            }
          }
        }
      }
    }

    this.bindSelected(model)
    this.placeholder = pane {
      vbox(10.0, Pos.CENTER) {
        spacer(Priority.SOMETIMES)
        label("No Owner Given") {
          textAlignment = TextAlignment.CENTER
          isUnderline = true
          textFill = Color.LIGHTSLATEGREY
        }
        spacer(Priority.SOMETIMES)
      }
    }
    this.isEditable = false
  }

}
