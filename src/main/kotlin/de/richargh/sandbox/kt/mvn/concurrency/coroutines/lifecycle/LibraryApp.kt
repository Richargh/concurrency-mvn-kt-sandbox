package de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

class LibraryApp(private val bookStore: RemoteBookStore): Lifecycle {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main + CoroutineName("LibraryApp")

    private val books = mutableListOf<Book>()

    override suspend fun initialize() = coroutineScope {
        bookStore.allBooks().forEach(books::add)
    }

    override fun start() {

    }

    override fun shutdown() {
        job.cancel()
    }

    val bookCount
        get() = books.size
}