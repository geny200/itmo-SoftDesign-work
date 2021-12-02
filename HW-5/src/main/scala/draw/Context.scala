package draw

import draw.point.Point2D

trait Context {
  def drawLine(from: Point2D, to: Point2D): Context
  def drawCircle(center: Point2D, radius: Float): Context
}
