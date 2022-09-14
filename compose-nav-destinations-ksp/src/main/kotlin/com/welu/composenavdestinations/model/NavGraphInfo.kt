package com.welu.composenavdestinations.model

//TODO -> Noch machen
data class NavGraphInfo(
    //Die einzigartige Route des NavGraphs
    val route: String,
    //Der eigene Import der Klasse -> Enthält den Namen und Package Name
    val navGraphSpecImport: ImportInfo,
    //Der Import der StartComponent
    val startComponentDeclaration: ImportInfo,
    //Der Import des ParentNavGraphSpecs -> wenn es null ist, dann ist der NavGraph ne root
    val parentNavGraphSpecImport: ImportInfo? = null,
    //Die Child NavGraphSpecImports dieses NavGraphs
    val childNavGraphSpecImports: List<ImportInfo> = emptyList(),
    //Die Child NavDestinationSpecImports dieses NavGraphs
    val childNavDestinationSpecImports: List<ImportInfo> = emptyList(),
    //Das sind die Deep links
    val deepLinks: List<String> = emptyList(),
    //Das sind die extrahierten NavArgInfos, wie sie auch bei NavDestinationInfo verwendet werden
    val navArgsInfo: NavArgsInfo? = null
)