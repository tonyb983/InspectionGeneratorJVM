package im.tony.ui.views

import im.tony.data.services.InspectionDataService
import im.tony.ui.fragments.InspectionDataModel
import javafx.geometry.Pos
import tornadofx.*
import javafx.scene.control.ListView
import javafx.scene.layout.Priority
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.TextAlignment

class InspectionListView : View("Inspection List") {
  private val inspections by lazy { InspectionDataService.inspections.values.toList().toObservable() }
  private val model: InspectionDataModel by inject()

  override val root = listview(inspections) {
    cellFormat {
      this.graphic = cache {
        vbox {
          text("Home ID: ${it.homeId}") {
            textAlignment = TextAlignment.RIGHT
            isUnderline = true
          }
          text("${it.streetNumber} ${it.streetName}") {
            font = Font.font(18.0)
            textAlignment = TextAlignment.RIGHT
          }
          text(if (it.isGood) "House has no inspection issues." else "House has ${it.issueCount} issue${if (it.issueCount > 1) "s" else ""}") {
            textFill = if (it.isGood) Color.LAWNGREEN else Color.ORANGERED
            textAlignment = TextAlignment.RIGHT
          }
        }
      }
    }

    this.bindSelected(model)
    this.placeholder = pane {
      vbox(10.0, Pos.CENTER) {
        spacer(Priority.SOMETIMES)
        label("No Inspection Given") {
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
