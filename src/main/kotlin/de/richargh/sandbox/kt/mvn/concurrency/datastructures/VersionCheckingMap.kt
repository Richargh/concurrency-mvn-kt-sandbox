package de.richargh.sandbox.kt.mvn.concurrency.datastructures

interface VersionCheckingMap<K: Any, V: Versionable> {
    fun put(key: K, value: V)
    fun remove(key: K, value: V)
    fun findById(id: K): V?
    fun count(): Long
}