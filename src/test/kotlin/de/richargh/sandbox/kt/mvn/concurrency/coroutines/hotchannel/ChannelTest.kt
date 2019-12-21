package de.richargh.sandbox.kt.mvn.concurrency.coroutines.hotchannel

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import org.junit.jupiter.api.Test
import java.util.concurrent.ForkJoinPool

class ChannelTest {
    @Test
    fun `channel exception is not caught`() = runBlocking<Unit>(ForkJoinPool(2).asCoroutineDispatcher()) {
        // arrange
        val channel = Channel<Unit>()
        channel.close()

        println("Job: ${coroutineContext[Job]}")

        println(blub())

        // act
        async {
            //            channel.send(Unit)
            async {
                val result = async {
                    channel.send(Unit)
                }
                try {
                    result.await()
                } catch (t: Throwable) {

                }
            }

        }

        // assert
    }
}

suspend fun blub() {

    yield()
    println("Cancelling child")

    yield()
    println("Parent is not cancelled")


    coroutineScope {

    }
}