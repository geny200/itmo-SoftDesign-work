package draw.base.awt

import draw.Drawer

import java.awt._
import javax.swing.{JFrame, WindowConstants}

class AwtDrawer(comp: Seq[Shape], width: Int, height: Int)
    extends JFrame
    with Drawer {
  setTitle("Graph by AWT")
  setSize(new Dimension(width, height))
  setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
  setResizable(false)
  setLocationRelativeTo(null)
  setVisible(true)
  toFront()

  override def paint(graphics: Graphics): Unit =
    graphics match {
      case d: Graphics2D =>
        d.setColor(Color.GRAY)
        getContentPane.getHeight
        comp.foreach(d.fill)
        comp.foreach(d.draw)
    }
}
