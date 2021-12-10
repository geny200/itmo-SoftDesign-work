package token

import error.visitor.VisitorError
import visitor.TokenVisitor

sealed trait Operation extends PriorityToken {
  override def accept(
      visitor: TokenVisitor
  ): Either[VisitorError, TokenVisitor] =
    visitor.visit(this)
}

case object Addition extends Operation {
  override def priority: Int = 2
}
case object Subtraction extends Operation {
  override def priority: Int = 2
}
case object Multiplication extends Operation {
  override def priority: Int = 3
}
case object Division extends Operation {
  override def priority: Int = 3
}
