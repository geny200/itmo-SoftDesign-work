package model

import cats.implicits.catsSyntaxApplicativeId
import cats.{Applicative, Id}

trait Convertor[F[_]] {
  def coinToEuro: F[Double]
  def coinToRuble: F[Double]
  def coinToDollar: F[Double]
}

object Convertor {
  val example: Convertor[Id] = new ConvertorImpl[Id]()
}

class ConvertorImpl[F[_]: Applicative] extends Convertor[F] {
  override def coinToEuro: F[Double]   = 1.2d.pure
  override def coinToRuble: F[Double]  = 100d.pure
  override def coinToDollar: F[Double] = 1.05d.pure
}
