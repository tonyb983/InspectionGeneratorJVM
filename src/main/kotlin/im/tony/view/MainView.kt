package im.tony.view

import com.google.api.services.docs.v1.model.Document
import im.tony.google.services.DocsService
import im.tony.google.services.DriveService
import javafx.beans.property.ReadOnlyListProperty
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.layout.Priority
import jfxtras.scene.layout.VBox
import com.google.api.services.drive.model.File as DriveFile
import tornadofx.*

class MainView : View("Hello TornadoFX") {
  private val vioDoc: Document by lazy { DocsService.violationTemplate }
  private val noVioDoc: Document by lazy { DocsService.noViolationTemplate }
  private val swap = booleanProperty(false)
  private val labelText = stringProperty(vioDoc.title)

  init {
    swap.addListener { _, oldValue, newValue ->
      if(oldValue == newValue) return@addListener

      labelText.set(if(newValue) vioDoc.title else noVioDoc.title)
    }
  }

  override val root = vbox(5.0, Pos.CENTER) {
    id = "vbox1"
    setPrefSize(645.0, 480.0)

    hbox(5, Pos.CENTER) {
      fitToParentSize()
      text(labelText) {
        alignment = Pos.TOP_CENTER
        fitToParentWidth()
        prefHeight(500.0)
      }
      vbox(10.0, Pos.BOTTOM_CENTER) {
        id = "vbox2"
        prefHeight(130.0)
        spacer(Priority.SOMETIMES)
        button("Swap") {
          prefWidth(((findParentOfType(VBox::class)?.width?.div(3)) ?: 50.0 ))
          alignment = Pos.CENTER
          action {
            swap.set(!swap.value)
          }
        }
        spacer(Priority.SOMETIMES)
      }
    }
  }
}