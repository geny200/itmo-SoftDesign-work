package server

import database.InMemoryDB
import model.ConvertorImpl
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.Router
import sttp.capabilities.zio.ZioStreams
import sttp.tapir.server.http4s.ztapir.ZHttp4sServerInterpreter
import zio.interop.catz.implicits.rts
import zio.interop.catz.{asyncInstance, asyncRuntimeInstance}
import zio.{Clock, ExitCode, RIO, Task, URIO, ZEnv, ZIO, ZIOAppDefault}

object ZIOServer extends ZIOAppDefault {

  lazy val serve: ZIO[ZEnv, Throwable, Unit] =
    for {
      db <- InMemoryDB[Task]()

      endpoints       = new ProductServerEndpoints[Task, ZioStreams](db)
      serverEndpoints = endpoints.serverEndpoints
      routes          = ZHttp4sServerInterpreter().from(serverEndpoints).toRoutes

      serv <- ZIO.runtime[ZEnv].flatMap { implicit runtime =>
        BlazeServerBuilder[RIO[Clock, *]]
          .withExecutionContext(runtime.runtimeConfig.executor.asExecutionContext)
          .bindHttp(8080, "localhost")
          .withHttpApp(Router("/" -> routes).orNotFound)
          .serve
          .compile
          .drain
      }
    } yield serv

  override def run: URIO[ZEnv, ExitCode] =
    serve.exitCode

  private implicit val convertor: ConvertorImpl[ZIO[Any, Throwable, *]] =
    new ConvertorImpl[ZIO[Any, Throwable, *]]
}
