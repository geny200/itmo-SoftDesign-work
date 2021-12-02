package graph

import draw.DrawingBuilderApi
import draw.base.{drawingApiReader, drawingApiWriter}
import tethys.derivation.auto.jsonWriterMaterializer
import tethys.derivation.semiauto.jsonReader
import tethys.{JsonReader, JsonWriter}

import scala.language.implicitConversions

package object geom {

  implicit lazy val geomGraphWriter: JsonWriter[GeomGraph] = {
    JsonWriter
      .obj[GeomGraph]
      .addField("type") {
        case ListGraph(_, _, _)   => "list"
        case MatrixGraph(_, _, _) => "matrix"
      }
      .addField("draw")(_.drawingApi)
      .addField("vertexes")(_.vertexes)
      .addField("edges")(_.edges)
  }

  implicit lazy val geomVertexReader: JsonReader[GeomVertex] =
    jsonReader[GeomVertex]

  implicit lazy val edgeGeomVertexReader: JsonReader[SimpleEdge[GeomVertex]] =
    jsonReader[SimpleEdge[GeomVertex]]

  implicit lazy val graphReader: JsonReader[GeomGraph] = JsonReader.builder
    .addField[String]("type")
    .addField[DrawingBuilderApi]("draw")
    .addField[Seq[GeomVertex]]("vertexes")
    .addField[Seq[SimpleEdge[GeomVertex]]]("edges")
    .buildReader {
      (
          t: String,
          api: DrawingBuilderApi,
          vertexes: Seq[GeomVertex],
          edges: Seq[SimpleEdge[GeomVertex]]
      ) =>
        val graph: GeomGraph = t match {
          case "list"   => ListGraph(api, vertexes)
          case "matrix" => MatrixGraph(api, vertexes)
        }
        edges.foldLeft(graph) {
          (graph: GeomGraph, edge: SimpleEdge[GeomVertex]) =>
            graph.addEdge(edge)
        }
    }
}
