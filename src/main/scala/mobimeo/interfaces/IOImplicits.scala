package mobimeo.interfaces

import cats.effect.IO
import mobimeo.repos.{DelaysRepo, LinesRepo, StopsRepo, TimetableRepo}

object IOImplicits {

  implicit def ioStopsRepo: StopsRepo[IO] = new StopsRepo[IO] {
    override def find(x: Int, y: Int): IO[StopsRepo.StopId] = ???
  }

  implicit def ioTimetableRepo: TimetableRepo[IO] = new TimetableRepo[IO] {
    override def findStopTimetable(stopId: StopsRepo.StopId): IO[List[TimetableRepo.TimetableSlot]] = ???
  }

  implicit def ioLinesRepo: LinesRepo[IO] = new LinesRepo[IO]{
    override def find(id: LinesRepo.LineId): IO[LinesRepo.LineName] = ???
  }

  implicit def ioDelaysRepo: DelaysRepo[IO] = new DelaysRepo[IO]{
    override def find(lineName: LinesRepo.LineName): IO[DelaysRepo.LineDelay] = ???
  }

}
