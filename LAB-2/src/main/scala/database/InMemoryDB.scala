package database

import cats.Monad
import cats.effect.{Async, Ref}
import cats.implicits.{catsSyntaxApplicativeId, catsSyntaxEitherId, toFlatMapOps, toFunctorOps}
import error._
import model.id.{SubjectId, UserId}
import model.{Subject, User}

class InMemoryDB[F[_]: Monad](
    productsRef: Ref[F, Seq[Subject]],
    usersRef: Ref[F, Seq[User]]
) extends ProductDB[F] {

  override def getUserById(id: UserId): F[Either[ApplicationError, User]] =
    usersRef.get
      .map(
        _.find(_.userId == id)
          .toRight(UserNotFoundError)
      )

  override def getProductById(id: SubjectId): F[Either[ApplicationError, Subject]] =
    productsRef.get
      .map(
        _.find(_.id == id)
          .toRight(ProductFoundError)
      )

  override def getAllProducts: F[Seq[Subject]] =
    productsRef.get

  override def addUser(user: User): F[Either[ApplicationError, Unit]] =
    getUserById(user.userId).flatMap {
      case Right(_) => (UserAlreadyExistError: ApplicationError).asLeft[Unit].pure
      case Left(_) =>
        usersRef.modify { users =>
          users.find(_.userId == user.userId) match {
            case Some(_) => (users, UserAlreadyExistError.asLeft)
            case None    => (user +: users, ().asRight)
          }
        }
    }

  override def addProduct(product: Subject): F[Either[ApplicationError, Unit]] =
    getProductById(product.id).flatMap {
      case Right(_) => (ProductAlreadyExistError: ApplicationError).asLeft[Unit].pure
      case Left(_) =>
        productsRef.modify { products =>
          products.find(_.id == product.id) match {
            case Some(_) => (products, ProductAlreadyExistError.asLeft)
            case None    => (product +: products, ProductAlreadyExistError.asLeft)
          }
        }
    }
}

object InMemoryDB {
  def apply[F[_]: Async](): F[ProductDB[F]] =
    for {
      users    <- Ref.of[F, Seq[User]](List(User.example))
      products <- Ref.of[F, Seq[Subject]](List(Subject.example))
    } yield new InMemoryDB[F](products, users)
}
