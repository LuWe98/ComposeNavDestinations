package com.welu.composenavdestinations.generation

import com.welu.composenavdestinations.model.FileContentInfo

interface FileContentInfoGenerator <T> {
    fun generate(instance: T) : FileContentInfo?
}