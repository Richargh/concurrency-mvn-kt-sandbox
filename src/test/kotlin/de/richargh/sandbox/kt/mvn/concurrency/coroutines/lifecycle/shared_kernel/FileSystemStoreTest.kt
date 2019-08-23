package de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.shared_kernel

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class FileSystemStoreTest {

    private val dataUri = this::class.java.getResource("Names.txt").toURI()

    @Test
    fun `can read data from file system`() = runBlockingTest {
        // arrange
        val channel = lines(dataUri)

        // act
        val names = mutableListOf<String>()
        println("${Thread.currentThread().name} Listening on channel: ")
        for(line in channel) {
            println("${Thread.currentThread().name} Received name: [$line]")
            names.add(line)
        }

        // assert
        assertThat(names).containsExactly("Robert Martin", "Martin Fowler", "Fowler Dreifuchs")
    }
}