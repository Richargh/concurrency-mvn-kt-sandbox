package de.richargh.sandbox.kt.mvn.concurrency.datastructures

import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

class SingleResultIndex<PrimaryK: Any, IndexKey: Any, V: Versionable>(
        private val otherKey: KClass<IndexKey>,
        private val indexKeyOf: (V) -> IndexKey): Index<PrimaryK, V> {

    private val entries = ConcurrentHashMap<IndexKey, VersionedKey<PrimaryK>>()

    override fun responsibleForKey(any: KClass<out Any>): Boolean {
        return any.isSubclassOf(otherKey)
    }

    override fun resolveKey(otherK: Any): PrimaryK? {
        return entries[otherK]?.key
    }

    override fun resolveKeys(otherK: Any): List<PrimaryK>? {
        return emptyList()
    }

    override fun beforePutChange(key: PrimaryK, oldValue: V?, newValue: V) {
        val oldIndex = oldValue?.let(indexKeyOf)
        val newIndex = indexKeyOf(newValue)

        if (oldIndex != null)
            entries.removeIfNewer(oldIndex, newValue.version) {}
        entries.putIfNewer(newIndex, VersionedKey(key, newValue.version)) {}
    }

    override fun beforeRemove(key: PrimaryK, oldValue: V?) {
        val oldIndex = oldValue?.let(indexKeyOf)
        if(oldIndex != null)
            entries.removeIfNewer(oldIndex, oldValue.version) {}
    }
}