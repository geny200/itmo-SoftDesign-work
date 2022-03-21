package server

import cats.effect.kernel.Sync
import database.ProductDB
import endoint.{AddProductEndpoint, AddUserEndpoint, Endpoint, GetProductsEndpoint}
import model.Convertor
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.swagger.bundle.SwaggerInterpreter

class ProductServerEndpoints[F[_]: Sync: Convertor, R](db: ProductDB[F]) {
  lazy val serverEndpoints: List[ServerEndpoint[R, F]] =
    logicServerEndpoints ++ swaggerUIServerEndpoints

  private lazy val endpoints: List[Endpoint[F, R]] =
    List(
      new GetProductsEndpoint[F, R](),
      new AddProductEndpoint[F, R](),
      new AddUserEndpoint[F, R]()
    )

  private lazy val logicServerEndpoints: List[ServerEndpoint[R, F]] =
    endpoints.map(_.serverEndpoint)

  private lazy val swaggerUIServerEndpoints: List[ServerEndpoint[Any, F]] =
    SwaggerInterpreter().fromEndpoints(
      logicServerEndpoints.map(_.endpoint),
      "itmo-software-design-2-hw",
      "0.1.0-SNAPSHOT"
    )

  private implicit lazy val dataBase: ProductDB[F] = db
}
