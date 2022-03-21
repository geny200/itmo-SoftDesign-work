import cache.InMemoryCacheDB
import store.InMemoryDB
import zio.interop.catz.asyncRuntimeInstance
import zio.interop.catz.implicits.rts
import zio.{ExitCode, Task, ZEnv, ZIO, ZIOAppArgs, ZIOAppDefault}

object Application extends ZIOAppDefault {

  override def run: ZIO[ZEnv with ZIOAppArgs, Throwable, ExitCode] =
    for {
      globalEventStore <- InMemoryDB[Task]()
      statisticsCache  <- InMemoryCacheDB[Task](globalEventStore)
      app = new ApplicationServer(globalEventStore, statisticsCache)
      exitCode <- app.run
    } yield exitCode
}
