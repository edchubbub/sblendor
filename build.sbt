val AkkaVersion = "2.6.10"
val AkkaManagementVersion = "1.0.9"

val akka = Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-cluster-sharding-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-persistence-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-serialization-jackson" % AkkaVersion
)

val akkaManagement = Seq(
  "com.lightbend.akka.management" %% "akka-management" % AkkaManagementVersion,
  "com.lightbend.akka.management" %% "akka-management-cluster-http" % AkkaManagementVersion
)

val logging = Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)

libraryDependencies ++= akka ++ akkaManagement ++ logging