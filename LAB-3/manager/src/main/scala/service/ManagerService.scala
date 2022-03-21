package service

import client.{ExtendPassClient, InfoPassClient}
import model.PassId

import java.time.LocalDate

trait ManagerService[F[_]] {
  def info(passId: PassId): F[Option[LocalDate]]
  def extend(passId: PassId, expiration: LocalDate): F[Unit]
  def create(passId: PassId): F[Unit]
}

class ManagerServiceImpl[F[_]](
    infoPass: InfoPassClient[F],
    extendPass: ExtendPassClient[F]
) extends ManagerService[F] {
  def info(passId: PassId): F[Option[LocalDate]] =
    infoPass.getPassInfo(passId)

  def extend(passId: PassId, expiration: LocalDate): F[Unit] =
    extendPass.extendPass(passId, expiration)

  def create(passId: PassId): F[Unit] =
    extendPass.createPass(passId)
}
