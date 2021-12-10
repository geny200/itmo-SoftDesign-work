package token

import error.visitor.VisitorError
import visitor.TokenVisitor

final case class NumberToken(number: Int) extends Token {
  override def accept(
      visitor: TokenVisitor
  ): Either[VisitorError, TokenVisitor] =
    visitor.visit(this)
}
