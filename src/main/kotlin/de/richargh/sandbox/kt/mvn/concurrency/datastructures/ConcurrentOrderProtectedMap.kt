package de.richargh.sandbox.kt.mvn.concurrency.datastructures

import java.util.concurrent.ConcurrentHashMap

class ConcurrentOrderProtectedMap<K, V: Versionable> {
    private val entries = ConcurrentHashMap<K, V>()
    private val deletes = ConcurrentHashMap<K, Version>()

    fun put(key: K, value: V) {
        // returning null removes the entry
        entries.putIfNewer(key, value) { }
    }

    fun remove(key: K, value: V) {
        // returning null removes the entry
        entries.removeIfNewer(key, value) {
            deletes.putIfNewer(key, value.version)
        }
    }

    fun findById(id: K): V? {
        return entries[id]
    }

    fun count() = entries.mappingCount()

    private fun ConcurrentHashMap<K, V>.putIfNewer(key: K, value: V, beforePut: (V) -> Unit) =
            compute(key) { _, existing: V? ->
                when {
                    (existing == null || existing.version < value.version)
                    && isDeletedVersionSmaller(key, value) -> {
                        beforePut(value)
                        value
                    }
                    else                                                 -> {
                        existing
                    }
                }
            }

    private fun isDeletedVersionSmaller(key: K, value: V): Boolean {
        val lastDeletedVersion = deletes[key]
        return lastDeletedVersion == null || lastDeletedVersion < value.version
    }

    private fun ConcurrentHashMap<K, Version>.putIfNewer(key: K, version: Version) =
            compute(key) { _, existingVersion: Version? ->
                when {
                    existingVersion == null || existingVersion < version -> version
                    else                                                 -> existingVersion
                }
            }

    private fun ConcurrentHashMap<K, V>.removeIfNewer(key: K, value: V, beforeRemove: (V?) -> Unit) =
            compute(key) { _, existing: V? ->
                when {
                    existing == null || existing.version < value.version -> {
                        beforeRemove(existing)
                        null
                    }
                    else                                                 -> {
                        existing
                    }
                }
            }
}