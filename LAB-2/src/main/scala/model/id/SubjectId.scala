package model.id

import sttp.tapir.Schema
import tethys.derivation.semiauto.{jsonReader, jsonWriter}
import tethys.{JsonReader, JsonWriter}

case class SubjectId(id: Long)

object SubjectId {
  implicit val subjectIdWriter: JsonWriter[SubjectId] = jsonWriter
  implicit val subjectIdReader: JsonReader[SubjectId] = jsonReader

  implicit val subjectIdSchema: Schema[SubjectId] =
    Schema
      .derived[SubjectId]
      .description("Уникальный идентификатор товара")
}
