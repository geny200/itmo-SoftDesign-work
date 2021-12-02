package error

object AbsentInputError extends AppError {
  override def message: String = "Missing input argument"
}
