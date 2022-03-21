package endpoint

import sttp.tapir.server.ServerEndpoint

trait StatisticLogicEndpoints[F[_], R] {
  def statisticEndpoints: List[ServerEndpoint[R, F]]
}

trait StatisticLogicEndpointsImpl[F[_], R] extends StatisticLogicEndpoints[F, R] {
  this: VisitStatisticEndpoint[F, R] with WeekStatisticEndpoint[F, R] =>

  override lazy val statisticEndpoints: List[ServerEndpoint[R, F]] =
    List(weekEndpoint, visitEndpoint)
}
