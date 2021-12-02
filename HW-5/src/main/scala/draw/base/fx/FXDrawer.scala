package draw.base.fx

import draw.Drawer
import scalafx.application.JFXApp3
import scalafx.scene.{Group, Node, Scene}

class FXDrawer(comp: Seq[Node], w: Int, h: Int) extends JFXApp3 with Drawer {
  main(Array())
  override def start(): Unit =
    stage = new JFXApp3.PrimaryStage {
      title = "Graph by FX"
      width = w
      height = h
      resizable = false
      scene = new Scene {
        root = new Group() {
          children = comp
        }
      }
    }
}
