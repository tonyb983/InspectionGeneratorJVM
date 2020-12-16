package im.tony.ui.views

import im.tony.google.extensions.drive.GoogleMimeTypes
import im.tony.google.services.DriveService
import javafx.collections.ObservableList
import javafx.geometry.Pos
import javafx.scene.Parent
import tornadofx.*
import com.google.api.services.drive.model.File as DriveFile

class DriveSheetsListView : View("Hello TornadoFX") {
  private val sheets: ObservableList<DriveFile> by lazy {
    observableListOf(
      DriveService
        .fetchFilesOfType(GoogleMimeTypes.Spreadsheet)
        ?.files
        ?: mutableListOf()
    )
  }
  private val preferred: Pair<Double, Double> = Pair(645.0, 480.0)

  override val root: Parent = vbox(5.0, Pos.TOP_CENTER) {
    id = "vbox1"
    setPrefSize(preferred.first, preferred.second)

    if (sheets.size < 1) {
      text("List is empty.")
    } else {
      listview(sheets) {
        cellFormat {
          fitToParentWidth()
          prefHeight(100.0)
          graphic = cache(item.id) {
            fitToParentWidth()
            prefHeight(100.0)
            vbox {
              text("Name: ${item.name}") {
                fitToParentWidth()
                prefHeight(100.0 / 3)
              }
              text("Type: ${GoogleMimeTypes.fromMime(item.mimeType).name}") {
                fitToParentWidth()
                prefHeight(100.0 / 3)
              }
              text("Owner: ${item.owners?.first()?.displayName ?: "Unknown"}") {
                fitToParentWidth()
                prefHeight(100.0 / 3)
              }
            }
          }
        }
      }
    }
  }
}
