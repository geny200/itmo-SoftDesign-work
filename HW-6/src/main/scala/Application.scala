import error.EmptyInputError
import executor.Calculator

object Application {
  def main(args: Array[String]): Unit =
    (for {
      expr <- args.headOption.toRight(EmptyInputError)
      result <- Calculator.evaluate(expr)
    } yield s"($expr) -> ${result.polishNotation}= ${result.value}") match {
      case Left(error)  => println(s"error: $error ${args.head}")
      case Right(value) => println(value)
    }
}
