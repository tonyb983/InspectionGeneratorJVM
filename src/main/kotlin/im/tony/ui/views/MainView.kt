package im.tony.ui.views

import im.tony.Const
import im.tony.google.extensions.drive.GoogleMimeTypes
import im.tony.google.services.DriveService
import im.tony.google.services.SheetsService
import im.tony.google.types.DriveFile
import im.tony.ui.UiServices
import im.tony.utils.getRandomLogLevel
import im.tony.utils.getRandomQuote
import javafx.beans.binding.StringBinding
import javafx.collections.ObservableList
import javafx.event.EventTarget
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.control.ListView
import javafx.scene.control.SelectionMode
import javafx.scene.layout.Priority
import tornadofx.*
import java.util.logging.Level

class MainView : View("My View") {
  private val mainText: String


  private val docs by lazy {
    DriveService.fetchFilesOfType(GoogleMimeTypes.Document, "iconLink")?.files?.toObservable() ?: observableListOf()
  }
  private val folders by lazy {
    DriveService.fetchFilesOfType(GoogleMimeTypes.Folder, "iconLink")?.files?.toObservable() ?: observableListOf()
  }

  private var selectedDoc: DriveFile? = null
  private var selectedFolder: DriveFile? = null

  private val sizeObs: StringBinding = stringBinding(
    UiServices.records.sizeProperty,
    UiServices.records,
    UiServices.records.sizeProperty
  ) { "Records Size: ${this.value}" }

  private fun createFileList(parent: EventTarget, l: ObservableList<DriveFile>, onSelect: (DriveFile?) -> Unit): ListView<DriveFile> {
    return parent.listview(l) {
      this.selectionModel.selectionMode = SelectionMode.SINGLE
      cellFormat {
        this.graphic = cache(it.id) {
          hbox {
            if (it.iconLink != null) {
              imageview(it.iconLink) {
                prefWidth = prefHeight
              }
            }
            vbox {
              text(it.id)
              text(it.name)
            }
          }
        }
      }

      isEditable = false
      onUserSelect(1) {
        onSelect.invoke(it)
      }
    }
  }

  init {
    var s = ""
    val spreadsheets = SheetsService.loadSpreadsheetMetadata(Const.InputDataSheetId)
    s += "Title: ${spreadsheets.properties.title}\n\n"
    spreadsheets.sheets.forEach { sheet ->
      s += "Sheet Title: ${sheet.properties.title}\n"
      s += "Sheet Type: ${sheet.properties.sheetType}\n"
      sheet.properties.gridProperties?.also { gp ->
        s += "Row Count: ${gp.rowCount} | Col Count: ${gp.columnCount}\n"
        s += "Frozen Rows: ${gp.frozenRowCount} | Frozen Cols: ${gp.frozenColumnCount}\n"
      }
      s += "\n"
    }

    mainText = s
  }

  override val root: Parent = borderpane {
    setPrefSize(800.0, 600.0)
    top {
      scrollpane(fitToWidth = true, fitToHeight = true) {
        text(mainText)
      }
    }

    bottom<LogView>()

    right {
      vbox(10, Pos.CENTER) {
        button("Add Log") {
          action {
            UiServices.logger.log(getRandomLogLevel(), getRandomQuote())
          }
        }

        spacer(Priority.SOMETIMES) {
          maxHeight = 100.0
        }

        text(sizeObs)
      }
    }

    center {
      vbox {
        hbox(alignment = Pos.TOP_CENTER) {
          fitToParentSize()

          borderpane {
            this.left<InspectionListView>()
            this.right<OwnerListView>()
          }
        }

        spacer(Priority.SOMETIMES)

        hbox {
          spacer(Priority.SOMETIMES)
          button("Make Copy") {
            action {
              if (selectedDoc == null || selectedFolder == null) {
                UiServices.log(Level.WARNING, "MainView Make Copy") { "Make Copy cannot run with null doc or null folder." }
              } else {
                UiServices.log(
                  Level.INFO,
                  "MainView Make Copy"
                ) { "Would have created copy of '${selectedDoc!!.name}' with '${selectedFolder!!.name}' as its parent." }
              }
            }
          }
          spacer(Priority.SOMETIMES)
        }
      }
    }
  }
}
