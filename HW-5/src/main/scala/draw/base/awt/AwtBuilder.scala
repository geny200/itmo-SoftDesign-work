package draw.base.awt

import cats.data.State
import draw.{Context, Drawer, DrawingBuilderApi}

import java.awt.Shape

case class AwtBuilder(
    width: Int,
    height: Int
) extends DrawingBuilderApi {

  override def context(): Context =
    AwtContext()

  override def start(): State[Context, Drawer] =
    for {
      comp <- State.inspect[Context, Seq[Shape]] {
        case AwtContext(comp) => comp
        case _                => Seq()
      }
    } yield new AwtDrawer(comp, width, height)
}

object AwtBuilder {
  def apply(width: Int, height: Int): AwtBuilder =
    new AwtBuilder(width, height)

  def apply(): AwtBuilder =
    apply(500, 500)
}
