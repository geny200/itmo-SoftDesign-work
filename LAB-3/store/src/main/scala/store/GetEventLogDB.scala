package store

import event.Event
import model.PassId

trait GetEventLogDB[F[_]] {
  def getEvents(passId: PassId): F[Seq[Event]] =
    getWithFilter(_.passId == passId)

  def getWithFilter(filter: Event => Boolean): F[Seq[Event]]
}

object GetEventLogDB {
  def apply[F[_]](implicit store: GetEventLogDB[F]): GetEventLogDB[F] = store
}
