package de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.catalogue

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.test_helper.testBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class FileSystemBookStoreTest {

    private val bookUri = this::class.java.getResource("Books.txt").toURI()

    @Test
    fun `can find all books in local file system`() =
            testBlocking {
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