package com.welu.composenavdestinations.generation

import com.welu.composenavdestinations.model.FileContentInfo

interface FileContentInfoGenerator {
    fun generate() : FileContentInfo?
}