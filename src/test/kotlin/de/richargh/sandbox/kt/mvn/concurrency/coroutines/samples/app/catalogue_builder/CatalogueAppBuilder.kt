package de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.catalogue_builder

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.catalogue.CatalogueApp
import de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.catalogue.FileSystemBookStore
import de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.catalogue.RemoteBookStore
import de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.shared_kernel.LocalNotifier
import de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.shared_kernel.Notifier
import kotlinx.coroutines.Dispatchers

class CatalogueAppBuilder{

    private val bookUri = this::class.java.getResource("Books.txt").toURI()

    private var bookStore: RemoteBookStore = FileSystemBookStore(bookUri)
    private var notifier: Notifier = LocalNotifier(Dispatchers.Default)

    fun build() = CatalogueApp(
            Dispatchers.Main,
            bookStore,
            notifier)

    fun withNotifier(notifier: Notifier) = apply {
        this.notifier = notifier
    }

}