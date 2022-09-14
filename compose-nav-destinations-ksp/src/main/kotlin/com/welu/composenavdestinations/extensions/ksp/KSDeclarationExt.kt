package com.welu.composenavdestinations.extensions.ksp

import com.google.devtools.ksp.isInternal
import com.google.devtools.ksp.isPublic
import com.google.devtools.ksp.symbol.*
import com.welu.composenavdestinations.model.ImportInfo

val KSDeclaration.isAccessible get() = isPublic() || isInternal()

fun KSDeclaration.asImportInfo(simpleNameSuffix: String = ""): ImportInfo? = qualifiedName?.asString()?.let {
    ImportInfo(qualifiedName = it + simpleNameSuffix.trim())
}


//fun KSDeclaration.getRootKSFile(): KSFile = containingFile
//    ?: parentDeclaration?.getRootKSFile()
//    ?: throw IllegalStateException("Could not load the file for the following Declaration: ${this.qualifiedName?.asString()} - ${this.simpleName.asString()}")
