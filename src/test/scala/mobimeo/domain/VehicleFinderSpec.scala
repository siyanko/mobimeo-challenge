package mobimeo.domain

import java.time.LocalTime

import cats.Id
import mobimeo.repos.DelaysRepo.LineDelay
import mobimeo.repos.LinesRepo.{LineId, LineName}
import mobimeo.repos.StopsRepo.StopId
import mobimeo.repos.TimetableRepo.TimetableSlot
import mobimeo.repos.{DelaysRepo, LinesRepo, StopsRepo, TimetableRepo}
import org.scalatest.{Matchers, WordSpec}

class VehicleFinderSpec extends WordSpec with Matchers {
  "VehicleFinder" should {
    "find a vehicle" in {
      val time = LocalTime.parse("10:06:00")

      VehicleFinder.find[Id](time, 1, 7).map(_.value) should contain ("M4")
    }

    "find all vehicles if there is more options" in {
      val time = LocalTime.parse("10:08:00")

      VehicleFinder.find[Id](time, 2, 9).map(_.value) should contain allOf("200", "S75")
    }
  }

  implicit val idStopsRepo: StopsRepo[Id] = new StopsRepo[Id] {
    override def find(x: Int, y: Int): Id[StopId] = StopId(3)
  }

  implicit val idTimetableRepo: TimetableRepo[Id] = new TimetableRepo[Id] {
    override def findStopTimetable(stopId: StopId): Id[List[TimetableSlot]] = List(
      TimetableSlot(LineId(0), LocalTime.parse("10:07:00")),
      TimetableSlot(LineId(1), LocalTime.parse("10:11:00")),
      TimetableSlot(LineId(2), LocalTime.parse("10:15:00")),
    )
  }

  implicit val idLinesRepo: LinesRepo[Id] = new LinesRepo[Id] {
    override def find(id: LineId): Id[LineName] = id.value match {
      case 1 => LineName("200")
      case 2 => LineName("S75")
      case _ => LineName("M4")
    }
  }

  implicit val idDelaysRepo: DelaysRepo[Id] = new DelaysRepo[Id] {
    override def find(lineName: LineName): Id[LineDelay] = lineName.value match{
      case "M4" => LineDelay(1)
      case "200" => LineDelay(2)
      case "S75" => LineDelay(10)
      case _ => LineDelay(12)
    }
  }

}
