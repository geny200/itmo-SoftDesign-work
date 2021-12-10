name := "itmo-software-design-hw-6-lab"

version := "0.1"

scalaVersion := "2.13.6"

val tethysVersion = "0.25.0"
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest"   % "3.2.10" % Test,
  "org.typelevel" %% "cats-effect" % "3.3.0",
  compilerPlugin("org.typelevel" % "kind-projector" % "0.13.2" cross CrossVersion.full)
)
