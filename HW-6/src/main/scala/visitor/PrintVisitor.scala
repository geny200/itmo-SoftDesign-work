package visitor

import error.visitor.{BraceError, VisitorError}
import token._

final case class PrintVisitor(output: StringBuilder) extends TokenVisitor {

  override def visit(token: NumberToken): Either[VisitorError, PrintVisitor] =
    Right(copy(output = output.append(s"${token.number} ")))

  override def visit(token: Brace): Either[VisitorError, PrintVisitor] =
    Left(BraceError)

  override def visit(token: Operation): Either[VisitorError, PrintVisitor] = {
    Right(copy(output = output.append(token match {
      case Addition       => "+ "
      case Subtraction    => "- "
      case Multiplication => "* "
      case Division       => "/ "
    })))
  }

  def result: String =
    output.result()
}

object PrintVisitor {
  def apply(): PrintVisitor =
    PrintVisitor(new StringBuilder())
}
