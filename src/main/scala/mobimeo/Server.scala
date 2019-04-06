package mobimeo

import java.time.LocalTime

import cats.effect._
import cats.implicits._
import mobimeo.domain.VehicleFinder
import mobimeo.interfaces.IOImplicits._
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server.blaze._

import scala.util.control.NonFatal

object Server extends IOApp {

  implicit val timeQueryParamDecoder: QueryParamDecoder[LocalTime] =
    QueryParamDecoder[String].map(LocalTime.parse)

  object TimeQueryParamMatcher extends QueryParamDecoderMatcher[LocalTime]("time")

  object XQueryParamMatcher extends QueryParamDecoderMatcher[Int]("x")

  object YQueryParamMatcher extends QueryParamDecoderMatcher[Int]("y")

  val service = HttpRoutes.of[IO] {
    case GET -> Root / "vehicle" :? TimeQueryParamMatcher(time)
      +& XQueryParamMatcher(x)
      +& YQueryParamMatcher(y) =>
      VehicleFinder.find[IO](time, x, y)
        .flatMap {
          case Nil => NotFound()
          case ls => Ok(ls.map(_.value).mkString(","))
        }.recoverWith {
        case NonFatal(th) =>
          IO(th.printStackTrace()) *> NotFound()
      }
  }.orNotFound

  override def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .bindHttp(8081, "localhost")
      .withHttpApp(service)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
}
