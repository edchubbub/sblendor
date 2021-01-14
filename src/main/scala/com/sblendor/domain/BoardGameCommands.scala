package com.sblendor.domain

import akka.actor.typed.ActorRef
import com.sblendor.domain.Gem.Gem

object BoardGameCommands {

  /**
   * Commands = incoming message
   */

  sealed trait Command extends CborSerializable

  final case class GetGems(quantity: List[Gem], replyTo: ActorRef[Confirmation]) extends Command

  sealed trait Confirmation extends CborSerializable

  final case class Accepted(msg: String) extends Confirmation
  final case class Rejected(reason: String) extends Confirmation

}
