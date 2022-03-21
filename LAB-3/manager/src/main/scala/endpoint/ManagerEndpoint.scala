package endpoint

import endpoint.Endpoint.baseEndpoint
import error.ApplicationError
import sttp.tapir.PublicEndpoint

trait ManagerEndpoint {
  val managerEndpoint: PublicEndpoint[Unit, ApplicationError, Unit, Any] =
    baseEndpoint
      .in("manager")
}

object ManagerEndpoint extends ManagerEndpoint
