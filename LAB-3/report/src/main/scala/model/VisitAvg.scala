package model

import sttp.tapir.Schema
import tethys.{JsonReader, JsonWriter}

import java.time.Duration
import java.time.temporal.ChronoUnit.SECONDS
import scala.concurrent.duration.DurationInt

case class VisitAvg(visits: Double, duration: Duration)

object VisitAvg {
  implicit val visitAvgWriter: JsonWriter[VisitAvg] =
    JsonWriter
      .obj[VisitAvg]
      .addField("visits")(_.visits)
      .addField("duration")(_.duration.get(SECONDS).toDouble / 1.hour.toSeconds)
  implicit val visitAvgReader: JsonReader[VisitAvg] =
    JsonReader.builder
      .addField[Double]("visits")
      .addField[Double]("duration")
      .buildReader { (visits, duration) =>
        VisitAvg(visits, Duration.ofSeconds((1.hour.toSeconds * duration).toLong))
      }

  implicit val visitAvgSchema: Schema[VisitAvg] =
    Schema
      .derived[VisitAvg]
      .description("Статистика по посещениям")

  val example: VisitAvg = VisitAvg(42, Duration.ofHours(1))
}
