package com.sblendor

import akka.actor.typed.{ActorSystem, Behavior, PostStop, Signal}
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}

/**
 * Root actor bootstrapping the application
 */

object Guardian {
  def apply[T](httpPort: Int): Behavior[T] = Behaviors.setup[T](context => new Guardian(httpPort, context))
}

class Guardian[T](httpPort: Int, context: ActorContext[T]) extends AbstractBehavior[T](context) {

  context.log.info("Guardian starting...")

  BoardGame.initSharding(context.system)
  val routes = new BoardGameRoutes(context.system.asInstanceOf[ActorSystem[T]])
  context.log.info("PORT {}", httpPort)
  BoardGameHttpServer.start(routes.boardGame, httpPort, context.system)

  override def onMessage(msg: T): Behavior[T] = {
    Behaviors.unhandled
  }

  override def onSignal: PartialFunction[Signal, Behavior[T]] = {
    case PostStop =>
      context.log.info("Guardian stopped...")
      this
  }
}
