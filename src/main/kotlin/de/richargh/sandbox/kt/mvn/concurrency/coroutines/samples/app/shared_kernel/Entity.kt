package de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.shared_kernel

interface Entity<T> where T: EntityId {
    val id: T

    infix fun hasSameIdAs(other: Entity<T>): Boolean = id == other.id
}

interface EntityId