package de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle_builder

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.FileSystemBookStore
import de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.LibraryApp
import kotlinx.coroutines.Dispatchers

class LibraryAppBuilder{

    private val bookUri = this::class.java.getResource("Books.txt").toURI()

    fun build() = LibraryApp(
            Dispatchers.Main,
            FileSystemBookStore(bookUri))

}