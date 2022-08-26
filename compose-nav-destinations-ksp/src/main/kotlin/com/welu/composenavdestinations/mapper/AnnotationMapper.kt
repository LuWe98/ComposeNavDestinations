package com.welu.composenavdestinations.mapper

import com.google.devtools.ksp.symbol.KSDeclaration

interface AnnotationMapper<D: KSDeclaration, T> {

    fun map(declaration: D): T

}
