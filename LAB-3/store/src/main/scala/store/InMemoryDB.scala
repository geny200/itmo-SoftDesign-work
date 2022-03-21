package store

import cats.Functor
import cats.effect.{Async, Ref}
import cats.implicits.toFunctorOps
import event.Event

class InMemoryDB[F[_]: Functor](eventsRef: Ref[F, Seq[Event]])
    extends GetEventLogDB[F]
    with AddEventLogDB[F] {
  override def getWithFilter(filter: Event => Boolean): F[Seq[Event]] =
    eventsRef.get.map(_.filter(filter))

  override def addEvent(event: Event): F[Unit] =
    eventsRef.update(events => event +: events)
}

object InMemoryDB {
  def apply[F[_]: Async](
      default: Seq[Event] = Seq.empty
  ): F[AddEventLogDB[F] with GetEventLogDB[F]] =
    for {
      events <- Ref.of[F, Seq[Event]](default)
    } yield new InMemoryDB(events)
}
