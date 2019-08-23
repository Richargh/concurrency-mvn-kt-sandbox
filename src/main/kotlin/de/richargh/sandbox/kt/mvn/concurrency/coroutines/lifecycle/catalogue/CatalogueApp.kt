package de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.catalogue

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.shared_kernel.Lifecycle
import de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.shared_kernel.Notifier
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class CatalogueApp(
        private val dispatcher: CoroutineDispatcher,
        private val bookStore: RemoteBookStore,
        private val notifier: Notifier):
        Lifecycle {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + dispatcher + CoroutineName("CatalogueApp")


    private val books = mutableListOf<Book>()

    override suspend fun initialize() = coroutineScope {
        bookStore.allBooks().forEach { books.add(it) }

        Unit
    }

    override fun start() {
    }

    override fun shutdown() {
        job.cancel()
    }

    val bookCount
        get() = books.size
}