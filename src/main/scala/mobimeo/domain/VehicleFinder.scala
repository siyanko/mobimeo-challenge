package mobimeo.domain

import java.time.LocalTime

import mobimeo.repos.LinesRepo.LineName

object VehicleFinder {

  def find[F[_]](time: LocalTime, x: Int, y: Int): F[Option[LineName]] = ???
}
