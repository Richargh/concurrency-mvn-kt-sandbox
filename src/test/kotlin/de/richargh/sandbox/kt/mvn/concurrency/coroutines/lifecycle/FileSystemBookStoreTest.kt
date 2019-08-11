package de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.runBlockingTest
import kotlinx.coroutines.async
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class FileSystemBookStoreTest {

    private val bookUri = this::class.java.getResource("Books.txt").toURI()

    @Test
    fun `can find all books in local file system`() = runBlockingTest {
        // arrange
        val bookStore: RemoteBookStore = FileSystemBookStore(bookUri)

        // act
        val allBooks = bookStore.allBooks()

        // assert
        assertThat(allBooks).containsExactly(Book("No Code"), Book("Destructoring"))
    }
}