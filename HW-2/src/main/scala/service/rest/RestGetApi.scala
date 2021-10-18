package service.rest

import error.RestError

trait RestGetApi {
  def get(request: String) : Either[RestError, String]
}
