package com.sblendor

import akka.actor.typed.ActorSystem
import akka.cluster.typed._
import akka.management.scaladsl.AkkaManagement
import com.typesafe.config.ConfigFactory

object Main {

	// 1. Ready the ports
	// 2. Create Guardian Behaviour
	// 3. Create a Typed Actor System
	// 4. Create a Cluster

	def main(args: Array[String]): Unit = {
		println("Hello Sblendor")

		val ports = if (args.isEmpty) Seq(25251, 25252, 0)
			else args.toSeq.map(_.toInt)

		ports.foreach(startup)
	}

	def startup(port: Int): Unit = {
		val key = s"akka.remote.artery.canonical.port=$port"
		val config = ConfigFactory.parseString(key).withFallback(ConfigFactory.load())
		val system = ActorSystem[Nothing](Guardian(), "ClusterSystem", config)
		val cluster = Cluster(system)

		println(s"starting up... $port")

//		cluster.manager ! Join(cluster.selfMember.address)

		//TODO: Check how to manage akka cluster via http for 2 nodes
		AkkaManagement(system).start()
	}

}