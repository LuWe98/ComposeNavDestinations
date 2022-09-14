package com.welu.composenavdestinations.model.structure

import com.google.devtools.ksp.symbol.KSClassDeclaration

data class RawNavGraphInfo(
    override val classDeclaration: KSClassDeclaration,
    //Die einzigartige Route des NavGraphs
    val route: String,
    //Ob dieser NavGraph der Start eines anderen NavGraphs ist
    override val isStart: Boolean,
    //Ob der NavGraph der default NavGraph ist
    val isDefaultNavGraph: Boolean,
    //Der Import der StartComponent -> Ist nullable weil es erst Später beim Mapping gesetzt werden kann. Ist aber beim Aufruf nie null
    val startComponentDeclaration: KSClassDeclaration? = null,
    //Der Import des ParentNavGraphSpecs -> wenn es null ist, dann ist der NavGraph ne root
    val parentNavGraphSpecDeclaration: KSClassDeclaration? = null,
    //Die Child NavGraphSpecImports dieses NavGraphs
    val childNavGraphSpecDeclarations: Sequence<KSClassDeclaration> = emptySequence(),
    //Die Child NavDestinationSpecImports dieses NavGraphs
    val childNavDestinationSpecDeclarations: Sequence<KSClassDeclaration> = emptySequence()
): RawComponentInfo {

    val simpleName get() = classDeclaration.simpleName.asString()
}
