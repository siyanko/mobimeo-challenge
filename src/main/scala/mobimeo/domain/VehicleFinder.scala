package mobimeo.domain

import java.time.LocalTime

import cats.Monad
import cats.implicits._
import mobimeo.repos.DelaysRepo.LineDelay
import mobimeo.repos.LinesRepo.LineName
import mobimeo.repos.{DelaysRepo, LinesRepo, StopsRepo, TimetableRepo}

object VehicleFinder {

  def find[F[_] : Monad : StopsRepo : TimetableRepo : LinesRepo : DelaysRepo](time: LocalTime, x: Int, y: Int): F[Option[LineName]] = for {
    stopId <- StopsRepo[F].find(x, y)
    stopTimetable <- TimetableRepo[F].findStopTimetable(stopId)
    timetable <- stopTimetable.traverse[F, (LineName, LocalTime, LineDelay)](timeSlot => for {
      lineName <- LinesRepo[F].find(timeSlot.lineId)
      lineDelay <- DelaysRepo[F].find(lineName)
    } yield (lineName, timeSlot.time, lineDelay)
    )
  } yield timetable
    .map { case (lineName, expectedTime, delay) =>
      (lineName, expectedTime.plusMinutes(delay.value))
    }
    .find { case (_, actualTime) => actualTime.isAfter(time) || actualTime.equals(time) }
    .map(_._1)
}
