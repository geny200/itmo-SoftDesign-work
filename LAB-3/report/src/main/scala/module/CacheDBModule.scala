package module

import cache.StatisticsDB

trait CacheDBModule[F[_]] {
  implicit def statisticsDB: StatisticsDB[F]
}

trait CacheDBModuleImpl[F[_]] extends CacheDBModule[F] {
  def cache: StatisticsDB[F]

  override implicit def statisticsDB: StatisticsDB[F] = cache
}
