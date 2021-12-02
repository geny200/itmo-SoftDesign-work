package draw.base.fx

import draw.Context
import draw.point.Point2D
import scalafx.scene.Node
import scalafx.scene.paint.Color
import scalafx.scene.shape.{Circle, Line}

case class FXContext(comp: Seq[Node]) extends Context {
  override def drawLine(from: Point2D, to: Point2D): Context =
    copy(comp = new Line {
      fill = Color.Gray
      stroke = Color.Gray
      startX = from.x
      startY = from.y
      endX = to.x
      endY = to.y
      smooth = true
    } +: comp)

  override def drawCircle(center: Point2D, r: Float): Context = {
    copy(comp = new Circle {
      fill = Color.Gray
      stroke = Color.Gray
      centerX = center.x
      centerY = center.y
      radius = r
      smooth = true
    } +: comp)
  }
}

object FXContext {
  def apply(): FXContext =
    new FXContext(Seq())
}
