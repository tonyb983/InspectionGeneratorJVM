package im.tony.view

import im.tony.data.InspectionData
import im.tony.data.OwnerData
import im.tony.data.services.InspectionDataService
import im.tony.data.services.OwnerDataService
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.layout.Priority
import kotlinx.collections.immutable.ImmutableCollection
import kotlinx.collections.immutable.toImmutableList
import tornadofx.*

public class DataView : View("Data View") {
  private val inspections: ImmutableCollection<InspectionData> by lazy { InspectionDataService.inspections.values }
  private val invalidInspections by lazy {
    InspectionDataService
      .inspections
      .values
      .filter { !it.isValid }
  }
  private val invalidText by lazy {
    if (invalidInspections.size < 1) "Invalid Inspections Empty"
    else
      invalidInspections
        .flatMap { it.failures }
        .groupBy { it }
        .map { "Failure: ${it.key}\nCount: ${it.value.size}\n" }
        .joinToString("\n")
  }

  private val owners: ImmutableCollection<OwnerData> by lazy { OwnerDataService.owners.values }

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
      text(invalidInspections.size.toString()) {
        style {
          baseColor = c(230, 20, 20)
        }
      }
      text(invalidText) {
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
