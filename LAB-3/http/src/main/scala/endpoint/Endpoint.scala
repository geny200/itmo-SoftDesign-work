package endpoint

import error.ApplicationError
import sttp.tapir.{PublicEndpoint, endpoint}
import sttp.tapir.json.tethysjson.jsonBody

trait Endpoint {
  val baseEndpoint: PublicEndpoint[Unit, ApplicationError, Unit, Any] =
    endpoint
      .errorOut(jsonBody[ApplicationError])
      .in("v1")
}

object Endpoint extends Endpoint
