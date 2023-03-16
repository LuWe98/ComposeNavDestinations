package com.welu.compose_nav_destinations_ksp.annotations

import com.welu.compose_nav_destinations_ksp.model.ImportInfo
import com.welu.compose_nav_destinations_ksp.utils.ImportUtils

sealed class AnnotationDeclaration(
    val name: String,
    val import: ImportInfo = ImportInfo(name, ImportUtils.ANNOTATIONS_PACKAGE)
)