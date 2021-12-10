package parser

import cats.data.StateT
import cats.implicits.toSemigroupKOps
import error.ParserError

object Parser {
  def eof[T, E <: ParserError](
      eofError: => E
  ): StateT[Either[E, *], Iterable[T], Unit] =
    StateT
      .inspectF[Either[E, *], Iterable[T], Unit] { line =>
        line.headOption.map(_ => eofError).toLeft(())
      }

  def some[T, R, E <: ParserError](
      s: StateT[Either[E, *], Iterable[T], R]
  ): StateT[Either[E, *], Iterable[T], Seq[R]] =
    s.flatMap(token => some(s).map(tokens => token +: tokens)) <+>
      s.map(Seq(_))

  def satisfy[T, E <: ParserError](symbol: T => Boolean)(error: T => E)(
      eofError: => E
  ): StateT[Either[E, *], Iterable[T], T] =
    StateT
      .inspectF[Either[E, *], Iterable[T], T] { line =>
        for {
          element <- line.headOption.toRight(eofError)
          correct <-
            if (symbol(element))
              Right(element)
            else
              Left(error(element))
        } yield correct
      }
      .modify(_.tail)

  def sequence[T, R, E <: ParserError](tokenName: Iterable[T], token: R)(
      error: T => E
  )(
      eofError: => E
  ): StateT[Either[E, *], Iterable[T], R] = for {
    _ <- tokenName.foldLeft(
      StateT.liftF[Either[E, *], Iterable[T], Unit](Right(()))
    )((state, currentElement: T) =>
      for {
        _ <- state
        _ <- satisfy({ x: T => x == currentElement })(error)(eofError)
      } yield ()
    )
  } yield token
}
