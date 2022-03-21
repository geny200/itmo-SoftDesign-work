package endpoint

import sttp.tapir.server.ServerEndpoint

trait TurnstileLogicEndpoints[F[_], R] {
  def turnstileEndpoints: List[ServerEndpoint[R, F]]
}

trait TurnstileEndpointsImpl[F[_], R] extends TurnstileLogicEndpoints[F, R] {
  this: QuitTurnstileEndpoint[F, R] with EntranceTurnstileEndpoint[F, R] =>

  override lazy val turnstileEndpoints: List[ServerEndpoint[R, F]] =
    List(quitEndpoint, entranceEndpoint)
}
