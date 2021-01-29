package com.sblendor.persistence

import com.sblendor.domain.CborSerializable
import Gem.Gem

object BoardGameEvents {

  /**
   * Events = a significat change in state, will be stored
   */

  sealed trait Event extends CborSerializable
  final case class GemsObtained(playerId: String, quantity: List[Gem]) extends Event
  final case object GameEnded extends Event
  final case class PlayerJoined(player: Player) extends Event

}
