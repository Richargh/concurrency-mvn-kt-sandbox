package de.richargh.sandbox.kt.mvn.concurrency.datastructures

import java.util.concurrent.ConcurrentHashMap

class ConcurrentOrderProtectedMap<K: Any, V: Versionable>(
        private val changeListeners: List<ChangeListener<K, V>> = emptyList()): VersionCheckingMap<K, V> {
    private val entries = ConcurrentHashMap<K, V>()
    private val deletes = ConcurrentHashMap<K, Version>()

    override fun put(key: K, value: V) {
        entries.putIfNewer(key, value) { old, new ->
            changeListeners.forEach { it.beforePutChange(key, old, new) }
        }
    }

    override fun remove(key: K, value: V) {
        entries.removeIfNewer(key, value.version) {
            deletes.putIfNewer(key, value.version)
            changeListeners.forEach { it.beforeRemove(key, value) }
        }
    }

    override fun findById(id: K): V? {
        return entries[id]
    }

    override fun count() = entries.mappingCount()

    private fun ConcurrentHashMap<K, V>.putIfNewer(key: K, value: V, beforePut: (V?, V) -> Unit) =
            compute(key) { _, existing: V? ->
                when {
                    (existing == null || existing.version < value.version)
                    && isDeletedVersionSmaller(key, value) -> {
                        beforePut(existing, value)
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

    fun ConcurrentHashMap<K, Version>.putIfNewer(key: K, version: Version) =
            compute(key) { _, existingVersion: Version? ->
                when {
                    existingVersion == null || existingVersion < version -> version
                    else                                                 -> existingVersion
                }
            }


}