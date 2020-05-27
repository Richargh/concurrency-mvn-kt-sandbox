package de.richargh.sandbox.kt.mvn.concurrency.datastructures

import java.lang.RuntimeException
import kotlin.reflect.KClass

class MultiIndexConcurrentMap<K: Any, V: Versionable>(private val indices: List<Index<K, V>>): VersionCheckingMap<K, V> {

    private val versionCheckingMap: VersionCheckingMap<K, V> = ConcurrentOrderProtectedMap(indices)

    override fun put(key: K, value: V) = versionCheckingMap.put(key, value)

    override fun remove(key: K, value: V) = versionCheckingMap.remove(key, value)

    override fun findById(id: K): V? = versionCheckingMap.findById(id)

    fun findWithIndex(otherKey: Any): V? {
        val index = indexFor(otherKey::class)
        val key = index.resolveKey(otherKey)
        val value = key?.let(::findById)
        // check if it matches the predicate
        return value
    }

    private fun indexFor(keyType: KClass<out Any>): Index<K, V> {
        return indices.firstOrNull { it.responsibleForKey(keyType) }
               ?: throw RuntimeException("No Index responsible for $keyType")
    }

    override fun count(): Long = versionCheckingMap.count()
}