package com.welu.composenavdestinations.extensions.ksp

import com.google.devtools.ksp.isInternal
import com.google.devtools.ksp.isPublic
import com.google.devtools.ksp.symbol.FileLocation
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFile

val KSDeclaration.rootKSFile get(): KSFile = containingFile ?: parentDeclaration?.rootKSFile ?: throw IllegalStateException()

val KSDeclaration.lineNumber get(): Int = (location as FileLocation).lineNumber - 1

val KSDeclaration.isAccessible get() = isPublic() || isInternal()

fun Sequence<KSDeclaration>.forEachWithIterator(action: (KSDeclaration) -> Unit) {
    iterator().let {
        while (it.hasNext()) {
            action(it.next())
        }
    }
}
