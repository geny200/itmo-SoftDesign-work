package client

import cats.Monad
import cats.implicits.{toFlatMapOps, toFunctorOps}
import event.UpdatePass
import model.PassId
import store.GetEventLogDB
import time.LocalTime

trait CheckPassTurnstileClient[F[_]] {
  def check(passId: PassId): F[Boolean]
}

class CheckPassTurnstileClientImpl[F[_]: Monad: LocalTime: GetEventLogDB]
    extends CheckPassTurnstileClient[F] {
  override def check(passId: PassId): F[Boolean] =
    for {
      currentTime <- LocalTime[F].localDate
      currentDate = currentTime.toLocalDate
      result <- GetEventLogDB[F]
        .getEvents(passId)
        .map(_.exists {
          case UpdatePass(`passId`, _, expiration) if expiration.isAfter(currentDate) => true
          case _                                                                      => false
        })
    } yield result
}
