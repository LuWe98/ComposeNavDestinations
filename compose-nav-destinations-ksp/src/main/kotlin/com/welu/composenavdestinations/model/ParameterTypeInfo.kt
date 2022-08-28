package com.welu.composenavdestinations.model

import com.welu.composenavdestinations.extensions.div
import com.welu.composenavdestinations.extensions.ifNotBlank
import com.welu.composenavdestinations.model.ParameterTypeArgument.Star
import com.welu.composenavdestinations.model.ParameterTypeArgument.Typed
import com.welu.composenavdestinations.utils.PackageUtils

data class ParameterTypeInfo(
    val type: ParameterType,
    val isNullable: Boolean = false
) {

    val isEnum get() = type.isEnum
    val isParcelable get() = type.isParcelable
    val isSerializable get() = type.isSerializable
    val isKtxSerializable get() = type.isKtxSerializable
    val qualifiedName get() = type.import.qualifiedName
    val asCustomNavArgName get() = PackageUtils.NAV_ARGS_PACKAGE + "." + qualifiedName.replace(".", "_")

    val allChildImports
        get(): List<ImportInfo> = type.typeArguments.filterIsInstance<Typed>().flatMap(Typed::typeInfo / ParameterTypeInfo::allChildImports) + type.import

    val definition
        get(): String = run {
            if (type.typeArguments.isEmpty()) return@run type.import.simpleName

            type.import.simpleName + "<" + type.typeArguments.joinToString(", ") { type ->
                when (type) {
                    Star -> type.varianceLabel
                    is Typed -> type.varianceLabel.ifNotBlank { "$it " } + type.typeInfo.definition + if (type.typeInfo.isNullable) "?" else ""
                }
            } + ">"
        }

    fun isSame(otherInfo: ParameterTypeInfo): Boolean = qualifiedName == otherInfo.qualifiedName
            && type.typeArguments.size == otherInfo.type.typeArguments.size
            && type.typeArguments.zip(otherInfo.type.typeArguments).all {
        it.first is Typed && it.second is Typed && (it.first as Typed).typeInfo.qualifiedName == (it.second as Typed).typeInfo.qualifiedName
    }

    //    override fun hashCode(): Int {
//        return Objects.hash(
//            qualifiedName,
//            type.typeArguments.map {
//                when (it) {
//                    is Star -> 0
//                    is Typed -> it.typeInfo.hashCode()
//                }
//            }
//        )
//    }
//
//    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//        if (javaClass != other?.javaClass) return false
//        return isSame(other as ParameterTypeInfo)
//    }
}