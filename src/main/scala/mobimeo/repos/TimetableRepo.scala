package mobimeo.repos

import java.time.LocalTime

import mobimeo.repos.LinesRepo.LineId
import mobimeo.repos.StopsRepo.StopId
import mobimeo.repos.TimetableRepo.TimetableSlot

trait TimetableRepo[F[_]] {
  def findStopTimetable(stopId: StopId): F[List[TimetableSlot]]
}

object TimetableRepo {
  def apply[F[_]](implicit F: TimetableRepo[F]): TimetableRepo[F] = F

  final case class TimetableSlot(lineId: LineId, time: LocalTime)
  final case class Record(lineId: LineId, stopId: StopId, time: LocalTime)
}
