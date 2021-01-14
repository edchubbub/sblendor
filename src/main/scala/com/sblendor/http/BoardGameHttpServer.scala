package com.sblendor.http

import akka.Done
import akka.actor.CoordinatedShutdown
import akka.actor.typed.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route

import scala.concurrent.duration._
import scala.util.{Failure, Success}
import akka.{actor => classic}

object BoardGameHttpServer {

  def start(routes: Route, port: Int, system: ActorSystem[_]): Unit = {

    // NOTE: why do we need to convert typed system back to classic?
    implicit val classicSystem: classic.ActorSystem = system.classicSystem
    val shutdown = CoordinatedShutdown(classicSystem)

    import system.executionContext

    Http().bindAndHandle(routes, "localhost", port).onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        system.log.info("BoardGameServer online at http://{}:{}", address.getHostString, address.getPort)

        shutdown.addTask(
          CoordinatedShutdown.PhaseServiceRequestsDone,
          "http-graceful-terminate"
        ) { () =>
          binding.terminate(10.seconds).map { _ =>
            system.log.info("BoardGameServer http://{}:{}/ graceful shutdown completed", address.getHostString, address.getPort)
            Done
          }
        }

      case Failure(exception) =>
        system.log.error("Failed to bind HTTP endpoint, terminating system", exception)
        system.terminate()
    }

  }

}
