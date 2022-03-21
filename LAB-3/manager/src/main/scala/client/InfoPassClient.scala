package client

import cats.Functor
import cats.implicits.toFunctorOps
import event.UpdatePass
import model.PassId
import store.GetEventLogDB

import java.time.LocalDate

trait InfoPassClient[F[_]] {
  def getPassInfo(passId: PassId): F[Option[LocalDate]]
}

class InfoPassClientImpl[F[_]: Functor: GetEventLogDB] extends InfoPassClient[F] {
  def getPassInfo(passId: PassId): F[Option[LocalDate]] =
    GetEventLogDB[F]
      .getEvents(passId)
      .map(
        _.flatMap {
          case UpdatePass(`passId`, _, expiration) => Some(expiration)
          case _                                   => None
        }.maxOption
      )
}
