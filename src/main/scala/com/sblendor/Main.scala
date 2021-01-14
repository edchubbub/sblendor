package com.sblendor

import akka.actor.AddressFromURIString
import akka.actor.typed.ActorSystem
import akka.cluster.typed._
import akka.management.scaladsl.AkkaManagement
import com.sblendor.clustersharding.Guardian
import com.typesafe.config.{Config, ConfigFactory}

import scala.collection.JavaConverters._

object Main {

	// 1. Ready the ports
	// 2. Create Guardian Behaviour
	// 3. Create a Typed Actor System
	// 4. Create a Cluster

	def main(args: Array[String]): Unit = {
		println("Hello Sblendor")

		val seedNodePorts = ConfigFactory.load().getStringList("akka.cluster.seed-nodes")
			.asScala
			.flatMap { case AddressFromURIString(s) => s.port }

		val ports = args.headOption match {
			case Some(port) => Seq(port.toInt)
			case None 			=> seedNodePorts ++ Seq(0)
		}

		ports foreach { port =>
			val httpPort =
				if (port > 0) 10000 + port
				else 0

			val config = configWithPort(port)
			ActorSystem[Nothing](Guardian(httpPort), "Sblendor", config)
			//AkkaManagement(system).start
		}
	}

	def configWithPort(port: Int): Config = {
		ConfigFactory.parseString(
			s"""
				 |akka.remote.artery.canonical.port=$port
				 |""".stripMargin).withFallback(ConfigFactory.load())
	}

}