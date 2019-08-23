package de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.shared_kernel

import kotlinx.coroutines.Dispatchers

class LocalNotifierTest: NotifierContract(
        LocalNotifier(Dispatchers.Default))