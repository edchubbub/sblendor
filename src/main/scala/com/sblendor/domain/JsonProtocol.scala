package com.sblendor.domain

import com.sblendor.persistence.Gem
import com.sblendor.persistence.Gem.Gem
import spray.json.{DefaultJsonProtocol, DeserializationException, JsArray, JsString, JsValue, RootJsonFormat}

object JsonProtocol extends DefaultJsonProtocol {

  implicit val getGemsRequestFormat: RootJsonFormat[List[Gem]] = new RootJsonFormat[List[Gem.Gem]] {
    def write(list: List[Gem]): JsValue = {
      JsArray(list.map {
        case Gem.Diamond  => JsString("diamond")
        case Gem.Emerald  => JsString("emerald")
        case Gem.Sapphire => JsString("sapphire")
        case Gem.Opal     => JsString("opal")
        case Gem.Ruby     => JsString("ruby")
      }.toVector)
    }

    def read(json: JsValue): List[Gem] = {
      json.asJsObject.getFields().map {
        case JsString(name) =>
          name.toLowerCase match {
            case "diamond"  => Gem.Diamond
            case "emerald"  => Gem.Emerald
            case "sapphire" => Gem.Sapphire
            case "opal"     => Gem.Opal
            case "ruby"     => Gem.Ruby
            case _          => throw DeserializationException("Invalid Gem.")
          }
        case _ => throw DeserializationException("JSON Gem parsing failed.")
      }.toList
    }

  }

}
