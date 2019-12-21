package de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.catalogue

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.shared_kernel.Lifecycle
import de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.shared_kernel.Notifier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope

class CatalogueApp(
        dispatcher: CoroutineDispatcher,
        private val bookStore: RemoteBookStore,
        private val notifier: Notifier):
        Lifecycle {

    private val job = SupervisorJob()
    override val coroutineContext = job + dispatcher +
                                    CoroutineName("CatalogueApp")

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