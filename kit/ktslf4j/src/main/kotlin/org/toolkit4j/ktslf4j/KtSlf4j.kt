package org.toolkit4j.ktslf4j

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class KtSlf4j {
    companion object{
        val <reified T> T.log: KLogger
            inline get() = KotlinLogging.logger{T::class.java.name}
    }
}
