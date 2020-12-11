package im.tony

import javafx.scene.text.FontWeight
import tornadofx.*

public class Styles : Stylesheet() {
  public companion object {
    public val heading: CssRule by cssclass()
  }

  init {
    label and heading {
      padding = box(10.px)
      fontSize = 20.px
      fontWeight = FontWeight.BOLD
    }
  }
}
