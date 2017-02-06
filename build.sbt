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
  "com.typesafe.akka" %% "akka-actor" % "2.4.16",
  "com.typesafe.akka" %% "akka-persistence" % "2.4.16",
  "com.typesafe.akka" %% "akka-persistence-query-experimental" % "2.4.16",
  "com.typesafe.akka" %% "akka-cluster" % "2.4.16",
  "com.typesafe.akka" %% "akka-cluster-sharding" % "2.4.16",
//  "com.typesafe.akka" %% "akka-cluster-tools" % "2.4.16",
//  "com.typesafe.akka" %% "akka-cluster-metrics" % "2.4.16",
  "com.typesafe.akka" %% "akka-distributed-data-experimental" % "2.4.16",
  "com.typesafe.akka" %% "akka-slf4j" % "2.4.16",
  "com.typesafe.akka" %% "akka-http" % "10.0.1",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.1",
  "org.iq80.leveldb" % "leveldb" % "0.7",
  "org.fusesource.leveldbjni" % "leveldbjni-all" % "1.8",
  "com.typesafe.slick" %% "slick" % "3.1.1",
  "com.typesafe.slick" %% "slick-codegen" % "3.1.1",
  "com.h2database" % "h2" % "1.4.191",
  "com.trueaccord.scalapb" %% "compilerplugin" % "0.5.47",
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
  "org.flywaydb" % "flyway-core" % "3.2.1"
)

lazy val root = Project(
  id = "komang",
  base = file("."),
  settings =
    Defaults.coreDefaultSettings ++
      commonSettings ++
      //PB.protobufSettings ++
      Seq(
        libraryDependencies ++= libDeps,
        unmanagedSourceDirectories in Compile += baseDirectory.value / "src/main/scala-gen"
      )
)

scalafmtConfig in ThisBuild := Some(file(".scalafmt"))

// code generation task
lazy val slickGenerate = taskKey[Seq[File]]("slick code generation from an existing database")

slickGenerate := {
  import java.io.File
  val dbName = "komang"
  val url = s"jdbc:h2:mem:$dbName"
  val jdbcDriver = "org.h2.Driver"
  val slickDriver = "slick.driver.H2Driver"
  val resultRelativeDir = "src/main/scala-gen" // directory to create output scala slick definitions at
  val targetPackageName = "org.zalando.komang.persistence" // package name to give it
  val resultFilePath = s"$resultRelativeDir/$targetPackageName/Tables.scala" // override the name if you like
  val backupFilePath = s"$resultRelativeDir/$targetPackageName/Tables.scala.auto-backup" // override the name if you like
  val format = scala.Console.BLUE + scala.Console.BOLD
  println(format + s"Backing up existing slick mappings source to: file://${baseDirectory.value}/$backupFilePath")
  println(format + s"About to auto-generate slick mappings source from database schema at $url...")
  //sbt.IO.copyFile(new File(resultFilePath), new File(backupFilePath))
//  (runner in Compile).value.run("org.zalando.komang.tools.KomangSlickCodeGenerator",
//                                (dependencyClasspath in Compile).value.files,
//                                Seq.empty,
//                                streams.value.log)
  (runMain in Compile).toTask(" org.zalando.komang.tools.KomangSlickCodeGenerator").value
  println(format + s"Result: file://${baseDirectory.value}/$resultFilePath" + scala.Console.RESET)
//  val diff = (s"diff -u $resultFilePath $backupFilePath" #| "colordiff").!!
//  println(scala.Console.BLUE + s"Changes compared to previous output file, follow, if any.\n\n $diff")
  Seq(file(resultFilePath))
}
