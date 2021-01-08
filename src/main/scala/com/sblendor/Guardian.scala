package com.sblendor

import akka.actor.typed.{Behavior, PostStop, Signal}
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}

object Guardian {
  def apply[T](): Behavior[T] = Behaviors.setup[T](context => new Guardian(context))
}

class Guardian[T](context: ActorContext[T]) extends AbstractBehavior[T](context) {

  context.log.info("Guardian started...")

  override def onMessage(msg: T): Behavior[T] = {
    Behaviors.unhandled
  }

  override def onSignal: PartialFunction[Signal, Behavior[T]] = {
    case PostStop =>
      context.log.info("Guardian stopped...")
      this
  }
}
