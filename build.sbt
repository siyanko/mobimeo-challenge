name := "mobimeo-challenge"

version := "0.1"

scalaVersion := "2.12.8"

val http4sVersion = "0.20.0-RC1"

resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-effect" % "1.2.0",

  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,

  "org.scalatest" % "scalatest_2.12" % "3.0.5" % "test"
)

scalacOptions ++= Seq("-Ypartial-unification")