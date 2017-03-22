organization in ThisBuild := "org.zalando"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.11.8"

def common = Seq(
  javacOptions in compile += "-parameters"
)

lazy val `komang` = (project in file("."))
  .aggregate(`komang-api`, `komang-impl`)

lazy val `komang-api` = (project in file("komang-api"))
  .settings(common: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `komang-impl` = (project in file("komang-impl"))
  .enablePlugins(LagomScala)
  .settings(common: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslTestKit,
      "com.softwaremill.macwire" %% "macros" % "2.2.5" % "provided"
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`komang-api`)

scalafmtConfig in ThisBuild := Some(file(".scalafmt"))
