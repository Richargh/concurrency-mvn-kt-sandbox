package de.richargh.sandbox.kt.mvn.concurrency.akka.typed

import akka.actor.TypedActor
import akka.japi.Option
import scala.concurrent.Future

class SquarerActor: Squarer {

    private val context = TypedActor.context()

    override fun squareDontCare(i: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun square(i: Int): Future<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun squareNowPlease(i: Int): Option<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun squareNow(i: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}