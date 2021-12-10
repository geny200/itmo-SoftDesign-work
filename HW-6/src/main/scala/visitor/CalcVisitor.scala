package visitor
import error.visitor._
import token._

final case class CalcVisitor(stack: Seq[NumberToken]) extends TokenVisitor {

  override def visit(token: NumberToken): Either[VisitorError, CalcVisitor] =
    Right(copy(stack = token +: stack))

  override def visit(token: Brace): Either[VisitorError, CalcVisitor] =
    Left(BraceError)

  override def visit(token: Operation): Either[VisitorError, CalcVisitor] =
    for {
      fst <- stack.headOption.toRight(MissingArgumentError)
      snd <- stack.tail.headOption.toRight(MissingArgumentError)
      _ = println(stack)
      executor = (operation: (Int, Int) => Int) =>
        copy(stack =
          NumberToken(operation(snd.number, fst.number)) +: stack.tail.tail
        )
    } yield token match {
      case Addition       => executor(_ + _)
      case Subtraction    => executor(_ - _)
      case Multiplication => executor(_ * _)
      case Division       => executor(_ / _)
    }

  def result: Either[VisitorError, NumberToken] =
    stack match {
      case Nil         => Left(EmptyExpressionError)
      case head :: Nil => Right(head)
      case _           => Left(ToManyArgumentsError)
    }
}

object CalcVisitor {
  def apply(): CalcVisitor =
    CalcVisitor(Seq())
}
