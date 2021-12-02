package draw.base

import cats.data.State
import draw.point.Point2D
import draw.{Context, Drawer, DrawingBuilderApi}

object BlackHoleDraw extends DrawingBuilderApi {
  override def width: Int = 1
  override def height: Int = 1

  override def context(): Context =
    new Context {
      override def drawLine(from: Point2D, to: Point2D): Context = this
      override def drawCircle(center: Point2D, radius: Float): Context = this
    }

  override def start(): State[Context, Drawer] =
    State.pure[Context, Drawer](new Drawer {})
}
