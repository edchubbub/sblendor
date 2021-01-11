package com.sblendor

import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import akka.cluster.sharding.typed.scaladsl.{ClusterSharding, Entity, EntityTypeKey}
import akka.persistence.typed.PersistenceId
import akka.persistence.typed.scaladsl.{Effect, EventSourcedBehavior}
import com.sblendor.Gem.Gem

object BoardGame {

  // State = represents the current state of actor

  final case class State(gems: Map[Gem, Int]) extends CborSerializable {
    def updateGems(quantity: List[Gem]): State = {
      val states = quantity map { g =>
        val newQty: Int = gems(g) - 1
        copy(gems = gems.updated(g, newQty))
      }
      states.last
    }

  }

  object State {
    val empty = State(Map.empty)
  }

  // Commands = incoming message

  sealed trait Command extends CborSerializable

  final case class GetGems(quantity: List[Gem], replyTo: ActorRef[Confirmation]) extends Command

  sealed trait Confirmation extends CborSerializable

  final case class Accepted(msg: String) extends Confirmation

  final case class Rejected(reason: String) extends Confirmation

  // Events = will be stored

  sealed trait Event extends CborSerializable

  final case class GemsObtained(playerId: String, quantity: List[Gem]) extends Event


  // Init
  // Cluster Sharding
  // - will take care of the lifecycle of this Board Game instance
  // - ensures that there is only 1 instance of Board Game in the cluster
  // - routes msgs to specific Board Game instance

  // Factory like of BoardGame instances

  val EntityKey: EntityTypeKey[Command] = EntityTypeKey[Command]("BoardGame")

  def init(system: ActorSystem[_]): Unit = {
    ClusterSharding(system).init(Entity(EntityKey) { entityContext =>
      BoardGame(entityContext.entityId)
    })
  }


  def handleCommand(playerId: String, state: State, command: Command): Effect[Event, State] =
    command match {
      case GetGems(quantity, replyTo) =>
        if (quantity.size <= 0) {
          replyTo ! Rejected("Quantity must be greater than zero")
          Effect.none
        } else {
          Effect.persist(GemsObtained(playerId, quantity))
            .thenRun(updatedBoard => replyTo ! Accepted("Success!"))
        }
    }

  def handleEvent(state: State, event: Event): State =
    // where the replaying of stored events happen
    event match {
      case GemsObtained(_, quantity) => state.updateGems(quantity: List[Gem])
    }

  def apply(playerId: String): Behavior[Command] = {
    EventSourcedBehavior[Command, Event, State](
      PersistenceId("BoardGame", playerId),
      State.empty,
      (state, command) => handleCommand(playerId, state, command),
      (state, event) => handleEvent(state, event)
    )
  }



}
