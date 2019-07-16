package de.richargh.sandbox.kt.mvn.concurrency.akka.routing

import akka.actor.AbstractActor
import akka.actor.Props
import de.richargh.sandbox.kt.mvn.concurrency.akka.printing.Counter

class Blub

class CountMessage(val threadId: String, val msg: String)

class CounterActor(private val counter: Counter): AbstractActor() {

    companion object {
        fun props(counter: Counter): Props {
            return Props.create(CounterActor::class.java) {
                CounterActor(counter)
            }
        }
    }

    override fun createReceive(): Receive = receiveBuilder()
            .match(CountMessage::class.java) {
                counter.message(it.threadId, it.msg)
            }.match(Blub::class.java){
                println("received blub")
                sender.tell(Blub(), self)
            }      .matchAny{o -> println("received unknown message $o")}
            .build()
}