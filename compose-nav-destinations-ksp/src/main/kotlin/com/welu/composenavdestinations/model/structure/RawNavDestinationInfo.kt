package com.welu.composenavdestinations.model.structure

import com.google.devtools.ksp.symbol.KSClassDeclaration

//TODO -> Hier noch route mit einbauen. Dass dann auch in das RawComponentInfo Interface mit einbauen
// -> Dann auch schon validieren ob es einzigartige Routen zwischen NavDestinations und NavGraphs gibt.
// -> Je früher desto besser, da sonst unnötig gearbeitet wird
data class RawNavDestinationInfo(
    override val isStart: Boolean = false,
    override val classDeclaration: KSClassDeclaration,
    val parentNavGraph: KSClassDeclaration
): RawComponentInfo {
    val simpleName get() = classDeclaration.simpleName.asString()
}
