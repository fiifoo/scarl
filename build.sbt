name := """scarl-app"""

version := "0.1"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .dependsOn(scarl)
  .aggregate(scarl)

lazy val scarl = project

scalaVersion := "2.12.6"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
libraryDependencies += "com.typesafe.play" %% "play-slick" % "3.0.3"
libraryDependencies += "com.typesafe.play" %% "play-slick-evolutions" % "3.0.3"
libraryDependencies += "org.postgresql" % "postgresql" % "42.2.4"

unmanagedResourceDirectories in Assets += (baseDirectory.value / "scarl-ui" / "dist")
unmanagedResourceDirectories in Assets += (baseDirectory.value / "scarl-admin" / "dist")

mappings in Universal ++= (baseDirectory.value / "data" * "*").get map (x => x -> ("data/" + x.getName))
