name := """arignee"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
  "org.jsoup"%"jsoup"%"1.8.1",
  "org.json"%"json"%"20140107",
  "org.julienrf" %% "play-jsmessages" % "1.6.2",
  "org.apache.commons" % "commons-lang3" % "3.3.2"
)
