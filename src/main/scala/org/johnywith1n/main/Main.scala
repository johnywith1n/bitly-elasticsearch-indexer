package org.johnywith1n.main

object Main extends App {
  akka.Main.main(Array(classOf[MainActor].getName))
}