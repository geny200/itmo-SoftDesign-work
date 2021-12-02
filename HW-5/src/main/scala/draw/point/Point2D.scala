package draw.point

trait Point2D {
  def x: Float
  def y: Float

  def normalize(leftBottom: Point2D, zoomPoint: Point2D): Point2D = {
    val currentX = x
    val currentY = y
    new Point2D {
      override def x: Float = (currentX - leftBottom.x) * zoomPoint.x
      override def y: Float = (currentY - leftBottom.y) * zoomPoint.y
    }
  }
}
