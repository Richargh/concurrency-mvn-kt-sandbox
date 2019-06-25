package de.richargh.sandbox.kt.mvn.concurrency

import java.util.concurrent.ConcurrentLinkedQueue

class SpyPublisher: Publisher {

    val publishedMessages = ConcurrentLinkedQueue<String>()

    override fun publish(message: String) {
        publishedMessages.add(message)
    }
}