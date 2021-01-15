package com.sblendor.persistence

object Gem {

  sealed trait Token extends Product with Serializable

  sealed trait Gem extends Token

  case object Diamond extends Gem

  case object Emerald extends Gem

  case object Sapphire extends Gem

  case object Opal extends Gem

  case object Ruby extends Gem

  case object Gold extends Token

}
