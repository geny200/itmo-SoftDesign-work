package draw

import cats.data.State
import cats.data.State.modify
import draw.point.Point2D

trait DrawingBuilderApi {
  type Endo[A] = A => A

  def width: Int
  def height: Int

  def context(): Context
  def start(): State[Context, Drawer]

  def drawLine(
      from: Point2D,
      to: Point2D
  ): State[Context, Unit] =
    modify[Context](_.drawLine(from, to))

  def drawCircle(
      center: Point2D,
      radius: Float
  ): State[Context, Unit] =
    modify[Context](_.drawCircle(center, radius))
}
