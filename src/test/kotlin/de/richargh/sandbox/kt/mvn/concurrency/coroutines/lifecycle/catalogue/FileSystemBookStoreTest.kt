package de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.catalogue

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class FileSystemBookStoreTest {

    private val bookUri = this::class.java.getResource("Books.txt").toURI()

    @Test
    fun `can find all books in local file system`() = runBlockingTest {
        // arrange
        val bookStore: RemoteBookStore =
                FileSystemBookStore(bookUri)

        // act
        val allBooks = bookStore.allBooks()

        // assert
        assertThat(allBooks.map { it.title }).containsExactly(
                "No Code",
                "Destructoring")
    }
}