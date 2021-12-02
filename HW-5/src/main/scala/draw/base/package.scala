package draw

import draw.base.awt.AwtBuilder
import draw.base.fx.FXBuilder
import tethys.readers.JsonReaderBuilder
import tethys.{JsonReader, JsonWriter}

package object base {
  implicit lazy val drawingApiWriter: JsonWriter[DrawingBuilderApi] = {
    JsonWriter
      .obj[DrawingBuilderApi]
      .addField("api") {
        case BlackHoleDraw => "blackHole"
        case ConsoleDraw   => "console"
        case _: AwtBuilder => "awt"
        case _: FXBuilder  => "fx"
        case _             => "unknown"
      }
  }

  implicit lazy val drawingApiReader: JsonReader[DrawingBuilderApi] =
    JsonReaderBuilder
      .addField[String]("api")
      .buildReader {
        case "console" => ConsoleDraw
        case "awt"     => AwtBuilder()
        case "fx"      => FXBuilder()
        case _         => BlackHoleDraw
      }
}
