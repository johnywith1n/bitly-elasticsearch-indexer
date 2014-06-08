package org.johnywith1n.bitly

import scala.util.parsing.json.JSON

import org.johnywith1n.main.HttpRequestMaker
import org.johnywith1n.main.HttpRequestMaker.Done
import org.johnywith1n.main.HttpRequestMaker.Failed

import com.typesafe.config.ConfigFactory

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.Props
import akka.actor.actorRef2Scala

object BitlyLinksGetter {
  val conf = ConfigFactory.load();
  val accessToken = conf.getString("access_token")

  val linksHistoryUrl = "https://api-ssl.bitly.com/v3/user/link_history?access_token=" + accessToken

  case class Request(offset: Int)
  case class Response(links: List[String])
}

class BitlyLinksGetter extends Actor with ActorLogging {
  import BitlyLinksGetter._

  def receive = {
    case Request(offset) =>
      val httpRequester = context.actorOf(Props[HttpRequestMaker], "bity-links-requester&offset:"+offset)
      httpRequester ! HttpRequestMaker.Request(linksHistoryUrl + "&offset=" + offset)
    case Done(url, response) =>
      println(JSON.parseFull(response))
      context.stop(sender)
    case Failed(url, e) =>
      log.error("Unable to retrieve url: {}  due to exception: {}", url, e)
      context.stop(sender)
      
  }
}