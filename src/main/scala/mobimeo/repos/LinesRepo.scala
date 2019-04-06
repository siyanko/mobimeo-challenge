package mobimeo.repos

import mobimeo.repos.LinesRepo.{LineId, LineName}

trait LinesRepo[F[_]] {
  def find(id: LineId): F[LineName]
}

object LinesRepo{
  def apply[F[_]](implicit F: LinesRepo[F]): LinesRepo[F] = F

  final case class LineId(value: Int) extends AnyVal
  final case class LineName(value: String) extends AnyVal
}
