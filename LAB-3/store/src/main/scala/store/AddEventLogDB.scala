package store

import event.Event

trait AddEventLogDB[F[_]] {
  def addEvent(event: Event): F[Unit]
}

object AddEventLogDB {
  def apply[F[_]](implicit store: AddEventLogDB[F]): AddEventLogDB[F] = store
}
