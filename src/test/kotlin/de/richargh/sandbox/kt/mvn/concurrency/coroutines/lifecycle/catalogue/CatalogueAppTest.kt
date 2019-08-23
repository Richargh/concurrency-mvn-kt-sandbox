package de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.catalogue

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.catalogue_builder.CatalogueAppBuilder
import de.richargh.sandbox.kt.mvn.concurrency.coroutines.runBlockingTest
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
        fun `with initialization there are books available`() = runBlockingTest {
            // arrange
            val app = CatalogueAppBuilder()
                    .build()

            // act
            app.initialize()

            // assert
            assertThat(app.bookCount).isGreaterThan(0)
        }

        @Test
        fun `the initialization is non-blocking and without it there are no books available`() = runBlockingTest {
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
    }

    @DisplayName("Subscription")
    @Nested
    inner class Subscription {

        @Test
        fun `without initialization there are no books available`() {
            // arrange
            val app = CatalogueAppBuilder()
                    .build()

            // act

            // assert
            assertThat(app.bookCount).isEqualTo(0)
        }
    }

    @DisplayName("Polling")
    @Nested
    inner class Polling {

        @Test
        fun `without initialization there are no books available`() {
            // arrange
            val app = CatalogueAppBuilder()
                    .build()

            // act

            // assert
            assertThat(app.bookCount).isEqualTo(0)
        }
    }
}