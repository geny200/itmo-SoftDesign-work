package endpoint

import sttp.tapir.server.ServerEndpoint
import sttp.tapir.swagger.bundle.SwaggerInterpreter

trait ApplicationEndpoint[F[_], R] {
  this: ManagerLogicEndpoints[F, R]
    with TurnstileLogicEndpoints[F, R]
    with StatisticLogicEndpoints[F, R] =>

  private lazy val allLogicEndpoints: List[ServerEndpoint[R, F]] =
    turnstileEndpoints ++ managerEndpoints ++ statisticEndpoints

  private lazy val swaggerEndpoints: List[ServerEndpoint[R, F]] =
    SwaggerInterpreter().fromEndpoints(
      allLogicEndpoints.map(_.endpoint),
      "itmo-software-design-3-hw",
      "0.1.0-SNAPSHOT"
    )

  lazy val publicEndpoint: List[ServerEndpoint[R, F]] = swaggerEndpoints ++ allLogicEndpoints
}
