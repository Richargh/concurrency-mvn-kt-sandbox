package de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle

import kotlinx.coroutines.CoroutineScope

interface Lifecycle: CoroutineScope {

    suspend fun initialize()

    fun start()

    fun shutdown()
}