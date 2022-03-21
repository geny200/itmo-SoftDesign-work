package database

import error.ApplicationError
import model.id.{SubjectId, UserId}
import model.{Subject, User}

trait ProductDB[F[_]] {
  def getUserById(id: UserId): F[Either[ApplicationError, User]]
  def getProductById(id: SubjectId): F[Either[ApplicationError, Subject]]
  def getAllProducts: F[Seq[Subject]]

  def addUser(user: User): F[Either[ApplicationError, Unit]]
  def addProduct(product: Subject): F[Either[ApplicationError, Unit]]
}

object ProductDB {
  def apply[F[_]](implicit db: ProductDB[F]): ProductDB[F] = db
}
