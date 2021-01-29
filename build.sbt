name := "sblendor"
version := "1.0"

organization := "com.sblendor"
scalaVersion := "2.13.3"

val AkkaVersion = "2.6.10"
val AkkaHttpVersion = "10.2.1"
val AkkaManagementVersion = "1.0.9"
val AkkaPersistenceCassandraVersion = "1.0.3"
val LogbackVersion = "1.2.3"
val AkkaProjectionVersion = "1.0.0"

lazy val sblendor = project
  .in(file("."))
  .settings(
    mainClass in (Compile, run) := Some("com.sblendor.Main"),
    scalacOptions in Compile ++= Seq("-deprecation", "-feature", "-unchecked", "-Xlog-reflective-calls", "-Xlint"),
    javacOptions in Compile ++= Seq("-Xlint:unchecked", "-Xlint:deprecation"),
    libraryDependencies ++= Seq(
      // clustered application
      "com.typesafe.akka" %% "akka-actor-typed"            % AkkaVersion,
      "com.typesafe.akka" %% "akka-stream"                 % AkkaVersion,
      "com.typesafe.akka" %% "akka-cluster-typed"          % AkkaVersion,
      "com.typesafe.akka" %% "akka-cluster-sharding-typed" % AkkaVersion,

      // Akka HTTP
      "com.typesafe.akka" %% "akka-http"            % AkkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion,

      // loggin
      "com.typesafe.akka" %% "akka-slf4j"      % AkkaVersion,
      "ch.qos.logback"    %  "logback-classic" % LogbackVersion,
      "com.typesafe.akka" %% "akka-discovery"  % AkkaVersion,

      // persistence
      "com.typesafe.akka"  %% "akka-distributed-data"               % AkkaVersion,
      "com.typesafe.akka"  %% "akka-persistence-typed"              % AkkaVersion,
      "com.typesafe.akka"  %% "akka-serialization-jackson"          % AkkaVersion,
      "com.typesafe.akka"  %% "akka-persistence-query"              % AkkaVersion,
      "com.typesafe.akka"  %% "akka-persistence-cassandra"          % AkkaPersistenceCassandraVersion,
      "com.typesafe.akka"  %% "akka-persistence-cassandra-launcher" % AkkaPersistenceCassandraVersion,
      "com.typesafe.akka"  %% "akka-persistence-testkit"            % AkkaVersion % Test,
      "com.lightbend.akka" %% "akka-projection-eventsourced"        % AkkaProjectionVersion,
      "com.lightbend.akka" %% "akka-projection-cassandra"           % AkkaProjectionVersion,

      // hack
      "com.datastax.oss"     % "java-driver-core"     % "4.6.1" force(),
      "com.esri.geometry"    % "esri-geometry-api"    % "2.2.4",
      "com.github.spotbugs"  % "spotbugs-annotations" % "4.2.0",
      "org.apache.tinkerpop" % "gremlin-core"         % "3.4.10",
      "org.apache.tinkerpop" % "tinkergraph-gremlin"  % "3.4.10",
    ),
  )
