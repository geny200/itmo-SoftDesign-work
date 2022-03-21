package model

import cats.Functor
import cats.implicits.toFunctorOps
import sttp.tapir.Schema
import tethys.{JsonReader, JsonWriter}

sealed trait Currency {
  def value: Double
}

sealed trait BaseCurrency {
  def fromCoin[F[_]: Functor: Convertor](coin: Coin): F[Currency]
}

case class Dollar private (override val value: Double) extends Currency
case class Euro private (override val value: Double)   extends Currency
case class Ruble private (override val value: Double)  extends Currency

case object Dollar extends BaseCurrency {
  override def fromCoin[F[_]: Functor](coin: Coin)(implicit convertor: Convertor[F]): F[Currency] =
    convertor.coinToDollar.map(price => Dollar(price * coin.coin))
}

case object Euro extends BaseCurrency {
  override def fromCoin[F[_]: Functor](coin: Coin)(implicit convertor: Convertor[F]): F[Currency] =
    convertor.coinToEuro.map(price => Euro(price * coin.coin))
}

case object Ruble extends BaseCurrency {
  override def fromCoin[F[_]: Functor](coin: Coin)(implicit convertor: Convertor[F]): F[Currency] =
    convertor.coinToRuble.map(price => Ruble(price * coin.coin))
}

object Currency {
  implicit val currencyReader: JsonReader[Currency] =
    JsonReader.builder
      .addField[Long]("value")
      .addField[String]("currency")
      .buildReader {
        case (dollar, "$") => Dollar(dollar)
        case (euro, "€")   => Euro(euro)
        case (ruble, "₽")  => Ruble(ruble)
      }

  implicit val currencyWriter: JsonWriter[Currency] =
    JsonWriter
      .obj[Currency]
      .addField("value")(_.value)
      .addField("currency") {
        case Dollar(_) => "$"
        case Euro(_)   => "€"
        case Ruble(_)  => "₽"
      }

  implicit val currencySchema: Schema[Currency] =
    Schema
      .derived[Currency]
      .description("Цена в валюте")
}

object BaseCurrency {
  implicit val baseCurrencyWriter: JsonWriter[BaseCurrency] =
    JsonWriter.obj[BaseCurrency].addField[String]("type") {
      case Dollar => "dollar"
      case Euro   => "euro"
      case Ruble  => "ruble"
    }
  implicit val baseCurrencyReader: JsonReader[BaseCurrency] =
    JsonReader.builder.addField[String]("type").buildReader {
      case "dollar" => Dollar
      case "euro"   => Euro
      case "ruble"  => Ruble
    }

  implicit val baseCurrencySchema: Schema[BaseCurrency] =
    Schema
      .derived[BaseCurrency]
      .description("Валюта отображаемая пользователю")
}
