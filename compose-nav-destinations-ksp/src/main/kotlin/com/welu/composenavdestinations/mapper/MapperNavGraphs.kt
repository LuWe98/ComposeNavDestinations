package com.welu.composenavdestinations.mapper

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.welu.composenavdestinations.model.NavGraphInfo

//TODO -> UM die NavGraphs zu erstellen
class MapperNavGraphs(
    private val resolver: Resolver,
    private val logger: KSPLogger
) : ComponentsMapper<KSClassDeclaration, NavGraphInfo> {

    override fun map(declaration: KSClassDeclaration): NavGraphInfo {
        TODO("Not yet implemented")
    }

}