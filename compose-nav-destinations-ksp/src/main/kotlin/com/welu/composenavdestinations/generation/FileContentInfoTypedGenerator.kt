package com.welu.composenavdestinations.generation

import com.welu.composenavdestinations.model.FileContentInfo

interface FileContentInfoTypedGenerator <T> {
    fun generate(instance: T) : FileContentInfo?
}