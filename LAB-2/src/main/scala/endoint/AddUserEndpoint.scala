package endoint

import database.ProductDB
import error.ApplicationError
import model.User
import sttp.tapir.PublicEndpoint
import sttp.tapir.json.tethysjson.jsonBody
import sttp.tapir.server.ServerEndpoint

class AddUserEndpoint[F[_]: ProductDB, R] extends AddEndpoint[F, R] {

  private lazy val addUser: PublicEndpoint[User, ApplicationError, Unit, Any] =
    baseEndpoint
      .in("user")
      .in(
        jsonBody[User]
          .description("Товар для добавления")
          .example(User.example)
      )

  private def addUserLogic(
      user: User
  ): F[Either[ApplicationError, Unit]] =
    ProductDB[F].addUser(user)

  override lazy val serverEndpoint: ServerEndpoint[R, F] =
    addUser.serverLogic(addUserLogic)
}
