package org.nova.core

import java.io.File

class MetaInfo(
    val name: String,
    val content: String
)

class Config(val sourceCode: List<File>, val metaInfo: List<MetaInfo>)
