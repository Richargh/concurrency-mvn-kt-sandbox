package de.richargh.sandbox.kt.mvn.concurrency.datastructures

import org.apache.commons.lang3.time.StopWatch
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestReporter
import java.util.concurrent.ConcurrentHashMap

internal class ConcurrentMultiValuedMapTest{
    @Test
    fun `should be less than full due to the deletes`(testReporter: TestReporter) {
        // arrange
        val count = 100_000L
        val stopWatch = StopWatch()
        val testling = ConcurrentMultiValuedMap<UserId, User>()
        val threadNames = ConcurrentHashMap<String, Unit>()
        val addVersion = Version(2)
        val deleteVersion = Version(1)

        stopWatch.start()
        (1..count).toList().shuffled().parallelStream().forEach {
            val threadName = Thread.currentThread().name
            threadNames[threadName] = Unit
            val userToAdd = User(UserId("$it"), Name(threadName))
            val userToDelete = User(UserId("${count - it + 1}"), Name(threadName))

            // double act
            testling.put(userToAdd.userId, userToAdd)
            testling.removeMapping(userToDelete.userId, userToDelete)
        }
        stopWatch.stop()

        // report
        testReporter.publishEntry("It took ${stopWatch.time} ms")
        testReporter.publishEntry("We executed on these threads ${threadNames.keys().toList().joinToString(", ")}")

        // assert
        assertThat(testling.count()).isLessThan(count)
    }

    private data class UserId(val rawValue: String)
    private data class Name(val rawValue: String)
    private data class User(val userId: UserId, val name: Name)
}