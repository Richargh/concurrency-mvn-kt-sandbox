package de.richargh.sandbox.kt.mvn.concurrency.akka.typed

import akka.actor.ActorSystem
import akka.actor.TypedActor
import akka.actor.TypedProps
import akka.testkit.javadsl.TestKit
import com.typesafe.config.ConfigFactory
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SquarerActorTest {

    private lateinit var system: ActorSystem

    @BeforeAll
    fun setup() {
        val config = ConfigFactory.load()
        system = ActorSystem.create("test", config)
    }

    @AfterAll
    fun teardown() {
        TestKit.shutdownActorSystem(system)
    }

    @Test
    fun testIt() {
        object: TestKit(system) {
            init {
                val mySquarer: Squarer = TypedActor.get(system)
                        .typedActorOf(TypedProps<SquarerActor>(Squarer::class.java, SquarerActor::class.java))
                mySquarer.squareDontCare(5)
            }
        }
    }
}