package de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.runBlockingTest
import kotlinx.coroutines.Dispatchers
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class LibraryAppTest{

    private val bookUri = this::class.java.getResource("Books.txt").toURI()

    @Test
    fun `without initialization there are no books available`() {
        // arrange
        val app = LibraryApp(Dispatchers.Main, FileSystemBookStore(bookUri))

        // act

        // assert
        assertThat(app.bookCount).isEqualTo(0)
    }

    @Test
    fun `without initialization there are books available`() = runBlockingTest {
        // arrange
        val app = LibraryApp(Dispatchers.Main, FileSystemBookStore(bookUri))

        // act
        app.initialize()

        // assert
        assertThat(app.bookCount).isGreaterThan(0)
    }
}