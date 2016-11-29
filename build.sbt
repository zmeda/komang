scalaVersion := "2.11.8"

lazy val commonSettings =
  Seq(
    organization := "org.zalando",
    scalaVersion := "2.11.8",
    scalacOptions ++= Seq(
      "-unchecked",
      "-deprecation",
      "-Xlint",
      "-Ywarn-dead-code",
      "-language:_",
      "-target:jvm-1.8",
      "-encoding",
      "UTF-8",
      "-Ywarn-unused-import"
    )
  ) ++ reformatOnCompileSettings

lazy val libDeps = Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.12",
  "com.typesafe.akka" %% "akka-persistence" % "2.4.12",
  "com.typesafe.akka" %% "akka-cluster-sharding" % "2.4.12",
  "com.typesafe.akka" %% "akka-http-experimental" % "2.4.11",
  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % "2.4.11",
  "org.iq80.leveldb" % "leveldb" % "0.7",
  "org.fusesource.leveldbjni" % "leveldbjni-all" % "1.8"
)

lazy val root = project.in(file(".")).settings(commonSettings: _*).settings(libraryDependencies ++= libDeps)

scalafmtConfig in ThisBuild := Some(file(".scalafmt"))
