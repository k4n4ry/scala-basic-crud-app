name := """bluebacks"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.0"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test
libraryDependencies += "com.h2database" % "h2" % "1.4.197"
libraryDependencies += "com.typesafe.play" %% "play-slick" % "4.0.2"
libraryDependencies += "com.typesafe.play" %% "play-slick-evolutions" % "4.0.2"
