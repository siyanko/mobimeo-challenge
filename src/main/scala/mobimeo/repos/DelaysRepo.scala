package mobimeo.repos

import mobimeo.repos.DelaysRepo.LineDelay
import mobimeo.repos.LinesRepo.LineName

trait DelaysRepo [F[_]] {
  def find(lineName: LineName): F[LineDelay]
}

object DelaysRepo {
  def apply[F[_]](implicit F: DelaysRepo[F]): DelaysRepo[F] = F

  final case class LineDelay(value: Int) extends AnyVal
  final case class Record(lineName: LineName, lineDelay: LineDelay)
}
