ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.8"

val tethysVersion = "0.26.0"
val tapirVersion  = "1.0.0-M2"

lazy val root = project in file(".") aggregate (
  store,
  turnstile,
  manager,
  http,
  server,
  report
)

lazy val store = (project in file("store"))
  .settings(name := "event-store")
  .settings(commonSettings ++ catsSettings ++ tethysSettings ++ tapirSettings)

lazy val turnstile = (project in file("turnstile"))
  .settings(name := "turnstile-service")
  .settings(commonSettings ++ catsSettings)
  .dependsOn(store, http)

lazy val http = (project in file("http"))
  .settings(name := "http-common")
  .settings(commonSettings ++ catsSettings ++ tapirSettings ++ tethysSettings)

lazy val server = (project in file("server"))
  .settings(name := "server")
  .settings(commonSettings ++ catsSettings ++ tapirSettings)
  .settings(
    addCompilerPlugin("org.typelevel" % "kind-projector" % "0.13.2" cross CrossVersion.full)
  )
  .settings(
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-blaze-server" % "0.23.10"
    )
  )
  .dependsOn(
    store,
    report,
    turnstile,
    manager,
    http
  )

lazy val manager = (project in file("manager"))
  .settings(name := "manager-service")
  .settings(commonSettings ++ catsSettings)
  .dependsOn(store, http)

lazy val report = (project in file("report"))
  .settings(name := "report-service")
  .settings(commonSettings ++ catsSettings)
  .dependsOn(store, http)

lazy val catsSettings = Seq(
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-core" % "2.7.0"
//    "org.typelevel" %% "cats-effect" % "2.5.4"
  )
)

lazy val tapirSettings = Seq(
  libraryDependencies ++= Seq(
    "com.softwaremill.sttp.tapir" %% "tapir-zio-http4s-server"  % tapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs"       % tapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % tapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui"         % tapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle"  % tapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-core"               % tapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-json-tethys"        % tapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-zio"                % tapirVersion
  )
)

lazy val tethysSettings = Seq(
  libraryDependencies ++= Seq(
    "com.tethys-json" %% "tethys-core"       % tethysVersion,
    "com.tethys-json" %% "tethys-derivation" % tethysVersion
  )
)

lazy val commonSettings = Seq(
  libraryDependencies ++= Seq(
    "org.scalamock" %% "scalamock" % "5.2.0"  % Test,
    "org.scalatest" %% "scalatest" % "3.2.11" % Test
  ),
  scalaVersion := "2.13.8"
)
