package com.sblendor.http

import akka.actor.typed.ActorSystem
import akka.cluster.sharding.typed.scaladsl.ClusterSharding
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.sblendor.clustersharding.BoardGame
import com.sblendor.persistence.Gem.Gem
import com.sblendor.persistence.BoardGameCommands
import com.sblendor.persistence.BoardGameCommands.Confirmation

import scala.concurrent.Future
import scala.concurrent.duration._


/**
 * HTTP API for
 * 1. Receiving data from remote weather stations
 * 2. Receiving and responding to queries
 */
final class BoardGameRoutes[T](system: ActorSystem[T]) extends JsonSupport {

  private val sharding = ClusterSharding(system)

  private implicit val timeout: Timeout = system.settings.config.getDuration("sblendor.routes.ask-timeout").toMillis.millis

  private def getGems(playerId: String, gems: List[Gem]): Future[Confirmation] = {
    val ref = sharding.entityRefFor(BoardGame.EntityKey, playerId)
    ref.ask(BoardGameCommands.GetGems(gems, _))
  }

  val boardGame: Route =
    path("gems") {
      concat(
        get {
          complete("hello world!")
        }
      )
    } ~ path("gems" / Segment) { playerId =>
      concat(
        post {
          entity(as[GetGemsRequest]) { request =>
            onSuccess(getGems(playerId, request.gems)) { performed =>
              complete(StatusCodes.Accepted -> s"Gems: $performed")
            }
          }
        }
      )
    }
}

