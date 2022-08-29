package com.welu.composenavdestinations.extensions.ksp

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSValueArgument
import com.welu.composenavdestinations.model.ImportInfo

fun KSAnnotation.getValueArgument(argName: String): KSValueArgument? = arguments.firstOrNull {
    it.name?.asString() == argName
}

fun KSAnnotation.requireValueArgument(argName: String): KSValueArgument =
    getValueArgument(argName) ?: throw IllegalStateException()

fun KSAnnotated.isAnnotationPresent(importInfo: ImportInfo): Boolean = this.annotations.any {
    it.shortName.getShortName() == importInfo.simpleName
            && it.annotationType.resolve().declaration.qualifiedName?.asString() == importInfo.qualifiedName
}
