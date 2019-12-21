package de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.catalogue

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.shared_kernel.lines
import kotlinx.coroutines.coroutineScope
import java.net.URI

class FileSystemBookStore(private val location: URI): RemoteBookStore {

    override suspend fun allBooks() = coroutineScope {
        val books = mutableListOf<Book>()
        for (bookTitle in lines(location)) {
            books.add(Book(BookId.random(), bookTitle))
        }
        books
    }
}