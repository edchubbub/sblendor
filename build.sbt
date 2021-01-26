name := "sblendor"
version := "1.0"

organization := "com.sblendor"
scalaVersion := "2.13.1"

val AkkaVersion = "2.6.9"
val AkkaHttpVersion = "10.2.0"
val AkkaManagementVersion = "1.0.9"
val AkkaPersistenceCassandraVersion = "1.0.1"
val LogbackVersion = "1.2.3"
val AlpakkaVersion = "2.0.2"

lazy val sblendor = project
  .in(file("."))
  .settings(
    mainClass in (Compile, run) := Some("com.sblendor.Main"),
    libraryDependencies ++= Seq(
      // 1. Basic dependencies for a clustered application
      "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
      "com.typesafe.akka" %% "akka-cluster-typed" % AkkaVersion,
      "com.typesafe.akka" %% "akka-cluster-sharding-typed" % AkkaVersion,

      // Akka HTTP
      "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion,

      // Common dependencies for logging
      "com.typesafe.akka" %% "akka-slf4j" % AkkaVersion,
      "ch.qos.logback" % "logback-classic" % LogbackVersion,
      "com.typesafe.akka" %% "akka-discovery" % AkkaVersion,

      // 3. Using Akka Persistence
      "com.typesafe.akka" %% "akka-persistence-typed" % AkkaVersion,
      "com.typesafe.akka" %% "akka-serialization-jackson" % AkkaVersion,
      "com.typesafe.akka" %% "akka-persistence-cassandra" % AkkaPersistenceCassandraVersion,
      "com.typesafe.akka" %% "akka-persistence-testkit" % AkkaVersion % Test,
    )
  )
