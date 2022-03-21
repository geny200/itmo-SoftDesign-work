package client

import cats.Monad
import cats.implicits.{toFlatMapOps, toFunctorOps}
import event.{UseInPass, UseOutPass}
import model.PassId
import store.AddEventLogDB
import time.LocalTime

trait EntranceTurnstileClient[F[_]] {
  def entrance(passId: PassId): F[Unit]
  def quit(passId: PassId): F[Unit]
}

class EntranceTurnstileClientImpl[F[_]: Monad: LocalTime: AddEventLogDB]
    extends EntranceTurnstileClient[F] {
  override def entrance(passId: PassId): F[Unit] = for {
    time <- LocalTime[F].localDate
    _    <- AddEventLogDB[F].addEvent(UseInPass(passId, time))
  } yield ()

  override def quit(passId: PassId): F[Unit] = for {
    time <- LocalTime[F].localDate
    _    <- AddEventLogDB[F].addEvent(UseOutPass(passId, time))
  } yield ()
}
