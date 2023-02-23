package com.welu.compose_nav_destinations_ksp.generation

import com.welu.compose_nav_destinations_ksp.model.FileContentInfo

interface FileContentInfoTypedGenerator <T> {
    fun generate(instance: T) : FileContentInfo?
}