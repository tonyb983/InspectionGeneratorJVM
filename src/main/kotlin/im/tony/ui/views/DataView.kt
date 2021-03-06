package im.tony.ui.views

import im.tony.data.services.InspectionDataService
import im.tony.data.services.OwnerDataService
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.layout.Priority
import tornadofx.*

class DataView : View("Data View") {
  private val inspections by lazy { InspectionDataService.inspections.values }

  private val owners by lazy { OwnerDataService.owners.values }

  private val pSize: Pair<Double, Double> = Pair(645.0, 480.0)

  override val root: Parent = hbox(5.0, Pos.CENTER) {
    id = "hbox1"
    setPrefSize(pSize.first, pSize.second)

    vbox {
      setPrefSize(pSize.first / 2, pSize.second)
      spacer(Priority.SOMETIMES)
      text("Inspection Count") {
        isUnderline = true
      }
      text(inspections.size.toString())
      spacer(Priority.SOMETIMES)
      text("Error Counts") {
        isUnderline = true
        style {
          baseColor = c(230, 20, 20)
        }
      }
      spacer(Priority.SOMETIMES)
    }

    separator(Orientation.VERTICAL)

    vbox {
      setPrefSize(pSize.first / 2, pSize.second)
      spacer(Priority.SOMETIMES)
      text("Owner Count") {
        isUnderline = true
      }
      spacer(Priority.SOMETIMES)
      text(owners.size.toString())
      spacer(Priority.SOMETIMES)
    }
  }
}
