package org.toolkit4j.boxes

@JvmInline
value class StringContainer private constructor(private val value: CharSequence) : CharSequence by value {

    fun count() {
        value.count()
    }

    companion object {
        fun of(value: CharSequence?): StringContainer {
            return StringContainer(value?.toString() ?: "")
        }

        fun empty(): StringContainer {
            return StringContainer("")
        }
    }
}