package mobimeo.domain

import java.time.LocalTime

import cats.Id
import org.scalatest.{Matchers, OptionValues, WordSpec}

class VehicleFinderSpec extends WordSpec with Matchers with OptionValues {
  "VehicleFinder" should {
    "find a vehicle" in {
      val time = LocalTime.parse("10:06:00")

      VehicleFinder.find[Id](time, 1, 7).value.value should be ("M4")
    }
  }

}
