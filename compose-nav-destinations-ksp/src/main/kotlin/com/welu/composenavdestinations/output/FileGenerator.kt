package com.welu.composenavdestinations.output

interface FileGenerator <T> {
    fun generate(instance: T)
}