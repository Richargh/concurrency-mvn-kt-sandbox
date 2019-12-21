package de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.catalogue

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.catalogue_builder.CatalogueAppBuilder
import de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.shared_kernel.LocalNotifier
import de.richargh.sandbox.kt.mvn.concurrency.coroutines.test_helper.testBlocking
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class CatalogueAppTest {

    @DisplayName("Initialization")
    @Nested
    inner class Initialization {

        @Test
        fun `without initialization there are no books available`() {
            // arrange
            val app = CatalogueAppBuilder()
                    .build()

            // act

            // assert
            assertThat(app.bookCount).isEqualTo(0)
        }

        @Test
        fun `with initialization there are books available`() =
                testBlocking {
                    // arrange
                    val app = CatalogueAppBuilder()
                            .build()

                    // act
                    app.initialize()

                    // assert
                    assertThat(app.bookCount).isGreaterThan(0)
                }

        @Test
        fun `the initialization is non-blocking and without it there are no books available`() =
                testBlocking {
                    // arrange
                    val app = CatalogueAppBuilder()
                            .build()

                    // act
                    launch {
                        delay(100)
                        println("Init")
                        app.initialize()
                    }

                    // assert
                    println("assert")
                    assertThat(app.bookCount).isEqualTo(0)
                }

        @Test
        fun `notifies me every time a new book is added`() =
                testBlocking {
                    // arrange
                    val notifier = LocalNotifier(Dispatchers.Default)

                    val app = CatalogueAppBuilder()
                            .withNotifier(notifier)
                            .build()

                    // act
                    launch {
                        delay(100)
                        println("Init")
                        app.initialize()
                    }

                    // assert
                    println("assert")
                    assertThat(app.bookCount).isEqualTo(0)
                }
    }

    @DisplayName("Subscription")
    @Nested
    inner class Subscription {

    }

    @DisplayName("Polling")
    @Nested
    inner class Polling {

    }
}