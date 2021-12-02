package draw.base.awt

import draw.Context
import draw.point.Point2D

import java.awt.Shape
import java.awt.geom.{Ellipse2D, Line2D}

case class AwtContext(comp: Seq[Shape]) extends Context {
  override def drawLine(from: Point2D, to: Point2D): Context =
    copy(comp = new Line2D.Float(from.x, from.y, to.x, to.y) +: comp)

  override def drawCircle(center: Point2D, radius: Float): Context =
    copy(comp =
      new Ellipse2D.Float(
        center.x - radius,
        center.y - radius,
        radius * 2,
        radius * 2
      ) +: comp
    )
}

object AwtContext {
  def apply(): AwtContext =
    new AwtContext(Seq())
}
