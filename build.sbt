scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "org.elasticsearch" % "elasticsearch" % "1.5.2",
  "com.sksamuel.elastic4s" %% "elastic4s-core" % "1.6.0",
  "commons-io" % "commons-io" % "2.4",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)
