package module

import store.{AddEventLogDB, GetEventLogDB}

trait GetEventStoreModule[F[_]] {
  implicit def eventStoreGet: GetEventLogDB[F]
}

trait AddEventStoreModule[F[_]] {
  implicit def eventStoreAdd: AddEventLogDB[F]
}

trait EventStoreModule[F[_]] extends AddEventStoreModule[F] with GetEventStoreModule[F]

trait EventStoreModuleImpl[F[_]] extends EventStoreModule[F] {
  def eventStore: AddEventLogDB[F] with GetEventLogDB[F]

  override implicit def eventStoreAdd: AddEventLogDB[F] = eventStore
  override implicit def eventStoreGet: GetEventLogDB[F] = eventStore
}
