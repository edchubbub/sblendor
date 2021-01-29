package com.sblendor.persistence

import com.sblendor.domain.CborSerializable
import com.sblendor.persistence.Gem.Gem

object BoardGameStates {

  /**
   * State = represents the current state of actor
   */

  sealed trait Description
  final case object Ready extends Description
  final case object Playing extends Description

  final case class State(players: Map[String, Player], gems: Map[Gem, Int], description: Description) extends CborSerializable {

    def updateGems(quantity: List[Gem]): State = {
      val states = quantity map { g =>
        val newQty: Int = gems(g) - 1
        copy(gems = gems.updated(g, newQty))
      }
      states.last
    }

    def isReady: Boolean = gems.isEmpty && (description == Playing)

    def updateDescription(description: Description): Description = description
  }
  object State {
    val ready: State = State(players = Map.empty, gems = Map.empty, description = Ready)
  }

}