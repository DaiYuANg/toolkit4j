package org.toolkit4j.ktslf4j

import org.slf4j.LoggerFactory

interface Slf4jInterface {
    fun logger() = LoggerFactory.getLogger(this.javaClass)!!
}