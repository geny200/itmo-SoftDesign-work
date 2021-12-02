package draw.base.fx

import cats.data.State
import draw.{Context, Drawer, DrawingBuilderApi}
import scalafx.scene.Node

case class FXBuilder(
    width: Int,
    height: Int
) extends DrawingBuilderApi {

  override def context(): Context =
    FXContext()

  override def start(): State[Context, Drawer] =
    for {
      comp <- State.inspect[Context, Seq[Node]] {
        case FXContext(comp) => comp
        case _               => Seq()
      }
    } yield new FXDrawer(comp, width, height)
}

object FXBuilder {
  def apply(width: Int, height: Int): FXBuilder =
    new FXBuilder(width, height)

  def apply(): FXBuilder =
    apply(500, 500)
}
