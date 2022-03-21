package module

import client.{CheckPassTurnstileClientImpl, EntranceTurnstileClientImpl}
import service.{TurnstileService, TurnstileServiceImpl}

trait TurnstileModule[F[_]] {
  def turnstileService: TurnstileService[F]
}

trait TurnstileModuleImpl[F[_]] extends TurnstileModule[F] {
  this: LocalTimeModule[F]
    with MonadModule[F]
    with GetEventStoreModule[F]
    with AddEventStoreModule[F] =>

  override lazy val turnstileService: TurnstileService[F] = {
    val checkClient    = new CheckPassTurnstileClientImpl()
    val entranceClient = new EntranceTurnstileClientImpl()

    new TurnstileServiceImpl(checkClient, entranceClient)
  }
}
