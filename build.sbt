name := """epuap"""
organization := "pl.systherminfo"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

libraryDependencies += filters
libraryDependencies += ws
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
libraryDependencies += "org.apache.ws.security" % "wss4j" % "1.6.19"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "pl.systherminfo.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "pl.systherminfo.binders._"
