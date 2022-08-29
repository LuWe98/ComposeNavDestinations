package com.welu.composenavdestinations.generation

interface FileContentGenerator <T> {
    fun generate(instance: T)
}