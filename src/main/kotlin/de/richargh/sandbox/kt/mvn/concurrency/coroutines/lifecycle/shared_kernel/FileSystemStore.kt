package de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.shared_kernel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.FileInputStream
import java.io.Reader
import java.net.URI

suspend fun CoroutineScope.lines(uri: URI): ReceiveChannel<String> {
    val channel = Channel<String>()

    launch {
        delay(500)
        println("${Thread.currentThread().name} Reading all lines")
        val lines = stream(uri).bufferedReader().use(Reader::readLines)
        lines.forEach {
            println("${Thread.currentThread().name} Sending line [$it]")
            channel.send(it)
        }
        channel.close()
    }

    println("${Thread.currentThread().name} Returning channel")
    return channel
}

private fun CoroutineScope.stream(uri: URI) = when {
    uri.scheme != null && uri.scheme == "classpath" ->
        this::class.java.getResourceAsStream(uri.schemeSpecificPart.drop(1))
    else                                            ->
        FileInputStream(uri.schemeSpecificPart)
}
