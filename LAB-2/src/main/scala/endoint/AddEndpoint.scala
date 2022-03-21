package endoint

import error.ApplicationError
import sttp.tapir.PublicEndpoint

trait AddEndpoint[F[_], R] extends Endpoint[F, R] {
  protected override def baseEndpoint: PublicEndpoint[Unit, ApplicationError, Unit, Any] =
    super.baseEndpoint.post
      .in("add")
}
