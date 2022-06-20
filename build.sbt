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
    // "-Yexplicit-nulls", // experimental (I've seen it cause issues with circe)
    "-Ykind-projector",
    // "-Ysafe-init", // experimental (I've seen it cause issues with circe)
  ) ++ Seq("-rewrite", "-indent") ++ Seq("-source", "future-migration")

lazy val `learning-zio` =
  project
    .in(file("."))
    .settings(name := "learning-zio")
    .settings(commonSettings)
    .settings(workaroundForTrapExit)
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

val ZIOVersion = "2.0.0-RC6"

lazy val dependencies = Seq(
  libraryDependencies ++= Seq(
    // main dependencies
    "dev.zio"     %% "zio"                 % ZIOVersion,
    "dev.zio"     %% "zio-json"            % "0.3.0-RC8",
    "com.lihaoyi" %% "requests"            % "0.7.0",
    "com.lihaoyi" %% "pprint"              % "0.7.0",
    "org.jsoup"    % "jsoup"               % "1.15.1",
    "dev.zio"     %% "zio-config"          % "3.0.0-RC9",
    "dev.zio"     %% "zio-config-typesafe" % "3.0.0-RC9",
    "dev.zio"     %% "zio-config-magnolia" % "3.0.0-RC9",
  ),
  libraryDependencies ++= Seq(
    org.scalatest.scalatest,
    org.scalatestplus.`scalacheck-1-16`,
    "dev.zio" %% "zio-test"          % ZIOVersion,
    "dev.zio" %% "zio-test-sbt"      % ZIOVersion,
    "dev.zio" %% "zio-test-magnolia" % ZIOVersion,
  ).map(_ % Test),
)

lazy val workaroundForTrapExit = Seq(
  Compile / run / fork           := true,
  Compile / run / connectInput   := true,
  Compile / run / outputStrategy := Some(StdoutOutput),
)
