package im.tony

import im.tony.ui.UiServices
import im.tony.ui.views.MainView
import im.tony.ui.views.SheetsWorkspace
import javafx.scene.Scene
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import org.scenicview.ScenicView
import tornadofx.App
import tornadofx.SingleAssignThreadSafetyMode
import tornadofx.UIComponent
import tornadofx.singleAssign

public class MyApp : App(SheetsWorkspace::class, Styles::class) {
  private var primaryScene: Scene by singleAssign(SingleAssignThreadSafetyMode.SYNCHRONIZED)
  private var scenicViewAction: () -> Unit by singleAssign(SingleAssignThreadSafetyMode.SYNCHRONIZED)

  override fun createPrimaryScene(view: UIComponent): Scene {
    primaryScene = super.createPrimaryScene(view)
    scenicViewAction = { ScenicView.show(primaryScene) }

    return primaryScene
  }

  override fun onBeforeShow(view: UIComponent) {
    workspace.dock<MainView>()
    workspace.accelerators[KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN)] = scenicViewAction
    UiServices.init(workspace.log)
  }

  override fun stop() {
    UiServices.stop()

    super.stop()
  }
}
