import draw.base.fx.FXBuilder
import error.{AbsentInputError, EmptyGraphError, InvalidInputError}
import graph.SimpleEdge
import graph.geom.{GeomGraph, GeomVertex, ListGraph}
import tethys._
import tethys.jackson._

object Application {

  def example(): Unit = {
    val listGraph: GeomGraph = ListGraph(FXBuilder(), Seq())
      .addVertex(GeomVertex(1, 5))
      .addVertex(GeomVertex(5, 1))
      .addVertex(GeomVertex(-1, -5))
      .addVertex(GeomVertex(-5, -1))
      .addEdge(SimpleEdge[GeomVertex](GeomVertex(1, 5), GeomVertex(5, 1)))
      .addEdge(SimpleEdge[GeomVertex](GeomVertex(-5, -1), GeomVertex(5, 1)))

    listGraph.drawGraph()
    println(listGraph.asJson)
  }

  def main(args: Array[String]): Unit =
    args.headOption
      .toRight(AbsentInputError)
      .flatMap { (line: String) =>
        line.jsonAs[GeomGraph] match {
          case Left(value)  => Left(InvalidInputError(value.getMessage))
          case Right(value) => Right(value)
        }
      }
      .flatMap(_.drawGraph().toRight(EmptyGraphError)) match {
      case Left(error) => println(s"error: ${error.message}")
      case Right(_)    =>
    }
}
