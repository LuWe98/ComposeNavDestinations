package com.welu.compose_nav_destinations_ksp.annotations

import com.welu.compose_nav_destinations_ksp.model.ImportInfo
import com.welu.compose_nav_destinations_ksp.utils.PackageUtils

sealed class AnnotationDeclaration(
    val name: String,
    val import: ImportInfo = ImportInfo(name, PackageUtils.ANNOTATIONS_PACKAGE)
)