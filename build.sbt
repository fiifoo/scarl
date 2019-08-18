name := """scarl-app"""

version := "0.1"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .dependsOn(scarl)
  .aggregate(scarl)

lazy val scarl = project

scalaVersion := "2.13.0"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test
libraryDependencies += "com.typesafe.play" %% "play-slick" % "4.0.2"
libraryDependencies += "com.typesafe.play" %% "play-slick-evolutions" % "4.0.2"
libraryDependencies += "org.postgresql" % "postgresql" % "42.2.6"

unmanagedResourceDirectories in Assets += (baseDirectory.value / "scarl-ui" / "dist")
unmanagedResourceDirectories in Assets += (baseDirectory.value / "scarl-admin" / "dist")

mappings in Universal ++= (baseDirectory.value / "data" * "*").get map (x => x -> ("data/" + x.getName))

scalacOptions := Seq("-unchecked", "-deprecation")
