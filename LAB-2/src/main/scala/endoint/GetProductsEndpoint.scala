package endoint

import cats.Monad
import cats.data.EitherT
import cats.implicits.{catsSyntaxApplicativeId, toFlatMapOps, toFunctorOps}
import database.ProductDB
import error.ApplicationError
import model.id.UserId
import model.{Convertor, CustomizeSubject, Subject, User}
import sttp.tapir.json.tethysjson.jsonBody
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.{PublicEndpoint, query}

class GetProductsEndpoint[F[_]: Monad: Convertor: ProductDB, R] extends Endpoint[F, R] {

  private lazy val getProducts
      : PublicEndpoint[UserId, ApplicationError, Seq[CustomizeSubject], Any] =
    baseEndpoint.get
      .in("all")
      .in(
        query[Long]("id")
          .map(UserId(_))(_.id)
          .description("Id пользователя")
          .example(UserId.example)
      )
      .out(
        jsonBody[Seq[CustomizeSubject]]
          .example(Seq(CustomizeSubject.example))
          .description("Список всех товаров")
      )

  private def getProductsLogic(
      userId: UserId
  ): F[Either[ApplicationError, Seq[CustomizeSubject]]] = {
    for {
      allProducts <- EitherT.liftF[F, ApplicationError, Seq[Subject]](ProductDB[F].getAllProducts)
      user        <- EitherT[F, ApplicationError, User](ProductDB[F].getUserById(userId))
      subjects <- EitherT.liftF[F, ApplicationError, Seq[CustomizeSubject]](
        allProducts
          .map(CustomizeSubject(_, user.currency))
          .foldLeft(Seq[CustomizeSubject]().pure[F]) { (seqF, subjectF) =>
            for {
              seq     <- seqF
              subject <- subjectF
            } yield subject +: seq
          }
      )
    } yield subjects
  }.value

  override lazy val serverEndpoint: ServerEndpoint[R, F] =
    getProducts.serverLogic(getProductsLogic)
}
