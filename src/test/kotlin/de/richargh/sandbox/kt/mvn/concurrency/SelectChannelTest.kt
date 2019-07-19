package de.richargh.sandbox.kt.mvn.concurrency

import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class SelectChannelTest {
    
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