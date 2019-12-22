package de.richargh.sandbox.kt.mvn.concurrency.coroutines.intergration

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.shared_kernel.Lifecycle
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LifecycleTest {

    @Test
    fun `can collect data on start`() {
        // arrange
        val greeting = flow {
            emit("Hello")
            emit("World")
        }
        val collectApp = CollectApp(greeting)

        runBlocking {
            with(collectApp){
                initialize()
                // act
                start()
            }
            delay(50)
        }
        collectApp.shutdown()

        // assert
        assertThat(collectApp.collection).containsExactly("Hello", "World")
    }


    private inner class CollectApp(val flow: Flow<String>):
            Lifecycle,
            CoroutineScope by CoroutineScope(
                    // job does not fail if one child fails
                    SupervisorJob() +
                    // CPU-intensive work
                    Dispatchers.Default +
                    // consolidated name for debugging
                    CoroutineName("CollectApp")) {

        val collection = mutableListOf<String>()

        override suspend fun initialize() {
            // we could initialize data here
        }

        override fun CoroutineScope.start(){
            launch {
                flow.collect { collection += it }
            }
        }

        override fun shutdown() {
            // cancel all running jobs
            cancel()
        }

    }
}