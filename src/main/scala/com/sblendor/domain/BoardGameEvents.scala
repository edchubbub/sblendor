package com.sblendor.domain

import com.sblendor.domain.Gem.Gem

object BoardGameEvents {

  /**
   * Events = will be stored
   */

  sealed trait Event extends CborSerializable

  final case class GemsObtained(playerId: String, quantity: List[Gem]) extends Event

}
