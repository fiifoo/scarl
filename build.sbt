name := """scarl-app"""

version := "0.1"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .dependsOn(scarl)

lazy val scarl = project

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)



fork in run := true



unmanagedResourceDirectories in Assets += (baseDirectory.value / "scarl-ui" / "dist")
