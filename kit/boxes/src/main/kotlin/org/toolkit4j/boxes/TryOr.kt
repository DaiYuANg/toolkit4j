package org.toolkit4j.boxes

class TryOr<T, E : Throwable>(private val block: () -> T) {
    private var result: T? = null
    private var error: E? = null

    fun execute(): TryOr<T, E> {
        try {
            result = block()
        } catch (e: Throwable) {
            @Suppress("UNCHECKED_CAST")
            error = e as E
        }
        return this
    }

    fun getResult(): T? {
        return result
    }

    fun getError(): E? {
        return error
    }

    fun isSuccess(): Boolean {
        return result != null
    }

    fun isFailure(): Boolean {
        return error != null
    }
}