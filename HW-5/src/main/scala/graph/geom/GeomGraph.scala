package graph.geom

import cats.data.State.pure
import cats.implicits._
import draw.point.GeomPoint2D
import draw.{Context, Drawer, DrawingBuilderApi}
import graph.{Graph, SimpleEdge}

import scala.math.min

trait GeomGraph extends Graph {
  def vertexes: Iterable[GeomVertex]
  def edges: Iterable[SimpleEdge[GeomVertex]]

  def addVertex(newVertex: GeomVertex): GeomGraph
  def addEdge(newEdge: SimpleEdge[GeomVertex]): GeomGraph
  def changeApi(newApi: DrawingBuilderApi): GeomGraph

  override def drawGraph(): Option[Drawer] = {
    for {
      left <- vertexes.map(_.x).minOption
      bottom <- vertexes.map(_.y).minOption
      right <- vertexes.map(_.x).maxOption
      top <- vertexes.map(_.y).maxOption

      zoomWidth = drawingApi.width / (right - left)
      zoomHeight = drawingApi.height / (top - bottom)
      zoomPoint = GeomPoint2D(zoomWidth * 0.8f, zoomHeight * 0.8f)
      nullPoint = GeomPoint2D(
        left - (right - left) / (zoomWidth * 0.2f),
        bottom - (top - bottom) / (zoomHeight * 0.2f)
      )
      border = GeomPoint2D(left, bottom).normalize(nullPoint, zoomPoint)
      radius = min(border.y, border.x) / 2

      drawer = (for {
        _ <- vertexes
          .map(_.normalize(nullPoint, zoomPoint))
          .map(drawingApi.drawCircle(_, radius))
          .fold(pure[Context, Unit](()))((fst, snd) => fst.flatMap(_ => snd))

        _ <- edges
          .flatMap(edge =>
            for {
              from <- vertexes
                .find(_ == edge.from)
                .map(_.normalize(nullPoint, zoomPoint))
              to <- vertexes
                .find(_ == edge.to)
                .map(_.normalize(nullPoint, zoomPoint))
            } yield drawingApi.drawLine(from, to)
          )
          .fold(pure[Context, Unit](()))((fst, snd) => fst.flatMap(_ => snd))

        result <- drawingApi.start()
      } yield result)
        .runA(drawingApi.context())
        .value
    } yield drawer
  }
}
