package im.tony.ui.views

import im.tony.ui.UiServices
import javafx.scene.Parent
import javafx.scene.paint.Color
import tornadofx.*
import java.util.logging.Level
import java.util.logging.LogRecord

public class LogView : View("My View") {
  override val root: Parent = pane {
    tableview(UiServices.records) {
      this.fitToParentWidth()
      this.prefHeight = 150.0

      if (this.items.size < 1) {
        this.items.add(LogRecord(Level.CONFIG, "Log record view initialized.").apply {
          loggerName = "im.tony.ui.views.SomethingDumb"
        })
      }

      this.isEditable = false

      this.column("Lvl.", LogRecord::getLevel)
        .minWidth(50.0)
        .maxWidth(50.0)
        .cellFormat {
          this.tableRow.apply {
            style {
              backgroundColor = when (it) {
                Level.INFO -> multi(Color.CHARTREUSE)
                Level.WARNING -> multi(Color.GOLD.brighter())
                Level.SEVERE -> multi(Color.RED.brighter())
                Level.FINE -> multi(Color.LAWNGREEN)
                Level.FINER -> multi(Color.LIMEGREEN)
                Level.FINEST -> multi(Color.FORESTGREEN)
                Level.CONFIG -> multi(Color.DODGERBLUE)
                else -> backgroundColor
              }
            }
          }
        }

      this.column("Timestamp", LogRecord::getInstant).apply {
        isEditable = false
        minWidth(50.0)
        maxWidth(250.0)
      }
      this.column("Sender", LogRecord::getLoggerName).apply {
        // value { it.value?.loggerName?.split(".")?.last() ?: "Error" }
        isEditable = false
        minWidth(50.0)
        maxWidth(250.0)
      }
      this.column("Message", LogRecord::getMessage).apply {
        isEditable = false
        remainingWidth()
      }
    }
  }
}
