package module

import client.{ExtendPassClientImpl, InfoPassClientImpl}
import service.{ManagerService, ManagerServiceImpl}

trait ManagerModule[F[_]] {
  def managerService: ManagerService[F]
}

trait ManagerModuleImpl[F[_]] extends ManagerModule[F] {
  this: MonadModule[F]
    with GetEventStoreModule[F]
    with AddEventStoreModule[F]
    with LocalTimeModule[F] =>

  override def managerService: ManagerService[F] = {
    val infoPass   = new InfoPassClientImpl()
    val extendPass = new ExtendPassClientImpl()

    new ManagerServiceImpl(infoPass, extendPass)
  }
}
