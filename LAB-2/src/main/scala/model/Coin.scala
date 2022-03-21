package model

import sttp.tapir.Schema
import tethys.derivation.semiauto.{jsonReader, jsonWriter}
import tethys.{JsonReader, JsonWriter}

case class Coin(coin: Long)

object Coin {
  implicit val coinWriter: JsonWriter[Coin] = jsonWriter
  implicit val coinReader: JsonReader[Coin] = jsonReader

  implicit val coinSchema: Schema[Coin] =
    Schema
      .derived[Coin]
      .description("Внутренняя не конвертируемая цена товара")
}
