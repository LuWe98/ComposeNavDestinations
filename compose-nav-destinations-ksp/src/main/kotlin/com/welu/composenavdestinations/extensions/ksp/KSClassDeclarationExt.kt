package com.welu.composenavdestinations.extensions.ksp

import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.validate
import com.welu.composenavdestinations.annotationinfo.AnnotationDeclaration

fun KSClassDeclaration.getAnnotationWith(annotationName: String): KSAnnotation? = annotations.firstOrNull {
    it.shortName.asString() == annotationName
}

fun KSClassDeclaration.getAnnotationWith(annotationDeclaration: AnnotationDeclaration): KSAnnotation? =
    getAnnotationWith(annotationDeclaration.name)

fun KSClassDeclaration.requireAnnotationWith(annotationName: String): KSAnnotation = getAnnotationWith(annotationName) ?: throw IllegalStateException()

fun KSClassDeclaration.requireAnnotationWith(annotationDeclaration: AnnotationDeclaration): KSAnnotation =
    getAnnotationWith(annotationDeclaration) ?: throw IllegalStateException()

val KSClassDeclaration.validProperties get() = getAllProperties().filter(KSPropertyDeclaration::validate)

inline fun <reified T> KSClassDeclaration.getAnnotationArgument(argName: String, annotation: AnnotationDeclaration): T =
    requireAnnotationWith(annotation).requireValueArgument(argName).valueAs()

val KSClassDeclaration.asType get() = asType(emptyList())

val KSClassDeclaration?.isEnum get() = this?.classKind == ClassKind.ENUM_CLASS

