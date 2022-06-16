import Dependencies._

ThisBuild / organization := "com.fayimora"
ThisBuild / scalaVersion := "3.1.2"

ThisBuild / scalacOptions ++=
  Seq(
    "-deprecation",
    "-explain",
    "-feature",
    "-language:implicitConversions",
    "-unchecked",
    "-Xfatal-warnings",
    "-Yexplicit-nulls", // experimental (I've seen it cause issues with circe)
    "-Ykind-projector",
    "-Ysafe-init", // experimental (I've seen it cause issues with circe)
  ) ++ Seq("-rewrite", "-indent") ++ Seq("-source", "future-migration")

lazy val `learning-zio` =
  project
    .in(file("."))
    .settings(name := "learning-zio")
    .settings(commonSettings)
    .settings(dependencies)

lazy val commonSettings = commonScalacOptions ++ Seq(
  update / evictionWarningOptions := EvictionWarningOptions.empty
)

lazy val commonScalacOptions = Seq(
  Compile / console / scalacOptions --= Seq(
    "-Wunused:_",
    "-Xfatal-warnings",
  ),
  Test / console / scalacOptions :=
    (Compile / console / scalacOptions).value,
)

lazy val dependencies = Seq(
  libraryDependencies ++= Seq(
    // main dependencies
    "dev.zio" %% "zio" % "2.0.0-RC6",
    "dev.zio" %% "zio-test" % "2.0.0-RC6",
    "dev.zio" %% "zio-json" % "0.3.0-RC8",
    "io.d11" %% "zhttp" % "2.0.0-RC9",
    "com.lihaoyi" %% "requests" % "0.7.0",
    "com.lihaoyi" %% "pprint" % "0.7.0",
  ),
  libraryDependencies ++= Seq(
    org.scalatest.scalatest,
    org.scalatestplus.`scalacheck-1-16`,
  ).map(_ % Test),
)
