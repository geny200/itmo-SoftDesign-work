package collection

object Traverse {
  def traverse[E, T](seq: Seq[Either[E, T]]): Either[E, List[T]] =
    seq.foldRight[Either[E, List[T]]](Right(List.empty))(
      (count, acc) => for {
        list <- acc
        value <- count
      } yield value :: list
    )
}
