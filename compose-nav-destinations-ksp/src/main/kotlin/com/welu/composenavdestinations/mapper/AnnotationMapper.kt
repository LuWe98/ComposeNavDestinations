package com.welu.composenavdestinations.mapper

import com.google.devtools.ksp.symbol.KSClassDeclaration

interface AnnotationMapper<T> {

    fun map(declaration: KSClassDeclaration): T

}
