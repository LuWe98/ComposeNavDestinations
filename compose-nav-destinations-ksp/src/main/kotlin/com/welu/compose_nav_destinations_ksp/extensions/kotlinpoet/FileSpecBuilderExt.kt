package com.welu.compose_nav_destinations_ksp.extensions.kotlinpoet

import com.squareup.kotlinpoet.FileSpec
import com.welu.compose_nav_destinations_ksp.extensions.toClassName
import com.welu.compose_nav_destinations_ksp.model.ImportInfo

fun FileSpec.Companion.builder(importInfo: ImportInfo) = builder(
    packageName = importInfo.packageDir,
    fileName = importInfo.simpleName
)

fun FileSpec.Companion.build(importInfo: ImportInfo, action: FileSpec.Builder.() -> Unit): FileSpec {
    val builder = builder(
        packageName = importInfo.packageDir,
        fileName = importInfo.simpleName
    )
    builder.apply(action)
    return builder.build()
}

fun FileSpec.Builder.addImports(importInfos: Sequence<ImportInfo>) {
    importInfos.forEach {
        addImport(it.packageDir, it.simpleName)
    }
}

fun FileSpec.Builder.addImports(importInfos: Collection<ImportInfo>) {
    importInfos.forEach(::addImport)
}

fun FileSpec.Builder.addImport(importInfo: ImportInfo) {
    if(importInfo.hasAlias) {
        addAliasedImport(importInfo.toClassName(), importInfo.importedAs!!)
        return
    }
    addImport(importInfo.packageDir, importInfo.simpleName)
}