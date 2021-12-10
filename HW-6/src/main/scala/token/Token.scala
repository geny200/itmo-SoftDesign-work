package token

import error.visitor.VisitorError
import visitor.TokenVisitor

trait Token {
  def accept(visitor: TokenVisitor): Either[VisitorError, TokenVisitor]
}
