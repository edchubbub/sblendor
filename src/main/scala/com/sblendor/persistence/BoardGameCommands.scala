package com.sblendor.persistence

import akka.actor.typed.ActorRef
import com.sblendor.domain.CborSerializable
import Gem.Gem

object BoardGameCommands {

  /**
   * Commands = incoming message
   */

  sealed trait Command extends CborSerializable
  final case class GetGems(quantity: List[Gem], replyTo: ActorRef[Confirmation]) extends Command
  final case class EndGame(replyTo: ActorRef[Confirmation]) extends Command

  sealed trait Confirmation extends CborSerializable
  final case class Accepted(msg: String) extends Confirmation
  final case class Rejected(reason: String) extends Confirmation

}
