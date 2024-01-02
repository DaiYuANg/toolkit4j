package org.toolkit4j.dispatcher

import java.util.concurrent.CopyOnWriteArraySet
import kotlin.reflect.KClass

class SimpleTransactionManager
@JvmOverloads constructor(
    private val steps: MutableSet<Runnable> = CopyOnWriteArraySet(),
    private val rollback: Runnable,
) : MutableSet<Runnable> by steps {

    fun run() {
        try {
            for (step in steps) {
                step.run()
            }
        } catch (e: Exception) {
            // Exception caught, perform rollback
            rollback.run()
            throw e // Re-throw the exception after rollback
        }
    }

    fun <T : Throwable> run(catchType: KClass<T>? = null) {
        try {
            steps.forEach { it.run() }
            println("Transaction succeeded")
        } catch (e: Throwable) {
            if (catchType == null || catchType.isInstance(e)) {
                rollback.run()
            }
            throw e
        }
    }
}