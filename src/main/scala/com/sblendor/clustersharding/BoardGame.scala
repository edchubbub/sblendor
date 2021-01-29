package com.sblendor.clustersharding

import akka.actor.typed.scaladsl.{Behaviors, LoggerOps}
import akka.actor.typed.{ActorSystem, Behavior, SupervisorStrategy}
import akka.cluster.sharding.typed.scaladsl.{ClusterSharding, Entity, EntityTypeKey}
import akka.persistence.typed.PersistenceId
import akka.persistence.typed.scaladsl.{Effect, EventSourcedBehavior, ReplyEffect}
import com.sblendor.persistence.BoardGameCommands._
import com.sblendor.persistence.BoardGameEvents.{Event, GameEnded, GemsObtained, PlayerJoined}
import com.sblendor.persistence.BoardGameStates
import com.sblendor.persistence.BoardGameStates.{Playing, Ready, State}
import com.sblendor.persistence.Gem.Gem

import scala.annotation.tailrec
import scala.concurrent.duration._

object BoardGame {

  /**
   * Init
   *
   * Factory of BoardGame instances
   *
   * Cluster Sharding
   * - will take care of the lifecycle of this Board Game instance
   * - ensures that there is only 1 instance of Board Game in the cluster
   * - routes msgs to specific Board Game instance
   *
   */

  val EntityKey: EntityTypeKey[Command] = EntityTypeKey[Command]("BoardGame")

  def initSharding(system: ActorSystem[_]): Unit = {
    ClusterSharding(system).init(Entity(EntityKey) { entityContext =>
      BoardGame(entityContext.entityId)
    })
  }

  def apply(playerId: String): Behavior[Command] = {
    Behaviors.setup[Command] { context =>
      EventSourcedBehavior.withEnforcedReplies[Command, Event, State](
        PersistenceId("BoardGame", playerId),
        BoardGameStates.State.ready,
        (state, command) => handleCommand(playerId, state, command),
        (state, event)   => handleEvent(state, event)
      ).snapshotWhen((state, _, _) => {
        context.log.info2("Snapshot actor {} => state: {}", context.self.path.name, state)
        true
      }).onPersistFailure {
        context.log.error("Persist failure")
        SupervisorStrategy.restartWithBackoff(
          minBackoff = 10.seconds,
          maxBackoff = 60.seconds,
          randomFactor = 0)
      }
    }
  }

  @tailrec
  def handleCommand(playerId: String, state: State, command: Command): ReplyEffect[Event, State] = {
    state match {
      case _ if state.isReady =>
        state.updateDescription(Playing)
        handleCommand(playerId, state, command)
      case _ =>
        command match {
          case GetGems(quantity, replyTo) =>
            if (quantity.size <= 0) {
              Effect.unhandled
                .thenReply(replyTo)(_ => Rejected("Quantity must be greater than zero"))
            } else {
              Effect.persist(GemsObtained(playerId, quantity))
                .thenReply(replyTo)(_ => Accepted("Success!"))
            }
          case JoinPlayer(player, replyTo) =>
            Effect.persist(PlayerJoined(player))
              .thenReply(replyTo)(_ => Accepted("Success!"))
          case _ => Effect.noReply
        }
    }
  }

  def handleEvent(state: State, event: Event): State =
    state.description match {
      case Ready => state
      case Playing =>
        event match {
          case GemsObtained(_, quantity) => state.updateGems(quantity: List[Gem])
        }
//      case d: Playing => throw new IllegalStateException(s"unexpected event [$event] in state [$state]")
    }

//  val sourceProvider: SourceProvider[Offset, EventEnvelope[BoardGameEvents.Event]] =
//    EventSourcedProvider
//      .eventsByTag[BoardGameEvents.Event](system, readJournalPluginId = CassandraReadJournal.Identifier, tag = "")

}
