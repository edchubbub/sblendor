package com.sblendor.persistence

import com.sblendor.domain.CborSerializable
import com.sblendor.persistence.Gem.Gem

object BoardGameStates {

  /**
   * State = represents the current state of actor
   */

  sealed trait State extends CborSerializable
  final object ConcludedState extends State
  final case class BlankState(gems: Map[Gem, Int] = Map.empty) extends State
  final case class PlayingState(gems: Map[Gem, Int]) extends State {
    def updateGems(quantity: List[Gem]): State = {
      val states = quantity map { g =>
        val newQty: Int = gems(g) - 1
        copy(gems = gems.updated(g, newQty))
      }
      states.last
    }
  }

}