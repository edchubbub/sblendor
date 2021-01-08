package com.sblendor

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import akka.cluster.ClusterEvent
import akka.cluster.ClusterEvent.{MemberEvent, ReachabilityEvent}
import akka.cluster.typed.{Cluster, Subscribe}
import akka.actor.typed.scaladsl.LoggerOps

object ClusterListener {

  sealed trait Event

  // internal adapted cluster events
  private final case class ReachabilityChange(reachabilityEvent: ReachabilityEvent) extends Event
  private final case class MemberChange(memberEvent: MemberEvent) extends Event

  def apply(): Behavior[Event] = Behaviors.setup { ctx =>

    val memberEventAdapter: ActorRef[MemberEvent] = ctx.messageAdapter(MemberChange)
    Cluster(ctx.system).subscriptions ! Subscribe(memberEventAdapter, classOf[MemberEvent])

    val reachabilityAdapter: ActorRef[ReachabilityEvent] = ctx.messageAdapter(ReachabilityChange)
    Cluster(ctx.system).subscriptions ! Subscribe(reachabilityAdapter, classOf[ReachabilityEvent])

    Behaviors.receiveMessage { message =>
      message match {
        case ReachabilityChange(reachabilityEvent) =>
          reachabilityEvent match {
            case ClusterEvent.UnreachableMember(member) => ctx.log.info("Member detected as unreachable: {}", member)
            case ClusterEvent.ReachableMember(member)   => ctx.log.info("Member back to reachable: {}", member)
          }
        case MemberChange(memberEvent) =>
          memberEvent match {
            case ClusterEvent.MemberUp(member)                      => ctx.log.info("Member is Up: {}", member.address)
            case ClusterEvent.MemberRemoved(member, previousStatus) => ctx.log.info2("Member is Removed: {} after {}", member.address, previousStatus)
            case _: MemberEvent => // ignore
          }
      }
      Behaviors.same
    }

  }
}
