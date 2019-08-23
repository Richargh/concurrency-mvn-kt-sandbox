package de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.catalogue_builder

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.catalogue.CatalogueApp
import de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.catalogue.FileSystemBookStore
import de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.catalogue.RemoteBookStore
import de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.shared_kernel.LocalPublisher
import de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.shared_kernel.Publisher
import kotlinx.coroutines.Dispatchers

class CatalogueAppBuilder{

    private val bookUri = this::class.java.getResource("Books.txt").toURI()

    private var bookStore: RemoteBookStore = FileSystemBookStore(bookUri)
    private var publisher: Publisher = LocalPublisher()

    fun build() = CatalogueApp(
            Dispatchers.Main,
            bookStore,
            publisher)

}