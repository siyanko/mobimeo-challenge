package mobimeo.domain

import java.time.LocalTime

import cats.Id
import mobimeo.repos.DelaysRepo.LineDelay
import mobimeo.repos.LinesRepo.{LineId, LineName}
import mobimeo.repos.StopsRepo.StopId
import mobimeo.repos.TimetableRepo.TimetableSlot
import mobimeo.repos.{DelaysRepo, LinesRepo, StopsRepo, TimetableRepo}
import org.scalatest.{Matchers, OptionValues, WordSpec}

class VehicleFinderSpec extends WordSpec with Matchers with OptionValues {
  "VehicleFinder" should {
    "find a vehicle" in {
      implicit val idStopsRepo: StopsRepo[Id] = new StopsRepo[Id] {
        override def find(x: Int, y: Int): Id[StopId] = StopId(3)
      }

      implicit val idTimetableRepo: TimetableRepo[Id] = new TimetableRepo[Id] {
        override def findStopTimetable(stopId: StopId): Id[List[TimetableSlot]] = List(
          TimetableSlot(LineId(0), LocalTime.parse("10:07:00")),
        )
      }

      implicit val idLinesRepo: LinesRepo[Id] = new LinesRepo[Id] {
        override def find(id: LineId): Id[LineName] = LineName("M4")
      }

      implicit val idDelaysRepo: DelaysRepo[Id] = new DelaysRepo[Id] {
        override def find(lineName: LineName): Id[LineDelay] = LineDelay(1)
      }

      val time = LocalTime.parse("10:06:00")

      VehicleFinder.find[Id](time, 1, 7).value.value should be("M4")
    }
  }

}
