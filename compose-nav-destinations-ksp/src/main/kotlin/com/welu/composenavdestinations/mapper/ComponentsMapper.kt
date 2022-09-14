package com.welu.composenavdestinations.mapper

import com.google.devtools.ksp.symbol.KSDeclaration

interface ComponentsMapper<D: KSDeclaration, T> {

    fun map(declaration: D): T

}
