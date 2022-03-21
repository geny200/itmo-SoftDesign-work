package module

import client.{ReportClient, ReportClientImpl}

trait ReportModule[F[_]] {
  def reportClient: ReportClient[F]
}

trait ReportModuleImpl[F[_]] extends ReportModule[F] {
  this: FunctorModule[F] with CacheDBModule[F] =>

  override lazy val reportClient: ReportClient[F] =
    new ReportClientImpl()
}
