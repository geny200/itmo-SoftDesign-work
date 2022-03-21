package endoint

import database.ProductDB
import error.ApplicationError
import model.Subject
import sttp.tapir.PublicEndpoint
import sttp.tapir.json.tethysjson.jsonBody
import sttp.tapir.server.ServerEndpoint

class AddProductEndpoint[F[_]: ProductDB, R] extends AddEndpoint[F, R] {

  private lazy val addProduct: PublicEndpoint[Subject, ApplicationError, Unit, Any] =
    baseEndpoint
      .in("product")
      .in(
        jsonBody[Subject]
          .description("Товар для добавления")
          .example(Subject.example)
      )

  private def addProductLogic(
      product: Subject
  ): F[Either[ApplicationError, Unit]] =
    ProductDB[F].addProduct(product)

  override lazy val serverEndpoint: ServerEndpoint[R, F] =
    addProduct.serverLogic(addProductLogic)
}
