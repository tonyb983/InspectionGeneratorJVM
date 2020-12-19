package im.tony.ui.views

import im.tony.utils.getRandomLogLevel
import im.tony.utils.getRandomQuote
import javafx.beans.property.BooleanProperty
import javafx.event.EventHandler
import javafx.scene.control.MenuBar
import javafx.util.Duration
import org.controlsfx.control.NotificationPane
import org.controlsfx.control.Notifications
import org.controlsfx.control.StatusBar
import tornadofx.*
import tornadofx.controlsfx.statusbar
import java.util.logging.Level
import kotlin.random.Random

class GeneratorRunnerView : View("Generator") {
  val notificationShowing: BooleanProperty = booleanProperty(false)
  private var nPane: NotificationPane by singleAssign()
  private val random by lazy { Random(0) }

  /**
   * Called when a Component becomes the Scene root or
   * when its root node is attached to another Component.
   * @see UIComponent.add
   */
  override fun onDock() {
    super.onDock()

    workspace.add(createMenuBar())
  }

  init {
    notificationShowing.addListener { obs, oldVal, newVal -> if (oldVal != newVal) println("Notification Showing On Change. Old:$oldVal | New:$newVal | Obs:$obs") }
  }

  override val root = pane {
    fitToParentSize()
    vbox {
      button("Create Notification") {
        action {
          val text = getRandomQuote().breakTextEvery(45, 52)
          val dur = if (text.length > 75) Duration.seconds(random.nextDouble(6.0, 10.0)) else if (text.length < 35) Duration.seconds(
            random.nextDouble(
              3.0,
              5.0
            )
          ) else Duration.seconds(random.nextDouble(4.0, 7.5))
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

  private fun createStatusBar(): StatusBar {
    return statusbar {
      fitToParentWidth()
      prefHeight(40.0)
      this.text = "Hello status-bar!"
    }
  }

  private fun createMenuBar(): MenuBar {
    return menubar {
      this.menu("Something") {
        this.item("Something 1") {
          this.onAction = EventHandler { log.log(Level.WARNING, "Something 1 pressed.") }
        }
        this.item("Something 2") {
          this.onAction = EventHandler { log.log(Level.WARNING, "Something 2 pressed.") }
        }
        this.separator()
        this.checkmenuitem("Check Menu Item") {
          this.onAction = EventHandler { log.warning("check menu handler: $it") }
        }
      }
      this.menu("Another Thing") {
        this.item("Another 1")
        this.item("And another")
      }
    }
  }

  private fun CharSequence.prepareHyphen(): Pair<CharSequence, CharSequence> {
    val mid = if (this.length % 2 != 0) (this.length - 1) / 2 else this.length / 2
    val first = this.subSequence(0, mid)
    val second = this.subSequence(mid, this.length)

    return first to second
  }

  private fun String.breakTextEvery(
    shortestLine: Int,
    longestLine: Int,
    breakOn: String = " ",
    ignoreCase: Boolean = false,
    breakWith: String = System.lineSeparator()
  ): String {
    val words = this.split(breakOn, ignoreCase = ignoreCase)
    val wordCount = words.size
    val sb = StringBuilder()
    var currentLineLength = 0

    fun appendSpaceIfMore(isLast: Boolean) {
      if (isLast) return

      sb.append(" ")
      currentLineLength += 1
    }

    for ((index, word) in words.withIndex()) {
      val isLastWord = index + 1 >= wordCount
      when (val afterCurrentSize = currentLineLength + word.length) {
        in 0 until shortestLine -> { // Too short.
          sb.append(word)
          currentLineLength += word.length
          appendSpaceIfMore(isLastWord)
        }
        in shortestLine..longestLine -> { // In Range
          when {
            isLastWord -> {
              // If this is the last word we will append and be done
              sb.append(word)
              // Unnecessary but safe
              currentLineLength += word.length
            }
            words[index + 1].length + afterCurrentSize + 1 < longestLine -> {
              // If the next word will fit we won't break
              sb.append("$word ")
              currentLineLength += (word.length + 1)
            }
            else -> {
              // If the next word won't fit we will break here
              sb.append("$word$breakWith")
              currentLineLength = 0
            }
          }
        }
        else -> { // We're too big to fit
          if ((word.length / 2) + currentLineLength <= longestLine) {
            val (first, second) = word.prepareHyphen()
            sb.append("$first-$breakWith")
            sb.append(second)
            currentLineLength = second.length
            appendSpaceIfMore(isLastWord)
          } else {
            sb.append(breakWith)
            currentLineLength = 0
            if (word.length > longestLine) {
              if ((word.length / 2) > longestLine) {
                // Way too big, multi-hyphen?
                val parts = word.length / shortestLine
                for (i in 0 until parts) {
                  val part = word.subSequence(i * shortestLine, (i + 1) * shortestLine)
                  sb.append("$part-$breakWith")
                }
                val last = word.subSequence((parts - 1) * shortestLine, word.length)
                sb.append(last)
                currentLineLength = last.length
                appendSpaceIfMore(isLastWord)
              } else {
                val (first, second) = word.prepareHyphen()
                sb.append("$first-$breakWith")
                sb.append(second)
                currentLineLength = second.length
                appendSpaceIfMore(isLastWord)
              }
            } else {
              sb.append(word)
              currentLineLength = word.length
              appendSpaceIfMore(isLastWord)
            }
          }
        }
      }
    }

    return sb.toString()
  }
}
