package visitor

import error.visitor.{MissingBraceError, VisitorError}
import token._

import scala.annotation.tailrec

final case class ParserVisitor(
    stack: Seq[PriorityToken],
    polishNotation: Seq[Token]
) extends TokenVisitor {

  override def visit(token: NumberToken): Either[VisitorError, TokenVisitor] =
    Right(copy(polishNotation = token +: polishNotation))

  @tailrec
  override def visit(token: Brace): Either[VisitorError, TokenVisitor] =
    token match {
      case CloseBracket =>
        stack match {
          case OpenBracket :: tail =>
            Right(copy(stack = tail))
          case head :: tail =>
            copy(stack = tail, polishNotation = head +: polishNotation)
              .visit(token)
          case Nil => Left(MissingBraceError)
        }
      case OpenBracket => Right(copy(stack = token +: stack))
    }

//  а) Если стек все еще пуст, или находящиеся в нем символы (а находится в нем могут только
//  знаки операций и открывающая скобка) имеют меньший приоритет, чем приоритет текущего символа,
//  то помещаем текущий символ в стек.
  @tailrec
  override def visit(token: Operation): Either[VisitorError, TokenVisitor] =
    stack match {
      case head :: tail =>
        if (head.priority >= token.priority)
          copy(stack = tail, polishNotation = head +: polishNotation)
            .visit(token)
        else
          Right(copy(stack = token :: head :: tail))
      case Nil => Right(copy(stack = Seq(token)))
    }

  def result: Seq[Token] =
    if (stack.nonEmpty)
      polishNotation.reverse ++ stack
    else
      polishNotation.reverse
}

object ParserVisitor {
  def apply(): ParserVisitor =
    ParserVisitor(Seq(), Seq())
}
