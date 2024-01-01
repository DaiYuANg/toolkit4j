package org.toolkit4j.dispatcher

import java.util.UUID
import java.util.function.Supplier

data class Task @JvmOverloads constructor(
    val taskName: String = UUID.randomUUID().toString(),
    val action: Runnable,
    val tryLock: Supplier<Boolean>
)

