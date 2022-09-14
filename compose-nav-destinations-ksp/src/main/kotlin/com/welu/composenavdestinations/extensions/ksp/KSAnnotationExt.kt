package com.welu.composenavdestinations.extensions.ksp

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSValueArgument
import com.welu.composenavdestinations.model.ImportInfo

fun KSAnnotation.getValueArgument(argName: String): KSValueArgument? = arguments.firstOrNull { it.name?.asString() == argName }

fun KSAnnotation.requireValueArgument(argName: String): KSValueArgument = getValueArgument(argName) ?: throw IllegalStateException()

fun KSAnnotated.isAnnotationPresent(importInfo: ImportInfo): Boolean = isAnnotationPresent(importInfo.simpleName, importInfo.qualifiedName)

fun KSAnnotated.isAnnotationPresent(annotationDeclaration: KSClassDeclaration): Boolean = isAnnotationPresent(annotationDeclaration.qualifiedName!!.asString())

fun KSAnnotated.isAnnotationPresent(qualifiedName: String): Boolean = isAnnotationPresent(qualifiedName.substring(qualifiedName.lastIndexOf(".") + 1), qualifiedName)

fun KSAnnotated.isAnnotationPresent(simpleName: String, qualifiedName: String): Boolean = this.annotations.any {
    it.shortName.asString() == simpleName && it.annotationType.resolve().declaration.qualifiedName?.asString() == qualifiedName
}


fun KSAnnotated.isAnnotationPresentSimple(importInfo: ImportInfo): Boolean = isAnnotationPresentSimple(importInfo.simpleName)

fun KSAnnotated.isAnnotationPresentSimple(simpleName: String): Boolean = this.annotations.any { it.shortName.asString() == simpleName }
