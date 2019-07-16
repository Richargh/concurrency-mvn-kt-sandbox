package de.richargh.sandbox.kt.mvn.concurrency.akka.routing

import akka.actor.AbstractActor
import akka.actor.ActorRef
import akka.actor.Props
import akka.routing.ActorRefRoutee
import akka.routing.RoundRobinRoutingLogic
import akka.routing.Routee
import akka.routing.Router
import akka.pattern.Patterns.ask

class MasterActor private constructor(private val counterActor: ActorRef): AbstractActor() {
    companion object {
        fun props(counterActor: ActorRef): Props {
            return Props.create(MasterActor::class.java) {
                MasterActor(counterActor)
            }
        }
    }

    private val router: Router

    init {
        val routees: List<Routee> = (1..5).map {
            val ref = context.actorOf(
                    Worker.props(counterActor,
                                 "Worker $it"))
            context.watch(ref)
            ActorRefRoutee(ref)
        }
        router = Router(RoundRobinRoutingLogic(), routees)
    }

    override fun createReceive() = receiveBuilder()
            .match(Work::class.java) {
                router.route(it, sender)

            }
            .build()
}

class Work

class Worker private constructor(private val counterActor: ActorRef, private val name: String): AbstractActor() {
    companion object {
        fun props(counterActor: ActorRef, name: String): Props {
            return Props.create(Worker::class.java) {
                Worker(counterActor, name)
            }
        }
    }

    override fun createReceive(): Receive = receiveBuilder()
            .match(Work::class.java, this::handle)
            .build()

    private fun handle(work: Work) {
        val threadId = Thread.currentThread().name
        val msg = heavyWork().toString()
        counterActor.tell(CountMessage(threadId, msg), self)
    }

    private fun heavyWork() = (1..100_000).fold(1.0) { acc, i ->
        4 * (Math.pow(-1.0, (i - 1).toDouble()) / ((2 * i) - 1))
    }
}
