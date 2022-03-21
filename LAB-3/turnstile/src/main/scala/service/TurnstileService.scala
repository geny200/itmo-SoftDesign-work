package service

import cats.Monad
import cats.implicits.{catsSyntaxApplicativeId, toFlatMapOps}
import client.{CheckPassTurnstileClient, EntranceTurnstileClient}
import model.PassId

trait TurnstileService[F[_]] {
  def entrance(passId: PassId): F[Boolean]
  def quit(passId: PassId): F[Unit]
}

class TurnstileServiceImpl[F[_]: Monad](
    checkClient: CheckPassTurnstileClient[F],
    entranceClient: EntranceTurnstileClient[F]
) extends TurnstileService[F] {
  override def entrance(passId: PassId): F[Boolean] =
    checkClient.check(passId).flatTap {
      case true => entranceClient.entrance(passId)
      case _    => ().pure
    }

  override def quit(passId: PassId): F[Unit] =
    entranceClient.quit(passId)
}
