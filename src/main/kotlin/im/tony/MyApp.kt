package im.tony

import im.tony.ui.UiServices
import im.tony.ui.app.Timers
import im.tony.ui.views.SecondMain
import im.tony.ui.views.SheetsWorkspace
import javafx.scene.Scene
import org.scenicview.ScenicView
import tornadofx.App
import tornadofx.SingleAssignThreadSafetyMode
import tornadofx.UIComponent
import tornadofx.singleAssign
import kotlin.collections.set

class MyApp : App(SheetsWorkspace::class, Styles::class) {
  private var primaryScene: Scene by singleAssign(SingleAssignThreadSafetyMode.SYNCHRONIZED)
  private var scenicViewAction: () -> Unit by singleAssign(SingleAssignThreadSafetyMode.SYNCHRONIZED)
  private var sheetsWorkspace: SheetsWorkspace by singleAssign(SingleAssignThreadSafetyMode.SYNCHRONIZED)

  init {

  }

  override fun createPrimaryScene(view: UIComponent): Scene {
    primaryScene = super.createPrimaryScene(view)
    scenicViewAction = { ScenicView.show(primaryScene) }

    return primaryScene
  }

  override fun onBeforeShow(view: UIComponent) {
    sheetsWorkspace = workspace as SheetsWorkspace
    workspace.dock<SecondMain>()
    workspace.accelerators[Const.ScenicViewShortcut] = scenicViewAction
    UiServices.init(workspace.log)
  }

  override fun stop() {
    UiServices.stop()
    Timers.stop()
    super.stop()
  }
}
