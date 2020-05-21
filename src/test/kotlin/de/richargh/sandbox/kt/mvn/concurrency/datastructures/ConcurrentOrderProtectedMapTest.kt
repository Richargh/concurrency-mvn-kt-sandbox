package de.richargh.sandbox.kt.mvn.concurrency.datastructures

import org.apache.commons.lang3.time.StopWatch
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestReporter
import java.util.concurrent.ConcurrentHashMap

internal class ConcurrentOrderProtectedMapTest{
    @Test
    fun `should be filled because the adds have a higher version number`(testReporter: TestReporter) {
        // arrange
        val count = 100_000L
        val stopWatch = StopWatch()
        val testling = ConcurrentOrderProtectedMap<UserId, User>()
        val threadNames = ConcurrentHashMap<String, Unit>()
        val addVersion = Version(2)
        val deleteVersion = Version(1)

        stopWatch.start()
        (1..count).toList().shuffled().parallelStream().forEach {
            val threadName = Thread.currentThread().name
            threadNames[threadName] = Unit
            val userToAdd = User(UserId("$it"), Name(threadName), addVersion)
            val userToDelete = User(UserId("${count - it}"), Name(threadName), deleteVersion)

            // double act
            testling.put(userToDelete.userId, userToAdd)
            testling.remove(UserId("${count - it}"), userToDelete)
        }
        stopWatch.stop()

        // report
        testReporter.publishEntry("It took ${stopWatch.time} ms")
        testReporter.publishEntry("We executed on these threads ${threadNames.keys().toList().joinToString(", ")}")

        // assert
        Assertions.assertThat(testling.count()).isEqualTo(count)
    }

    @Test
    fun `should be empty because the deletes have a higher version number`(testReporter: TestReporter) {
        // arrange
        val count = 100_000L
        val stopWatch = StopWatch()
        val testling = ConcurrentOrderProtectedMap<UserId, User>()
        val threadNames = ConcurrentHashMap<String, Unit>()
        val addVersion = Version(1)
        val deleteVersion = Version(2)

        stopWatch.start()
        (1..count).toList().shuffled().parallelStream().forEach {
            val threadName = Thread.currentThread().name
            threadNames[threadName] = Unit
            val userToAdd = User(UserId("$it"), Name(threadName), addVersion)
            val userToDelete = User(UserId("${count - it}"), Name(threadName), deleteVersion)

            // double act
            testling.put(userToDelete.userId, userToAdd)
            testling.remove(UserId("${count - it}"), userToDelete)
        }
        stopWatch.stop()

        // report
        testReporter.publishEntry("It took ${stopWatch.time} ms")
        testReporter.publishEntry("We executed on these threads ${threadNames.keys().toList().joinToString(", ")}")

        // assert
        Assertions.assertThat(testling.count()).isEqualTo(0)
    }

    private data class UserId(val rawValue: String)
    private data class Name(val rawValue: String)
    private class User(val userId: UserId, val name: Name, override val version: Version): Versionable
}