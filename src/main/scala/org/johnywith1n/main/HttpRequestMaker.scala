package org.johnywith1n.main

import scala.util.Failure
import scala.util.Success

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.actorRef2Scala
import dispatch.Defaults.executor
import dispatch.Http
import dispatch.as
import dispatch.implyRequestHandlerTuple
import dispatch.url

object HttpRequestMaker {
  case class Request(link: String)
  case class Done(link: String, response: String)
  case class Failed(link: String, e: Throwable)
}
class HttpRequestMaker extends Actor with ActorLogging {
  import HttpRequestMaker._
  
  def receive = {
    case Request(link) =>
      val http = Http
      val _sender = sender
      
      try {
        val res = http(url(link) OK as.String)
        res onComplete {
          case Success(s) =>
            _sender ! Done(link, s)
            http.shutdown
          case Failure(e) =>
            _sender ! Failed(link, e)
            http.shutdown
        }
      } catch {
        case e: Exception =>
          _sender ! Failed(link, e)
          http.shutdown
      }
  }
}