package endpoint

import endpoint.Endpoint.baseEndpoint
import error.ApplicationError
import sttp.tapir.PublicEndpoint

trait TurnstileEndpoint {
  val turnstileEndpoint: PublicEndpoint[Unit, ApplicationError, Unit, Any] =
    baseEndpoint
      .in("turnstile")
}

object TurnstileEndpoint extends TurnstileEndpoint
