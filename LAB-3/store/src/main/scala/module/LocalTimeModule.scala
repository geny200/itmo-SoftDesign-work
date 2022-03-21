package module

import time.LocalTime

trait LocalTimeModule[F[_]] {
  implicit def localTime: LocalTime[F]
}

trait LocalTimeModuleImpl[F[_]] extends LocalTimeModule[F] {
  this: ClockModule[F] with FunctorModule[F] =>

  override def localTime: LocalTime[F] = LocalTime.timeInstance[F]
}
