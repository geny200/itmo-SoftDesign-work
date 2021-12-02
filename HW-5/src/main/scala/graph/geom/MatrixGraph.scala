package graph.geom

import draw.DrawingBuilderApi
import draw.base.BlackHoleDraw
import graph.SimpleEdge

case class MatrixGraph(
    drawingApi: DrawingBuilderApi,
    vertexes: Seq[GeomVertex],
    matrix: Map[GeomVertex, GeomVertex]
) extends GeomGraph {
  override def edges: Iterable[SimpleEdge[GeomVertex]] =
    matrix.map(edge => SimpleEdge(edge._1, edge._2))

  override def addVertex(newVertex: GeomVertex): MatrixGraph =
    copy(vertexes = newVertex +: vertexes)

  override def addEdge(newEdge: SimpleEdge[GeomVertex]): MatrixGraph =
    copy(matrix = matrix.updated(newEdge.from, newEdge.to))

  override def changeApi(newApi: DrawingBuilderApi): MatrixGraph =
    copy(drawingApi = newApi)
}

object MatrixGraph {
  def apply(
      drawingApi: DrawingBuilderApi,
      vertexes: Seq[GeomVertex]
  ): MatrixGraph =
    MatrixGraph(drawingApi, vertexes, Map())

  def apply(vertexes: Seq[GeomVertex]): MatrixGraph =
    MatrixGraph(BlackHoleDraw, vertexes, Map())
}
