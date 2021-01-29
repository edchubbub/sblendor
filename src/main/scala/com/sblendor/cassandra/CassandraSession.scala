package com.sblendor.cassandra

import akka.actor.typed.ActorSystem
import akka.stream.alpakka.cassandra.CassandraSessionSettings
import akka.stream.alpakka.cassandra.scaladsl.{CassandraSession, CassandraSessionRegistry}

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

object CassandraSession {

  def init(actorSystem: ActorSystem[_], sessionSettings: CassandraSessionSettings = CassandraSessionSettings()): Unit = {
    implicit val cassandraSession: CassandraSession =
      CassandraSessionRegistry.get(actorSystem).sessionFor("alpakka.cassandra")

//    // TODO use real replication strategy in real application
//    val keyspaceStmt =
//      """
//      CREATE KEYSPACE IF NOT EXISTS akka
//      WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 }
//      """
//
//    val offsetTableStmt =
//      """
//      CREATE TABLE IF NOT EXISTS akka_snapshot.offset_store (
//        projection_name text,
//        partition int,
//        projection_key text,
//        offset text,
//        manifest text,
//        last_updated timestamp,
//        PRIMARY KEY ((projection_name, partition), projection_key)
//      )
//        """
//    // ok to block here, main thread
//    Await.ready(cassandraSession.executeDDL(keyspaceStmt), 30.seconds)
//    actorSystem.log.info("Created akka keyspace")
//    Await.ready(cassandraSession.executeDDL(offsetTableStmt), 30.seconds)
//    actorSystem.log.info("Created akka_snapshot.offset_store table")
  }

}
