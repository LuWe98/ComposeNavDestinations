package com.welu.composenavdestinations.generation

interface FileGenerator <T> {
    fun generate(instance: T)
}