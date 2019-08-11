package de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class LibraryAppTest{

    private val bookUri = this::class.java.getResource("Books.txt").toURI()

    @Test
    fun `without initialization there are no books available`() {
        // arrange
        val app = LibraryApp(FileSystemBookStore(bookUri))

        // act

        // assert
        assertThat(app.bookCount).isEqualTo(0)
    }

    @Test
    fun `without initialization there are books available`() {
        // arrange
        val app = LibraryApp(FileSystemBookStore(bookUri))

        // act
        app.initialize()

        // assert
        assertThat(app.bookCount).isGreaterThan(0)
    }
}