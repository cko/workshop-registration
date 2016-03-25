import com.typesafe.sbt.packager.archetypes.ServerLoader.Systemd
import com.typesafe.sbt.SbtNativePackager.autoImport._
import DebianConstants._

name := """workshop-registration"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala, DebianPlugin)

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
  "com.h2database" % "h2" % "1.4.177",
  "org.xerial" % "sqlite-jdbc" % "3.8.11.2",
  "org.apache.pdfbox" % "pdfbox" % "2.0.0-RC3",
  "org.seleniumhq.selenium" % "selenium-java" % "2.48.2"
)


resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

// use different configuration for tests
javaOptions in Test += "-Dconfig.file=conf/application.test.conf"

// Debian package
enablePlugins(JavaAppPackaging)
enablePlugins(JavaServerAppPackaging)
serverLoading in Debian  := Systemd
maintainer in Linux      := "Foo Bar <foo.bar@example.com>"
packageSummary in Linux  := "Workshop Registration"
packageDescription       := "Workshop Registration"
daemonUser in Linux      := "wsreg"
daemonGroup in Linux     := "wsreg"
debianPackageDependencies in Debian ++= Seq("java8-runtime", "sqlite3")
import DebianConstants._
maintainerScripts in Debian := maintainerScriptsAppend((maintainerScripts in Debian).value)(
  Postinst ->  {
    // create database directory
    "mkdir -p /var/lib/workshop-registration\n" +
    "chown ${{daemon_user}}:${{daemon_group}} /var/lib/workshop-registration\n" +
    // generate aplication secret
    "echo play.crypto.secret=`head /dev/urandom | tr -dc A-Za-z0-9 | head -c 64` >> /etc/workshop-registration/application.prod.conf\n" +
    // restart
    "restartService ${{app_name}}\n"
  }
)
javaOptions in Universal += "-Dpidfile.path=/var/run/workshop-registration/play.pid"
javaOptions in Universal += "-Dconfig.file=conf/application.prod.conf"

