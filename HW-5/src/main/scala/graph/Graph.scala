package graph

import draw.{Drawer, DrawingBuilderApi}

trait Graph {

  /** Bridge to drawing api
    */
  def drawingApi: DrawingBuilderApi

  def drawGraph(): Option[Drawer]
}
