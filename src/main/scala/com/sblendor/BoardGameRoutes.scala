package com.sblendor

import akka.actor.typed.ActorSystem
import akka.cluster.sharding.typed.scaladsl.ClusterSharding
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.sblendor.BoardGame.Confirmation
import com.sblendor.domain.Gem
import com.sblendor.domain.Gem.Gem
import spray.json.{DefaultJsonProtocol, _}

import scala.concurrent.Future
import scala.concurrent.duration._

final case class GetGemsRequest(gems: List[Gem])

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  implicit val getGemsRequestFormatx: RootJsonFormat[GetGemsRequest] = new RootJsonFormat[GetGemsRequest] {
    def write(request: GetGemsRequest): JsValue = {
      JsArray(request.gems.map {
        case Gem.Diamond => JsString("diamond")
        case Gem.Emerald => JsString("emerald")
        case Gem.Sapphire => JsString("sapphire")
        case Gem.Opal => JsString("opal")
        case Gem.Ruby => JsString("ruby")
      }.toVector)
    }

    def read(json: JsValue): GetGemsRequest = {
      val xxx = json.asJsObject.getFields("gems").map {
        case JsString(name) =>
          name.toLowerCase match {
            case "diamond" => Gem.Diamond
            case "emerald" => Gem.Emerald
            case "sapphire" => Gem.Sapphire
            case "opal" => Gem.Opal
            case "ruby" => Gem.Ruby
            case _ => throw DeserializationException("Invalid Gem.")
          }
        case _ => throw DeserializationException("JSON Gem parsing failed.")
      }
      GetGemsRequest(xxx.toList)
    }
  }
}


/**
 * HTTP API for
 * 1. Receiving data from remote weather stations
 * 2. Receiving and responding to queries
 */
final class BoardGameRoutes[T](system: ActorSystem[T]) extends JsonSupport {

  private val sharding = ClusterSharding(system)

  private implicit val timeout: Timeout = system.settings.config.getDuration("").toMillis.millis

  private def getGems(playerId: String, gems: List[Gem]): Future[Confirmation] = {
    val ref = sharding.entityRefFor(BoardGame.EntityKey, playerId)
    ref.ask(BoardGame.GetGems(gems, _))
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

