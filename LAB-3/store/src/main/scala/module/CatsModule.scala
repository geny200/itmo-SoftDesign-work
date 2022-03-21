package module

import cats.effect.{Async, Clock}
import cats.{Functor, Monad}
import zio.Task

trait FunctorModule[F[_]] {
  implicit def functor: Functor[F]
}

trait MonadModule[F[_]] {
  implicit def monad: Monad[F]
}

trait AsyncModule[F[_]] {
  implicit def async: Async[F]
}

trait ClockModule[F[_]] {
  implicit def clock: Clock[F]
}

trait CatsModule[F[_]]
    extends FunctorModule[F]
    with ClockModule[F]
    with MonadModule[F]
    with AsyncModule[F]

trait ZIOTaskModule extends CatsModule[Task] {
  import zio.interop.catz.asyncRuntimeInstance
  import zio.interop.catz.implicits.rts

  override def clock: Clock[Task]     = asyncRuntimeInstance
  override def async: Async[Task]     = asyncRuntimeInstance
  override def monad: Monad[Task]     = asyncRuntimeInstance
  override def functor: Functor[Task] = asyncRuntimeInstance
}
