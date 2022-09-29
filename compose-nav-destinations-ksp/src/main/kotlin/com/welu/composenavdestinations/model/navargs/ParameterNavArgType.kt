package com.welu.composenavdestinations.model.navargs

import com.welu.composenavdestinations.model.ImportInfo
import com.welu.composenavdestinations.model.Parameter

/**
 * Represents a NavArgType which will be used to parse and serialize a [Parameter].
 *
 * A [ParameterNavArgType] can either be a [BasicParameterNavArgType] (used for primitive types) or a [ComplexParameterNavArgType] (used for complex types - e.g. Parcelables and Enums)
 *
 * @property importInfo The import of the used [ParameterNavArgType]
 */
sealed interface ParameterNavArgType {
    val importInfo: ImportInfo
    val simpleName: String get() = importInfo.simpleName
}

/**
 * The actual import of this [ParameterNavArgType].
 * This is needed so that the [ComplexParameterNavArgType.generatedNavArgImport] will be used instead of the default [ParameterNavArgType.importInfo]
 * for [BasicParameterNavArgType]s
 */
val ParameterNavArgType.actualTypeImport get() = when(this){
    is BasicParameterNavArgType -> importInfo
    is ComplexParameterNavArgType -> generatedNavArgImport
    else -> throw IllegalArgumentException()
}

val ParameterNavArgType.actualTypeName get() = actualTypeImport.simpleName