package de.richargh.sandbox.kt.mvn.concurrency.akka.sample

import akka.actor.ActorSystem
import akka.actor.Props
import akka.testkit.javadsl.TestKit
import com.typesafe.config.ConfigFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.time.Duration

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestKitSampleTest {

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
        /*
     * Wrap the whole test procedure within a testkit constructor
     * if you want to receive actor replies or use Within(), etc.
     */
        object: TestKit(system) {
            init {
                val subject = system.actorOf(Props.create(
                        SomeActor::class.java) { SomeActor() }, "master")

                // can also use JavaTestKit “from the outside”
                val probe = TestKit(system)
                // “inject” the probe by passing it to the test subject
                // like a real resource would be passed in production
                subject.tell(probe.ref, ref)
                // await the correct response
                expectMsg(Duration.ofSeconds(1), "done")

                // the run() method needs to finish within 3 seconds
                within(Duration.ofSeconds(3)) {
                    subject.tell("hello", ref)

                    // This is a demo: would normally use expectMsgEquals().
                    // Wait time is bounded by 3-second deadline above.
                    awaitCond(probe::msgAvailable)

                    // response must have been enqueued to us before probe
                    expectMsg(Duration.ZERO, "world")
                    // check that the probe we injected earlier got the msg
                    probe.expectMsg(Duration.ZERO, "hello")
                    assertThat(ref).isEqualTo(probe.lastSender)

                    // Will wait for the rest of the 3 seconds
                    expectNoMessage()
                    null
                }
            }
        }
    }
}