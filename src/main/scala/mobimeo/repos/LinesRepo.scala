package mobimeo.repos

import mobimeo.repos.LinesRepo.{LineId, LineName}

trait LinesRepo[F[_]] {
  def find(id: LineId): F[Option[LineName]]
}

object LinesRepo{
  final case class LineId(value: Int) extends AnyVal
  final case class LineName(value: String) extends AnyVal
}
