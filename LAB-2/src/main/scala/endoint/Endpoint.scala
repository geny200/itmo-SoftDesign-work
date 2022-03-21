package endoint

import error.ApplicationError
import sttp.tapir.json.tethysjson.jsonBody
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.{PublicEndpoint, endpoint}

trait Endpoint[F[_], R] {
  protected def baseEndpoint: PublicEndpoint[Unit, ApplicationError, Unit, Any] =
    endpoint
      .errorOut(jsonBody[ApplicationError])
      .in("v1")

  def serverEndpoint: ServerEndpoint[R, F]
}
