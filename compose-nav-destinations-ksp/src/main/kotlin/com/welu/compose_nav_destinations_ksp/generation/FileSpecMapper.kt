package com.welu.compose_nav_destinations_ksp.generation

import com.squareup.kotlinpoet.FileSpec

/**
 * Maps an [Input] to a [FileSpec]
 */
interface FileSpecMapper <Input> {
    fun generate(input: Input): FileSpec?
}