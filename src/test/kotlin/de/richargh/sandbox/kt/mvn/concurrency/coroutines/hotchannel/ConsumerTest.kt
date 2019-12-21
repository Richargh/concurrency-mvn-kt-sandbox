package de.richargh.sandbox.kt.mvn.concurrency.coroutines.hotchannel

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.test_helper.testBlocking
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ConsumerTest {

    @Test
    fun `can consume all data from a channel`() =
            testBlocking {
                fun CoroutineScope.produceGreeting(): ReceiveChannel<String> = produce {
                    listOf("Hello", "World", "Channel").forEach {
                        delay(100)
                        send(it)
                    }
                }
                // arrange
                val expected = "HelloWorldChannel"

                // act
                var message = ""
                for (word in produceGreeting()) {
                    message += word
                }

                // assert
                assertThat(message).isEqualTo(expected)
            }
}

