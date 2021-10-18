name := "HW-2"

version := "0.1"

scalaVersion := "2.13.6"

libraryDependencies ++= Seq(
  "org.scalamock" %% "scalamock" % "5.1.0" % Test,
  "org.scalatest" %% "scalatest" % "3.2.10" % Test,
  "com.xebialabs.restito" % "restito" % "0.9.4" % Test,
  "org.json4s" %% "json4s-jackson" % "4.0.3"
)