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
import akka.pattern.Patterns.ask
import java.util.concurrent.CompletableFuture

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class MasterActorTest {

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
                repeat(maxCount){ masterActor.tell(Work(), ActorRef.noSender()) }

                // assert
                val foo = ask(counterActor, Blub(), Duration.ofMinutes(1)).toCompletableFuture()
                CompletableFuture.allOf(foo)
//                ask(counterActor, "fpoop", 10000).value()

                println("Foo: $foo")

                awaitAssert(Duration.ofMinutes(1)) {

                    println("Asserting ${counter.messageCount()} with ${counter.actorCount()} actors")
                    assertThat(counter.messageCount()).isEqualTo(maxCount)
                }
            }
        }
    }
}