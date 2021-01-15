package com.sblendor.http

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.sblendor.persistence.Gem
import com.sblendor.persistence.Gem.Gem
import spray.json.{DefaultJsonProtocol, DeserializationException, JsArray, JsString, JsValue, RootJsonFormat}

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  final case class GetGemsRequest(gems: List[Gem])

  implicit val getGemsRequestFormat: RootJsonFormat[GetGemsRequest] = new RootJsonFormat[GetGemsRequest] {
    def write(request: GetGemsRequest): JsValue = {
      JsArray(request.gems.map {
        case Gem.Diamond  => JsString("diamond")
        case Gem.Emerald  => JsString("emerald")
        case Gem.Sapphire => JsString("sapphire")
        case Gem.Opal     => JsString("opal")
        case Gem.Ruby     => JsString("ruby")
      }.toVector)
    }

    def read(json: JsValue): GetGemsRequest = {
      val gems = json.asJsObject.getFields("gems") match {
        case Seq(JsArray(elements)) =>
          elements.map {
            case JsString(name) =>
              name.toLowerCase match {
                case "diamond"  => Gem.Diamond
                case "emerald"  => Gem.Emerald
                case "sapphire" => Gem.Sapphire
                case "opal"     => Gem.Opal
                case "ruby"     => Gem.Ruby
                case _          => throw DeserializationException("Invalid Gem.")
              }
            case e => throw DeserializationException(s"JSON Gem parsing failed. $e")
          }
        case _ => throw DeserializationException(s"JSON Gem parsing failed.")
      }
      GetGemsRequest(gems.toList)
    }
  }
}

