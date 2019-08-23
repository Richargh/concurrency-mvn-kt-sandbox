package de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.catalogue

import com.natpryce.hamkrest.ValueDescription

class BookDescription : ValueDescription {
    override fun describe(v: Any?) =
            when (v) {
                is Book -> "Book[${v.title}]"
                else -> null
            }
}