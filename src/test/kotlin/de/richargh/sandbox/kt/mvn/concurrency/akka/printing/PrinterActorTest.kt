package de.richargh.sandbox.kt.mvn.concurrency.akka.printing

import akka.actor.ActorSystem
import akka.testkit.javadsl.TestKit
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.time.Duration
import akka.actor.ActorRef
import com.typesafe.config.ConfigFactory

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PrinterActorTest {

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
    fun `Akka does not drop messages in-process, even if actors are busy`() {
        val counter = Counter()
        val maxCount = 10_000
        object: TestKit(system) {
            init {
                // arrange
                val actorRefs = (1..maxCount).map { system.actorOf(
                        PrinterActor.props(counter), "printerActor${it}") }

                // act
                actorRefs.forEach{ it.tell(PrintMessage(), ActorRef.noSender())}

                // assert
                awaitAssert(Duration.ofMinutes(1)) {
                    println("Asserting ${counter.messageCount()} with ${counter.actorCount()} actors")
                    assertThat(counter.messageCount()).isEqualTo(maxCount)
                }
            }
        }
    }
}