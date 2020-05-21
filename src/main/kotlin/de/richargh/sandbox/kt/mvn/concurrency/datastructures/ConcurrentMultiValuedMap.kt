package de.richargh.sandbox.kt.mvn.concurrency.datastructures

import java.util.concurrent.ConcurrentHashMap

class ConcurrentMultiValuedMap<K, V> {
    private val entries = ConcurrentHashMap<K, MutableList<V>>()

    fun put(key: K, value: V) {
        entries.compute(key) { _, existing: MutableList<V>? ->
            when (existing) {
                null -> mutableListOf(value)
                else -> existing.apply { add(value) }
            }
        }
    }

    fun removeMapping(key: K, value: V) {
        entries.compute(key) { _, existing: MutableList<V>? ->
            when (existing) {
                null -> null
                else -> {
                    existing.remove(value)
                    if(existing.size == 0) null
                    else existing
                }
            }
        }
    }

    fun findById(id: K): Collection<V> {
        return entries[id] ?: emptyList()
    }

    fun count() = entries.mappingCount()

}