package de.richargh.sandbox.kt.mvn.concurrency.akka.sample

import akka.actor.AbstractActor
import akka.actor.ActorRef

class SomeActor: AbstractActor() {



    private var target: ActorRef? = null

    override fun createReceive(): Receive {
        return receiveBuilder()
                .matchEquals("hello") { message ->
                    println("message: Thread ${Thread.currentThread().name}")
                    sender.tell("world", self)
                    target?.forward(message, context)
                }
                .match(ActorRef::class.java) { actorRef ->
                    println("actor: Thread ${Thread.currentThread().name}")
                    target = actorRef
                    sender.tell("done", self)

                }.build()
    }
}