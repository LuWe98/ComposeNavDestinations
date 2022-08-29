package com.welu.composenavdestinations.extensions.ksp

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import com.google.devtools.ksp.validate
import com.welu.composenavdestinations.annotations.AnnotationDeclaration

val KSFunctionDeclaration.validParameters get() = parameters.filter(KSValueParameter::validate)

inline fun <reified T> KSFunctionDeclaration.getAnnotationArgument(argName: String, annotation: AnnotationDeclaration): T =
    requireAnnotationWith(annotation).requireValueArgument(argName).valueAs()

fun KSFunctionDeclaration.requireAnnotationWith(annotationDeclaration: AnnotationDeclaration): KSAnnotation =
    getAnnotationWith(annotationDeclaration) ?: throw IllegalStateException()

fun KSFunctionDeclaration.getAnnotationWith(annotationDeclaration: AnnotationDeclaration): KSAnnotation? =
    getAnnotationWith(annotationDeclaration.name)

fun KSFunctionDeclaration.getAnnotationWith(annotationName: String): KSAnnotation? = annotations.firstOrNull {
    it.shortName.asString() == annotationName
}