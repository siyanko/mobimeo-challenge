package mobimeo.repos

import mobimeo.repos.DelaysRepo.LineDelay
import mobimeo.repos.LinesRepo.LineName

trait DelaysRepo [F[_]] {
  def find(lineName: LineName): F[Option[LineDelay]]
}

object DelaysRepo {
  final case class LineDelay(value: Int) extends AnyVal
}
