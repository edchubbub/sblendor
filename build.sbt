val AkkaVersion = "2.6.10"
val AkkaHttpVersion = "10.1.11"
val AkkaManagementVersion = "1.0.9"
val AkkaPersistenceCassandraVersion = "1.0.4"
val LogbackVersion = "1.2.3"

lazy val buildSettings = Seq(
  organization := "com.sblendor",
  scalaVersion := "2.13.1"
)

val akkaDependencies = Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-cluster-sharding-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-persistence-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-persistence-query" % AkkaVersion,
  "com.typesafe.akka" %% "akka-serialization-jackson" % AkkaVersion,
  "com.typesafe.akka" %% "akka-cluster-tools" % AkkaVersion,
  "com.lightbend.akka" %% "akka-projection-eventsourced" % "1.0.0"
)

val akkaHttpDependencies = Seq(
  "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion
)

val akkaManagementDependencies = Seq(
  "com.lightbend.akka.management" %% "akka-management" % AkkaManagementVersion,
  "com.lightbend.akka.management" %% "akka-management-cluster-http" % AkkaManagementVersion
)

val akkaPersistenceCassandraDependencies = Seq(
  "com.typesafe.akka" %% "akka-persistence-cassandra" % AkkaPersistenceCassandraVersion
)

val logDependencies = Seq(
  "ch.qos.logback" % "logback-classic" % LogbackVersion
)

lazy val sblendor = project
  .in(file("."))
  .settings(
    mainClass in (Compile, run) := Some("com.sblendor.Main"),
    libraryDependencies ++= akkaDependencies ++ akkaHttpDependencies ++ akkaManagementDependencies ++ akkaPersistenceCassandraDependencies ++ logDependencies
  )
