package model

import model.id.SubjectId
import sttp.tapir.Schema
import tethys.derivation.semiauto.{jsonReader, jsonWriter}
import tethys.{JsonReader, JsonWriter}

case class Subject(
    id: SubjectId,
    title: String,
    price: Coin,
    description: String
)

object Subject {
  implicit val subjectWriter: JsonWriter[Subject] = jsonWriter
  implicit val subjectReader: JsonReader[Subject] = jsonReader

  implicit val subjectSchema: Schema[Subject] =
    Schema
      .derived[Subject]
      .description("Товар")

  val example: Subject =
    Subject(
      SubjectId(0),
      "Laptop ROG Strix",
      Coin(1800),
      "Игровой ноутбук ASUS ROG Strix SCAR 17 G733QS-K4232T"
    )
}
