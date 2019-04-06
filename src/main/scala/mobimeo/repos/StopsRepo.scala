package mobimeo.repos

import mobimeo.repos.StopsRepo.{StopId, StopLocation}

trait StopsRepo[F[_]] {
  def find (id: StopId): F[Option[StopLocation]]
}

object StopsRepo {
  final case class StopId(value: Int) extends AnyVal
  final case class StopLocation(x: Int, y: Int)
}
