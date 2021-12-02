package draw.base

import cats.data.State
import draw.point.Point2D
import draw.{Context, Drawer, DrawingBuilderApi}

object ConsoleDraw extends DrawingBuilderApi {
  override def width: Int = 1
  override def height: Int = 1

  override def context(): Context =
    new Context {
      override def drawLine(from: Point2D, to: Point2D): Context = {
        println(s"Line (x:${from.x}, y:${from.y}) to (x:${to.x}, y:${to.y})")
        this
      }

      override def drawCircle(center: Point2D, radius: Float): Context = {
        println(s"Circle x:${center.x}, y:${center.y}, r:$radius ")
        this
      }
    }

  override def start(): State[Context, Drawer] =
    State.pure[Context, Drawer](new Drawer {})
}
