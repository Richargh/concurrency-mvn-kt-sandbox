package de.richargh.sandbox.kt.mvn.concurrency.coroutines.intergration

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.integration.await
import de.richargh.sandbox.kt.mvn.concurrency.coroutines.test_helper.testBlocking
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.fail

class IntegrationTest {
    @Test
    fun `can bridge to result of non-coroutine code`() = testBlocking {
        // arrange
        val data = "data"
        val fakeCall = FakeCall(data)

        // act
        val result = fakeCall.await()

        // assert
        assertThat(result).isEqualTo(data)
    }

    @Test
    fun `can bridge to exception of non-coroutine code`() = testBlocking {
        // arrange
        val fakeCall = FakeCall(null)

        // act
        try {
            fakeCall.await()
            fail("This code should never be reached because FakeCall(null) throws an exception")
        }catch (t: Throwable){
            // assert
            assertThat(t).isInstanceOf(InterruptedException::class.java)
        }
    }
}
