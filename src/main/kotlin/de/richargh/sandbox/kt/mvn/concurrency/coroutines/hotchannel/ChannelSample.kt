package de.richargh.sandbox.kt.mvn.concurrency.coroutines.hotchannel

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach

fun threadContext() = "${Thread.currentThread().name}"

class ChannelSender(private val name: String, private val channel: Channel<Data>, private val numPackages: Int) {
    var numPackagesSent = 0

    suspend fun sender() {
        for (i in 1..numPackages) {
            println(threadContext() + " ChannelSender $name:   Send $i to   channel ${channel}")
            channel.send(Data(i))
            numPackagesSent++
        }
    }
}

class ChannelReceiver(private val name: String, private val channel: Channel<Data>, private val throttleInMs: Long) {
    var numPackagesReceived = 0

    @ObsoleteCoroutinesApi
    suspend fun receiver() {
        channel.consumeEach {data ->
            println(threadContext() + " ChannelReceiver $name: Get  $data from channel ${channel}")
            numPackagesReceived++

            delay(throttleInMs)
        }
    }
}

class Data(val rawValue: Int) {
    override fun toString(): String = rawValue.toString()
}
