package com.sblendor.cassandra

import akka.actor.typed.ActorSystem
//import akka.stream.{ActorMaterializer, Materializer}
//import akka.stream.alpakka.cassandra.CassandraSessionSettings
//import akka.stream.alpakka.cassandra.scaladsl.CassandraSessionRegistry
//import akka.stream.alpakka.cassandra.scaladsl.CassandraSession
//import akka.stream.scaladsl.Sink

import scala.concurrent.Future

object CassandraSession {

//  def init(actorSystem: ActorSystem[_], sessionSettings: CassandraSessionSettings = CassandraSessionSettings()): Unit = {
//    implicit val cassandraSession: CassandraSession =
//      CassandraSessionRegistry.get(actorSystem).sessionFor(sessionSettings)

//    val version: Future[String] = cassandraSession
//      .select("SELECT release_version FROM system.local;")
//      .map(_.getString("release_version"))
//      .runWith(Sink.head)(Materializer(actorSystem))
//    version
//  }

}
