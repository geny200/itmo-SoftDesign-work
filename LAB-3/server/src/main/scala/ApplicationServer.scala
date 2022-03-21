import cache.StatisticsDB
import endpoint._
import module._
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.Router
import store.{AddEventLogDB, GetEventLogDB}
import sttp.tapir.server.http4s.ztapir.ZHttp4sServerInterpreter
import zio.interop.catz.asyncInstance
import zio.{Clock, ExitCode, RIO, Task, URIO, ZEnv, ZIO}

class ApplicationServer(
    globalEventStore: AddEventLogDB[Task] with GetEventLogDB[Task],
    globalCache: StatisticsDB[Task]
) extends ZIOTaskModule
    with LocalTimeModuleImpl[Task]
    with EventStoreModuleImpl[Task]
    with ManagerLogicEndpointsImpl[Task, Any]
    with ExtendManagerEndpointImpl[Task, Any]
    with InfoManagerEndpointImpl[Task, Any]
    with CreateManagerEndpointImpl[Task, Any]
    with ManagerModuleImpl[Task]
    with TurnstileEndpointsImpl[Task, Any]
    with QuitTurnstileEndpointImpl[Task, Any]
    with EntranceTurnstileEndpointImpl[Task, Any]
    with TurnstileModuleImpl[Task]
    with StatisticLogicEndpointsImpl[Task, Any]
    with VisitStatisticEndpointImpl[Task, Any]
    with WeekStatisticEndpointImpl[Task, Any]
    with ReportModuleImpl[Task]
    with CacheDBModuleImpl[Task]
    with ApplicationEndpoint[Task, Any] {

  lazy val run: URIO[ZEnv, ExitCode] = {
    val routes = ZHttp4sServerInterpreter().from(publicEndpoint).toRoutes

    val serve = ZIO.runtime[ZEnv].flatMap { implicit runtime =>
      BlazeServerBuilder[RIO[Clock, *]]
        .withExecutionContext(runtime.runtimeConfig.executor.asExecutionContext)
        .bindHttp(8080, "localhost")
        .withHttpApp(Router("/" -> routes).orNotFound)
        .serve
        .compile
        .drain
    }
    serve.exitCode
  }

  override lazy val eventStore: AddEventLogDB[Task] with GetEventLogDB[Task] = globalEventStore
  override lazy val cache: StatisticsDB[Task]                                = globalCache
}
