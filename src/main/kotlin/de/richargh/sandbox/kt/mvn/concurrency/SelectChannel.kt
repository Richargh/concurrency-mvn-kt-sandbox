package de.richargh.sandbox.kt.mvn.concurrency

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.selects.select

class SelectChannel {
    suspend fun selectFizzBuzz(fizz: ReceiveChannel<String>, buzz: ReceiveChannel<String>) {
        select<Unit> { // <Unit> means that this select expression does not produce any result
            fizz.onReceive { value ->  // this is the first select clause
                println("fizz -> '$value'")
            }
            buzz.onReceive { value ->  // this is the second select clause
                println("buzz -> '$value'")
            }
        }
    }
}

fun CoroutineScope.fizz() = produce<String> {
    while (true) { // sends "Fizz" every 300 ms
        delay(300)
        send("Fizz")
    }
}

fun CoroutineScope.buzz() = produce<String> {
    while (true) { // sends "Buzz!" every 500 ms
        delay(500)
        send("Buzz!")
    }
}