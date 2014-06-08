package org.johnywith1n.main

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.Props
import akka.actor.actorRef2Scala
import org.johnywith1n.bitly.BitlyLinksGetter

object MainActor {

}

class MainActor extends Actor with ActorLogging {
  
 override def preStart(): Unit = {
    val bitlyRequester = context.actorOf(Props[BitlyLinksGetter], "bitly-requester")
    bitlyRequester ! BitlyLinksGetter.Request(0)
  }

  def receive = {
    case HttpRequestMaker.Done(url, res) => 
      log.info(url)
      log.info(res)
      context.stop(self)
  }
  
}