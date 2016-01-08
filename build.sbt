name := """workshop-registration"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

resolvers += "Atlassian Releases" at "https://maven.atlassian.com/public/"

libraryDependencies ++= Seq(
  "org.scalatestplus" % "play_2.11" % "1.4.0-M4",
  cache,
  ws,
  specs2 % Test,
  "com.typesafe.play" %% "play-slick" % "1.1.1",
  "com.typesafe.play" %% "play-slick-evolutions" % "1.1.1",
  "com.mohiva" %% "play-silhouette" % "3.0.4",
  "com.adrianhurt" %% "play-bootstrap3" % "0.4.5-P24",
  "com.mohiva" %% "play-silhouette-testkit" % "3.0.4" % "test",
  "net.codingwell" %% "scala-guice" % "4.0.0",
  "net.ceedubs" %% "ficus" % "1.1.2",
  "com.h2database" % "h2" % "1.4.177"
)


resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
