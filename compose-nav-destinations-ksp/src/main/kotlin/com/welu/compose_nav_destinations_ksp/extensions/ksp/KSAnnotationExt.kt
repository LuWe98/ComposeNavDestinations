package com.welu.compose_nav_destinations_ksp.extensions.ksp

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSValueArgument
import com.welu.compose_nav_destinations_ksp.model.ImportInfo

fun KSAnnotation.getValueArgument(argName: String): KSValueArgument? = arguments.firstOrNull { it.name?.asString() == argName }

fun KSAnnotation.requireValueArgument(argName: String): KSValueArgument = getValueArgument(argName) ?: throw IllegalStateException()

fun KSAnnotated.hasAnnotation(annotationDeclaration: com.welu.compose_nav_destinations_ksp.annotations.AnnotationDeclaration): Boolean = hasAnnotation(annotationDeclaration.import)

fun KSAnnotated.hasAnnotation(importInfo: ImportInfo): Boolean = hasAnnotation(importInfo.simpleName, importInfo.qualifiedName)

fun KSAnnotated.hasAnnotation(annotationDeclaration: KSClassDeclaration): Boolean = hasAnnotation(annotationDeclaration.qualifiedName!!.asString())

fun KSAnnotated.hasAnnotation(qualifiedName: String): Boolean = hasAnnotation(qualifiedName.substring(qualifiedName.lastIndexOf(".") + 1), qualifiedName)

fun KSAnnotated.hasAnnotation(simpleName: String, qualifiedName: String): Boolean = annotations.any {
    it.shortName.asString() == simpleName && it.annotationType.resolve().declaration.qualifiedName?.asString() == qualifiedName
}


fun KSAnnotated.hasAnnotationSimple(importInfo: ImportInfo): Boolean = hasAnnotationSimple(importInfo.simpleName)

fun KSAnnotated.hasAnnotationSimple(simpleName: String): Boolean = annotations.any { it.shortName.asString() == simpleName }
