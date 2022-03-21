ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "itmo-software-design-2-hw"
  )
  .settings(commonSettings)

val tethysVersion = "0.26.0"
val tapirVersion  = "1.0.0-M2"

lazy val commonSettings = Seq(
  libraryDependencies ++= Seq(
    "org.scalamock"               %% "scalamock"                % "5.2.0"  % Test,
    "org.scalatest"               %% "scalatest"                % "3.2.11" % Test,
    "org.typelevel"               %% "cats-core"                % "2.7.0",
    "com.tethys-json"             %% "tethys-core"              % tethysVersion,
    "com.tethys-json"             %% "tethys-jackson"           % tethysVersion,
    "com.tethys-json"             %% "tethys-derivation"        % tethysVersion,
    "com.tethys-json"             %% "tethys"                   % tethysVersion,
    "com.tethys-json"             %% "tethys-enumeratum"        % tethysVersion,
    "com.tethys-json"             %% "tethys-circe"             % tethysVersion,
    "org.scala-lang.modules"      %% "scala-collection-contrib" % "0.2.2",
    "com.softwaremill.sttp.tapir" %% "tapir-zio-http4s-server"  % tapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs"       % tapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % tapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui"         % tapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle"  % tapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-core"               % tapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-json-tethys"        % tapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-zio"                % tapirVersion,
    "org.http4s"                  %% "http4s-blaze-server"      % "0.23.10"
  ),
  scalaVersion := "2.13.8",
  addCompilerPlugin("org.typelevel" % "kind-projector" % "0.13.2" cross CrossVersion.full)
)
