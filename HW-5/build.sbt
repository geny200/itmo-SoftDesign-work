name := "HW-5"

version := "0.1"

scalaVersion := "2.13.6"

val tethysVersion = "0.25.0"
libraryDependencies ++= Seq(
  "com.tethys-json" %% "tethys-core"       % tethysVersion,
  "com.tethys-json" %% "tethys-jackson"    % tethysVersion,
  "com.tethys-json" %% "tethys-derivation" % tethysVersion,
  "com.tethys-json" %% "tethys"            % tethysVersion,
  "org.typelevel"   %% "cats-effect"       % "3.2.9",
  "org.scalafx"     %% "scalafx"           % "16.0.0-R25"
)

scalacOptions ++= Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-feature")

// Fork a new JVM for 'run' and 'test:run', to avoid JavaFX double initialization problems
fork := true

libraryDependencies ++= {
  // Determine OS version of JavaFX binaries
  lazy val osName = System.getProperty("os.name") match {
    case n if n.startsWith("Linux")   => "linux"
    case n if n.startsWith("Mac")     => "mac"
    case n if n.startsWith("Windows") => "win"
    case _                            => throw new Exception("Unknown platform!")
  }
  Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
    .map(m => "org.openjfx" % s"javafx-$m" % "16" classifier osName)
}
