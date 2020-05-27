package de.richargh.sandbox.kt.mvn.concurrency.datastructures

import org.apache.commons.lang3.time.StopWatch
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestReporter
import java.util.concurrent.ConcurrentHashMap

class MultiIndexConcurrentMapTest {

    @Test
    fun `should be able to find all the entries via index`(testReporter: TestReporter) {
        // arrange
        val count = 100_000L
        val stopWatch = StopWatch()
        val testling = MultiIndexConcurrentMap<UserId, User>(
                listOf(SingleResultIndex(AnotherCode::class) { it.anotherCode }))
        val threadNames = ConcurrentHashMap<String, Unit>()
        val initialVersion = Version(1)

        stopWatch.start()
        (1..count).toList().shuffled().parallelStream().forEach {
            val threadName = Thread.currentThread().name
            threadNames[threadName] = Unit
            val userToAdd = User(UserId("$it"), AnotherCode(it), initialVersion)

            testling.put(userToAdd.userId, userToAdd)
        }
        stopWatch.stop()

        // report
        testReporter.publishEntry("It took ${stopWatch.time} ms")
        testReporter.publishEntry("We executed on these threads ${threadNames.keys().toList().joinToString(", ")}")

        // act
        val result = (1..count).mapNotNull { testling.findWithIndex(AnotherCode(it)) }

        // assert
        assertThat(result).hasSize(count.toInt())
    }

    @Test
    fun `should be able to `(testReporter: TestReporter) {
        // arrange
        val count = 100_000L
        val stopWatch = StopWatch()
        val testling = MultiIndexConcurrentMap<UserId, User>(
                listOf(SingleResultIndex(AnotherCode::class) { it.anotherCode }))
        val initialVersion = Version(0)
        val defaultCode = AnotherCode(-1)
        val specialCode = AnotherCode(1)

        stopWatch.start()
        (1..count).toList().parallelStream().forEach {
            val userWithDefaultCode = User(UserId("$it"), defaultCode, initialVersion)
            testling.put(userWithDefaultCode.userId, userWithDefaultCode)
        }
        val threadNames = ConcurrentHashMap<String, Unit>()
        (1..(count - 1)).toList().shuffled().parallelStream().forEach {
            val threadName = Thread.currentThread().name
            threadNames[threadName] = Unit
            // basically we want the special code to move from user to user, but only one user should have it
            val userWithDefaultCode = User(UserId("$it"), defaultCode, Version(it))
            val userWithSpecialCode = User(UserId("${it + 1}"), specialCode, Version(it))

            testling.put(userWithDefaultCode.userId, userWithDefaultCode)
            testling.put(userWithSpecialCode.userId, userWithSpecialCode)
        }
        stopWatch.stop()

        // report
        testReporter.publishEntry("It took ${stopWatch.time} ms")
        testReporter.publishEntry("We executed on these threads ${threadNames.keys().toList().joinToString(", ")}")

        // act
        val result = testling.findWithIndex(specialCode)

        // assert
        assertThat(result).isEqualTo(testling.findById(UserId("$count")))
    }

    private data class UserId(val rawValue: String)
    private data class AnotherCode(val rawValue: Long)
    private class User(val userId: UserId, val anotherCode: AnotherCode, override val version: Version): Versionable
}