package mobimeo.interfaces

import java.nio.file.Paths
import java.time.LocalTime
import java.util.concurrent.Executors

import cats.Applicative
import cats.effect.{ContextShift, IO, Resource}
import cats.implicits._
import fs2.{Stream, io, text}
import mobimeo.repos.DelaysRepo.LineDelay
import mobimeo.repos.LinesRepo.{LineId, LineName}
import mobimeo.repos.StopsRepo.StopId
import mobimeo.repos.TimetableRepo.TimetableSlot
import mobimeo.repos.{DelaysRepo, LinesRepo, StopsRepo, TimetableRepo}

import scala.concurrent.ExecutionContext
import scala.util.Try

object IOImplicits {

  private val blockingExecutionContext =
    Resource.make(IO(ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(2))))(ec => IO(ec.shutdown()))

  private val toInt: String => Option[Int] = s => Try(s.toInt).toOption

  private val toLocalTime: String => Option[LocalTime] = s => Try(LocalTime.parse(s)).toOption

  private def streamFile(path: String)(implicit contextShift: ContextShift[IO]): Stream[IO, List[String]] =
    Stream.resource(blockingExecutionContext).flatMap { ec =>
      io.file.readAll[IO](Paths.get(path), ec, 4096)
        .through(text.utf8Decode)
        .through(text.lines)
        .filter(s => !s.trim.isEmpty && !s.startsWith("//"))
        .map(line => line.split(",").toList)
    }

  implicit def ioStopsRepo(implicit contextShift: ContextShift[IO]): StopsRepo[IO] = new StopsRepo[IO] {
    override def find(x: Int, y: Int): IO[StopId] =
      streamFile("data/stops.csv")
        .map {
          case id :: x :: y :: _ => Applicative[Option].map3(toInt(id), toInt(x), toInt(y)) {
            case (a, b, c) => StopsRepo.Record(StopId(a), b, c)
          }
          case _ => None
        }
        .unNone
        .find(record => record.x == x && record.y == y)
        .map(_.id).compile.toList.flatMap(_.headOption match {
        case Some(id) => IO.pure(id)
        case None => IO.raiseError(new Exception(s"Couldn't find stop with coordinates x: $x and y: $y"))
      })
  }


  implicit def ioTimetableRepo(implicit contextShift: ContextShift[IO]): TimetableRepo[IO] = new TimetableRepo[IO] {
    override def findStopTimetable(stopId: StopId): IO[List[TimetableSlot]] =
      streamFile("data/times.csv")
        .map {
          case lineId :: stopId :: time :: _ =>
            Applicative[Option].map3(toInt(lineId), toInt(stopId), toLocalTime(time)) {
              case (a, b, c) => TimetableRepo.Record(LineId(a), StopId(b), c)
            }
          case _ => None
        }
        .unNone
        .filter(record => record.stopId == stopId)
        .map(record => TimetableSlot(record.lineId, record.time))
        .compile.toList
  }

  implicit def ioLinesRepo(implicit contextShift: ContextShift[IO]): LinesRepo[IO] = new LinesRepo[IO] {
    override def find(id: LineId): IO[LineName] =
      streamFile("data/lines.csv")
      .map {
        case id :: name :: _ => Applicative[Option].map(toInt(id)){
          case a => LinesRepo.Record(LineId(a), LineName(name))
        }
        case _ => None
      }.unNone
      .find(record => record.lineId == id)
      .map(_.lineName)
      .compile.toList.map(_.headOption).flatMap{
        case Some(n) => IO.pure(n)
        case None => IO.raiseError(new Exception(s"Couldn't find line with id: ${id.value}"))
      }
  }

  implicit def ioDelaysRepo(implicit contextShift: ContextShift[IO]): DelaysRepo[IO] = new DelaysRepo[IO] {
    override def find(lineName: LineName): IO[LineDelay] =
      streamFile("data/delays.csv")
      .map{
        case name :: delay :: _ => Applicative[Option].map(toInt(delay)){
          case d => DelaysRepo.Record(LineName(name), LineDelay(d))
        }
        case _ => None
      }.unNone
      .find(record => record.lineName == lineName)
      .map(_.lineDelay)
      .compile.toList.map(_.headOption).flatMap{
        case Some(d) => IO.pure(d)
        case None => IO.raiseError(new Exception(s"Couldn't find line delay with line name: ${lineName.value}"))
      }
  }

}
