package endpoint

import endpoint.Endpoint.baseEndpoint
import error.ApplicationError
import sttp.tapir.PublicEndpoint

trait StatisticEndpoint {
  val statisticEndpoint: PublicEndpoint[Unit, ApplicationError, Unit, Any] =
    baseEndpoint.get
      .in("statistic")
}

object StatisticEndpoint extends StatisticEndpoint
