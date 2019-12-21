package de.richargh.sandbox.kt.mvn.concurrency.coroutines.hotchannel

import de.richargh.sandbox.kt.mvn.concurrency.SelectChannel
import de.richargh.sandbox.kt.mvn.concurrency.buzz
import de.richargh.sandbox.kt.mvn.concurrency.fizz
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class SelectChannelTest {
    
    @ExperimentalCoroutinesApi
    @Test
    fun `fooo`() {
        // arrange
        runBlocking {
            val fizz = fizz()
            val buzz = buzz()
            repeat(7) {
                SelectChannel().selectFizzBuzz(fizz, buzz)
            }
            coroutineContext.cancelChildren() // cancel fizz & buzz coroutines
        }
    
        // act
    
    
        // assert
        
    }
}