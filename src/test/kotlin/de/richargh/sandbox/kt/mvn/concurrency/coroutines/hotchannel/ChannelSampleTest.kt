package de.richargh.sandbox.kt.mvn.concurrency.coroutines.hotchannel

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.concurrent.ForkJoinPool

internal class ChannelSampleTest {

    @ObsoleteCoroutinesApi
    @Test
    fun `data exchanged between one sender and two receivers via coroutine channel is not lost`() {
        val threadPoolSize = 2  // for one sender and two receivers
        val numPackages = 20
        val channelBufferSize = 3
        val throttleInMs = 100L

        runBlocking(ForkJoinPool(threadPoolSize).asCoroutineDispatcher()) {
            // arrange
            val channel = Channel<Data>(capacity = channelBufferSize)
            val sender       =
                    ChannelSender("S1", channel, numPackages)
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
}
