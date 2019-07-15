package de.richargh.sandbox.kt.mvn.concurrency.akka.routing

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.testkit.javadsl.TestKit
import com.typesafe.config.ConfigFactory
import de.richargh.sandbox.kt.mvn.concurrency.akka.printing.Counter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.time.Duration

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class MasterTest {

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
    fun `akka does not swallow messages in-process when using round-robin routing`() {
        object: TestKit(system) {
            init {
                val maxCount = 1_000
                // arrange
                val counter = Counter()
                val counterActor: ActorRef = system.actorOf(
                        CounterActor.props(counter), "counter")
                val masterActor = system.actorOf(
                        MasterActor.props(counterActor), "masterActor")

                // act
                (1..maxCount).forEach { _ -> masterActor.tell(
                        Work(), ActorRef.noSender()) }

                // assert
                awaitAssert(Duration.ofMinutes(1)) {
                    println("Asserting ${counter.messageCount()} with ${counter.actorCount()} actors")
                    assertThat(counter.messageCount()).isEqualTo(maxCount)
                }
            }
        }
    }
}