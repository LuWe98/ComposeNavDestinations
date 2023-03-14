package com.welu.compose_nav_destinations_ksp.generation

import com.squareup.kotlinpoet.FileSpec

interface FileSpecGenerator <Input> {
    fun generate(input: Input): FileSpec?
}