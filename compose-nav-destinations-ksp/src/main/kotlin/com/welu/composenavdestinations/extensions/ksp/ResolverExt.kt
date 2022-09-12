package com.welu.composenavdestinations.extensions.ksp

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getClassDeclarationByName
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.validate
import com.welu.composenavdestinations.model.ImportInfo
import kotlin.reflect.KClass

@OptIn(KspExperimental::class)
fun Resolver.getValidDeclarationsOfPackage(packageName: String): Sequence<KSDeclaration> {
    return getDeclarationsFromPackage(packageName).filter(KSDeclaration::validate)
}

fun Resolver.isImportNameContainedInPackage(packageName: String, importName: String): Boolean = runCatching {
    getValidDeclarationsOfPackage(packageName).firstOrNull {
        it.simpleName.asString() == importName
    }?.let(KSDeclaration::isAccessible) ?: false
}.getOrDefault(false)

fun Resolver.isImportNameContainedInPackage(importInfo: ImportInfo, importName: String) = isImportNameContainedInPackage(importInfo.packageDir, importName)

fun Resolver.getTypeWithClassName(name: String) = getClassDeclarationByName(name)!!.asType

fun Resolver.getTypeWithImportInfo(importInfo: ImportInfo) = getClassDeclarationByName(importInfo.qualifiedName)!!.asType

fun Resolver.getTypeWithClass(clazz: KClass<*>) = getTypeWithClassName(clazz.qualifiedName!!)

fun Resolver.getStarProjectedTypeWithClass(clazz: KClass<*>) = getClassDeclarationByName(clazz.qualifiedName!!)!!.asStarProjectedType()


//fun Resolver.getClassDeclarationTypeByName(clazz: Class<*>) = getClassDeclarationTypeByName(clazz.qualifiedName!!)