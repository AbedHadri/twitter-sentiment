name := "twitter-stream-process"

version := "1.0"

scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .enablePlugins(JavaServerAppPackaging, DockerPlugin)
  .settings(
    name := "twitter-stream-process",
    Defaults.itSettings,
    inConfig(IntegrationTest)(
      ScalafmtPlugin.scalafmtConfigSettings ++
        scalafixConfigSettings(IntegrationTest)
    ),
    scalacOptions := Seq(
      sys.env.get("CI").fold("")(_ => "-Werror"),
      "-unchecked",
      "-feature",
      "-deprecation",
      "-Yrangepos",
      "-Wunused",
      "-Xcheckinit",
      "-Xlint",
      "-Wextra-implicit",
      "-Wdead-code",
      "-Wnumeric-widen",
      "-Wvalue-discard",
      "-encoding",
      "utf8",
      "-Wconf:msg=While parsing annotations in:silent"
    ),
    Compile / run / mainClass := Some("com.abedhadri.Main"),
    scalafmtOnCompile := true,
    libraryDependencies := Dependencies.seq
  )
