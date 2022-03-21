package endpoint

import sttp.tapir.server.ServerEndpoint

trait ManagerLogicEndpoints[F[_], R] {
  def managerEndpoints: List[ServerEndpoint[R, F]]
}

trait ManagerLogicEndpointsImpl[F[_], R] extends ManagerLogicEndpoints[F, R] {
  this: ExtendManagerEndpoint[F, R]
    with CreateManagerEndpoint[F, R]
    with InfoManagerEndpoint[F, R] =>

  override lazy val managerEndpoints: List[ServerEndpoint[R, F]] =
    List(extendEndpoint, createEndpoint, infoEndpoint)
}
