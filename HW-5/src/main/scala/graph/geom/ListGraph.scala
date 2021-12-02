package graph.geom

import draw.DrawingBuilderApi
import draw.base.BlackHoleDraw
import graph.SimpleEdge

case class ListGraph(
    drawingApi: DrawingBuilderApi,
    vertexes: Seq[GeomVertex],
    adjacencyList: Map[GeomVertex, Seq[SimpleEdge[GeomVertex]]]
) extends GeomGraph {
  override def edges: Iterable[SimpleEdge[GeomVertex]] =
    adjacencyList.flatMap(_._2)

  override def addVertex(newVertex: GeomVertex): ListGraph =
    copy(vertexes = newVertex +: vertexes)

  override def addEdge(newEdge: SimpleEdge[GeomVertex]): ListGraph = {
    val oldList = adjacencyList.getOrElse(newEdge.from, Seq())
    copy(adjacencyList =
      adjacencyList.updated(newEdge.from, newEdge +: oldList)
    )
  }

  override def changeApi(newApi: DrawingBuilderApi): GeomGraph =
    copy(drawingApi = newApi)
}

object ListGraph {
  def apply(
      drawingApi: DrawingBuilderApi,
      vertexes: Seq[GeomVertex]
  ): ListGraph =
    ListGraph(drawingApi, vertexes, Map())

  def apply(vertexes: Seq[GeomVertex]): ListGraph =
    ListGraph(BlackHoleDraw, vertexes, Map())
}
