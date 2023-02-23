package com.welu.compose_nav_destinations_ksp.generation

import com.welu.compose_nav_destinations_ksp.model.FileContentInfo

interface FileContentInfoGenerator {
    fun generate() : FileContentInfo?
}