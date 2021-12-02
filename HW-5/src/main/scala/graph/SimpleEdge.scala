package graph

case class SimpleEdge[T <: Vertex](from: T, to: T) extends Edge
