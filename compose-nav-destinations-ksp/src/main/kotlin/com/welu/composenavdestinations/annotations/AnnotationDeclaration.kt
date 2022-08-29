package com.welu.composenavdestinations.annotations

import com.welu.composenavdestinations.model.ImportInfo
import com.welu.composenavdestinations.utils.PackageUtils

sealed class AnnotationDeclaration(
    val name: String,
    val import: ImportInfo =  ImportInfo(name,"${PackageUtils.PACKAGE_NAME}.annotations")
)