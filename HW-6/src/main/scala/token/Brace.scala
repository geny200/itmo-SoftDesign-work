package token

import error.visitor.VisitorError
import visitor.TokenVisitor

trait Brace extends PriorityToken {
  override def accept(
      visitor: TokenVisitor
  ): Either[VisitorError, TokenVisitor] =
    visitor.visit(this)

  override def priority: Int = 1
}

case object OpenBracket extends Brace
case object CloseBracket extends Brace
