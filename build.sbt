name := """bitly-elasticsearch-indexer"""

version := "1.0"

scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.2",
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.1"
)

