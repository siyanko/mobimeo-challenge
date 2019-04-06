package mobimeo.repos

import mobimeo.repos.StopsRepo.StopId

trait StopsRepo[F[_]] {
  def find(x: Int, y: Int): F[StopId]
}

object StopsRepo {
  def apply[F[_]](implicit F: StopsRepo[F]): StopsRepo[F] = F

  final case class StopId(value: Int) extends AnyVal
  final case class Record(id: StopId, x: Int, y: Int)
}
