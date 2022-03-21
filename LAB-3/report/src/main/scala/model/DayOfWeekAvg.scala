package model

import sttp.tapir.Schema
import tethys.derivation.semiauto.{jsonReader, jsonWriter}
import tethys.{JsonReader, JsonWriter}

case class DayOfWeekAvg(visits: Double)

object DayOfWeekAvg {
  implicit val writer: JsonWriter[DayOfWeekAvg] = jsonWriter
  implicit val reader: JsonReader[DayOfWeekAvg] = jsonReader

  implicit val schema: Schema[DayOfWeekAvg] =
    Schema
      .derived[DayOfWeekAvg]
      .description("Статистика посещений по дням (в нормализованном виде)")

  val example: DayOfWeekAvg = DayOfWeekAvg(0.32d)
}
