package de.richargh.sandbox.kt.mvn.concurrency.coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import java.lang.RuntimeException
import java.util.concurrent.ForkJoinPool
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

internal class ChannelSampleTest {

    fun runBlockingTest(context: CoroutineContext = EmptyCoroutineContext, block: suspend CoroutineScope.() -> Any) = runBlocking<Unit>(context) {
        if (launch { block() }.isCancelled) fail("Probably an exception was swallowed.")
    }

    @Test
    fun `data exchanged between one sender and two receivers via coroutine channel is not lost`() {
        val threadPoolSize = 2  // for one sender and two receivers
        val numPackages = 20
        val channelBufferSize = 3
        val throttleInMs = 100L

        runBlocking(ForkJoinPool(threadPoolSize).asCoroutineDispatcher()) {
            // arrange
            val channel = Channel<Data>(capacity = channelBufferSize)
            val sender       = ChannelSender("S1", channel, numPackages)
            val slowReceiver = ChannelReceiver("R1", channel, throttleInMs * 5)
            val fastReceiver = ChannelReceiver("R2", channel, throttleInMs * 1)

            // act

            // receivers need to be started first
            launch { slowReceiver.receiver() }
            launch { fastReceiver.receiver() }
            // as otherwise the sender is blocked as noone is taking data out of the channel
            launch { sender.sender()     }.join()

            channel.close()

            // assert
            assertThat(sender.numPackagesSent == slowReceiver.numPackagesReceived + fastReceiver.numPackagesReceived)
        }
    }

/*

Exception handling, needs cleanup

    @Test
    fun `foo`() = runBlockingTest(ForkJoinPool(10).asCoroutineDispatcher()) {
        launch {
            delay(500)
            throw RuntimeException("evil")
        }
    }

    @Test
    fun `send on closed channel throws exception`(){
        runBlockingTest {
            val job = launch {
                println("${Thread.currentThread().name} hier")

                println("${Thread.currentThread().name} da")

                val channel = Channel<Int>()
                channel.close()
                try {
                    channel.send(42)
                }catch (t: Throwable){
                    println("Caught $t")
                }
                throwsMyEx()

                // ChannelClosedException is thrown here but somewhat swallowed. Test not red? Wat?

                throw RuntimeException("evil")
            }

            println("Job successfully completed = ${job.isCompleted}")
        }
    }

    suspend fun throwsMyEx() {
        delay(1000)
        throw RuntimeException("huhu")
    }


 */
}

