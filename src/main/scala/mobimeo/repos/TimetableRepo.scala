package mobimeo.repos

import java.time.LocalTime

import mobimeo.repos.LinesRepo.LineId
import mobimeo.repos.StopsRepo.StopId
import mobimeo.repos.TimetableRepo.ExpectedTime

trait TimetableRepo[F[_]] {
  def find(lineId: LineId, stopId: StopId): F[Option[ExpectedTime]]
}

object TimetableRepo {
  final case class ExpectedTime(value: LocalTime)
}
