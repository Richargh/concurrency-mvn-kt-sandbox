package de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.shared_kernel.LocalPublisher

class LocalPublisherTest: PublisherContract(
        LocalPublisher())