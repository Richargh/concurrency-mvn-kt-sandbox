package de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.catalogue

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.shared_kernel.Lifecycle
import de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.shared_kernel.Notifier
import kotlinx.coroutines.*

class CatalogueApp(
        dispatcher: CoroutineDispatcher,
        private val bookStore: RemoteBookStore,
        private val notifier: Notifier):
        Lifecycle {

    override val coroutineContext = SupervisorJob() +
                                    dispatcher +
                                    CoroutineName(javaClass.simpleName)

    private val books = mutableListOf<Book>()

    override suspend fun initialize() = coroutineScope {
        bookStore.allBooks().forEach { books.add(it) }

        Unit
    }

    override fun CoroutineScope.start() {

    }

    override fun shutdown() {
        cancel()
    }

    val bookCount
        get() = books.size
}