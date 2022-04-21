import sbt._

object Dependencies {

  object Versions {
    val Akka = "2.6.19"
    val AkkaHttp = "10.2.9"
    val Cats = "2.3.0"
    val KOauth = "2.1.0"
    val ScalaCommon = "4.1.2"
    val ScalaLogging = "3.9.4"
    val PureConfig = "0.17.1"
    val TestcontainersScala = "0.40.3"
    val LogStashLogbackEncoder = "7.0.1"
    val Kamon = "2.5.0"
    val Logback = "1.2.11"
    val ScalaTest = "3.2.11"
  }

  val seq = Seq(
    "com.typesafe.akka" %% "akka-http" % Versions.AkkaHttp,
    "com.typesafe.akka" %% "akka-http-spray-json" % Versions.AkkaHttp,
    "com.typesafe.akka" %% "akka-slf4j" % Versions.Akka,
    "com.typesafe.akka" %% "akka-stream" % Versions.Akka,
    "org.typelevel" %% "cats-core" % Versions.Cats,
    "com.hunorkovacs" %% "koauth" % Versions.KOauth,
    "ch.qos.logback" % "logback-classic" % Versions.Logback,
    "org.scalatest" %% "scalatest" % Versions.ScalaTest % "it,test",
    "com.typesafe.akka" %% "akka-http-testkit" % Versions.AkkaHttp % "it,test",
    "com.typesafe.akka" %% "akka-stream-testkit" % Versions.Akka % "it,test",
    "com.github.pureconfig" %% "pureconfig" % Versions.PureConfig,
    "net.logstash.logback" % "logstash-logback-encoder" % Versions.LogStashLogbackEncoder,
    "com.typesafe.scala-logging" %% "scala-logging" % Versions.ScalaLogging,
    "io.kamon" %% "kamon-core" % Versions.Kamon,
    "io.kamon" %% "kamon-system-metrics" % Versions.Kamon
  )

}
