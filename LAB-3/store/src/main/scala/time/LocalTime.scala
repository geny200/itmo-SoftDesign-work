package time

import cats.Functor
import cats.effect.Clock
import cats.implicits.toFunctorOps

import java.time.{Instant, LocalDateTime, ZoneId}

trait LocalTime[F[_]] {
  def localDate: F[LocalDateTime]
}

object LocalTime {
  def apply[F[_]](implicit ev: LocalTime[F]): LocalTime[F] = ev

  implicit def timeInstance[F[_]: Functor: Clock]: LocalTime[F] = new LocalTime[F] {
    override def localDate: F[LocalDateTime] =
      for {
        time <- Clock[F].realTime
        instantNow = Instant.ofEpochSecond(time.toSeconds)
      } yield LocalDateTime.ofInstant(instantNow, ZoneId.systemDefault())
  }
}
