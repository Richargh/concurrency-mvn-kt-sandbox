package de.richargh.sandbox.kt.mvn.concurrency.akka.typed

import akka.actor.TypedActor
import akka.actor.TypedProps
import akka.routing.RoundRobinRoutingLogic
import akka.routing.Routee
import akka.routing.Router
import de.richargh.sandbox.kt.mvn.concurrency.akka.routing.Work
import de.richargh.sandbox.kt.mvn.concurrency.akka.routing.Worker

class TypedMasterActor: TypedMaster {

    private val context = TypedActor.context()

//    private val router: Router

    init {
//        val routees: List<Routee> = (1..5).map {
//            val mySquarer: Squarer = TypedActor.get(TypedActor.context())
//                    .typedActorOf(TypedProps<SquarerActor>(Squarer::class.java, SquarerActor::class.java))
//            val ref = context.actorOf(
//                    Worker.props(counterActor,
//                                 "Worker $it"))
//            context.watch(ref)
////            ActorRefRoutee(mySquarer)
//        }
//        router = Router(RoundRobinRoutingLogic(), routees)
    }

    override fun dispatchWork(work: Work) {
    }
}