package service.rest

import error.{ConnectionError, RestError}

import scala.util.Using

case class HttpGetByHost(host: String) extends RestGetApi {
  override def get(request: String): Either[RestError, String] = Using(
    scala.io.Source.fromURL(host + request)
  )(_
    .getLines()
    .fold("")(_ + _)
  ).toEither
  match {
    case Left(_) => Left(ConnectionError)
    case Right(value) => Right(value)
  }
}
