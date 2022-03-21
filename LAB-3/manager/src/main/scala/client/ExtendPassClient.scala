package client

import cats.Monad
import cats.implicits.{toFlatMapOps, toFunctorOps}
import event.UpdatePass
import model.PassId
import store.AddEventLogDB
import time.LocalTime

import java.time.LocalDate

trait ExtendPassClient[F[_]] {
  def extendPass(passId: PassId, expiration: LocalDate): F[Unit]
  def createPass(passId: PassId): F[Unit]
}

class ExtendPassClientImpl[F[_]: Monad: LocalTime: AddEventLogDB] extends ExtendPassClient[F] {
  override def extendPass(passId: PassId, expiration: LocalDate): F[Unit] =
    for {
      time <- LocalTime[F].localDate
      _    <- AddEventLogDB[F].addEvent(UpdatePass(passId, time, expiration))
    } yield ()

  override def createPass(passId: PassId): F[Unit] = for {
    time <- LocalTime[F].localDate
    _    <- AddEventLogDB[F].addEvent(UpdatePass(passId, time, time.toLocalDate))
  } yield ()

}
