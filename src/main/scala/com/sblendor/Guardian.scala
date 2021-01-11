package com.sblendor

import akka.actor.typed.{Behavior, PostStop, Signal}
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}

/**
 * Root actor bootstrapping the application
 */

object Guardian {
  def apply[T](httpPort: Int): Behavior[T] = Behaviors.setup[T](context => new Guardian(httpPort, context))
}

class Guardian[T](httpPort: Int, context: ActorContext[T]) extends AbstractBehavior[T](context) {

  context.log.info("Guardian started...")

  def apply(httpPort: Int, context: ActorContext[T]): Behavior[T] = {
    BoardGame.initSharding(context.system)

    val routes = new BoardGameRoutes(context.system)
    BoardGameHttpServer.start(routes, httpPort, context.system)

    Behaviors.empty
  }

  override def onMessage(msg: T): Behavior[T] = {
    Behaviors.unhandled
  }

  override def onSignal: PartialFunction[Signal, Behavior[T]] = {
    case PostStop =>
      context.log.info("Guardian stopped...")
      this
  }
}
