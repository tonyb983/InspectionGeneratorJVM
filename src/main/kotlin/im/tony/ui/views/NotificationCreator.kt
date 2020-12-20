package im.tony.ui.views

import im.tony.utils.breakTextEvery
import im.tony.utils.getRandomLogLevel
import im.tony.utils.getRandomQuote
import javafx.util.Duration
import org.controlsfx.control.Notifications
import tornadofx.*
import java.util.logging.Level
import kotlin.random.Random

class NotificationCreator : View("My View") {
  val random by lazy { Random(0) }

  override val root = borderpane {
    center {
      button("Create Notification") {
        action {
          val text = getRandomQuote().breakTextEvery(45, 52)
          val dur = when {
            text.length > 90 -> Duration.seconds(random.nextDouble(6.0, 10.0))
            text.length < 35 -> Duration.seconds(
              random.nextDouble(
                3.0,
                5.0
              )
            )
            else -> Duration.seconds(random.nextDouble(4.0, 7.5))
          }
          Notifications
            .create()
            .darkStyle()
            .hideAfter(dur)
            .text(text)
            .run {
              when (getRandomLogLevel()) {
                Level.SEVERE -> showError()
                Level.WARNING -> showWarning()
                Level.INFO -> showInformation()
                Level.CONFIG -> showConfirm()
                else -> show()
              }
            }
        }
      }
    }
  }
}
