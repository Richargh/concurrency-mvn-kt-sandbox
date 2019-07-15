package de.richargh.sandbox.kt.mvn.concurrency.akka

import akka.actor.AbstractLoggingActor
import akka.japi.pf.ReceiveBuilder

class HelloKotlinActor : AbstractLoggingActor() {
    override fun createReceive() = ReceiveBuilder().match(String::class.java) { log().info(it) }.build()
}