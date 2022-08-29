package com.welu.composenavdestinations.mapper

import com.welu.composenavdestinations.model.*
import com.welu.composenavdestinations.utils.PackageUtils
import com.welu.composenavdestinations.utils.PackageUtils.BASIC_NAV_ARGS
import com.welu.composenavdestinations.utils.PackageUtils.NAV_ARG_SERIALIZABLE_TYPE

object ParameterNavArgInfoMapper {

    fun ParameterTypeInfo.extractParameterNavArgInfo(): ParameterNavArgInfo {
        BASIC_NAV_ARGS.firstOrNull { (otherType, _) ->
            qualifiedName == otherType.qualifiedName
                    && type.typeArguments.size == otherType.type.typeArguments.size
                    && type.typeArguments.zip(otherType.type.typeArguments).all {
                it.first is ParameterTypeArgument.Typed
                        && it.second is ParameterTypeArgument.Typed
                        && (it.first as ParameterTypeArgument.Typed).typeInfo.qualifiedName == (it.second as ParameterTypeArgument.Typed).typeInfo.qualifiedName
            }
        }?.second?.let {
            return ParameterNavArgInfo(it)
        }

        if (isParcelable) {
            return ParameterNavArgInfo(this, PackageUtils.NAV_ARG_PARCELABLE_TYPE)
        }

        if (isEnum) {
            return ParameterNavArgInfo(this, PackageUtils.NAV_ARG_ENUM_TYPE)
        }

        if (type.typeArguments.size == 1) {
            val firstTypeArg = type.typeArguments.first()

            if (firstTypeArg is ParameterTypeArgument.Typed) {
                val navArgType: ImportInfo? = when {
                    type.isList -> when {
                        firstTypeArg.typeInfo.isEnum -> PackageUtils.NAV_ARG_ENUM_LIST_TYPE
                        firstTypeArg.typeInfo.isParcelable -> PackageUtils.NAV_ARG_PARCELABLE_LIST_TYPE
                        else -> null
                    }
                    type.isSet -> when {
                        firstTypeArg.typeInfo.isEnum -> PackageUtils.NAV_ARG_ENUM_SET_TYPE
                        firstTypeArg.typeInfo.isParcelable -> PackageUtils.NAV_ARG_PARCELABLE_SET_TYPE
                        else -> null
                    }
                    type.isArray -> when {
                        firstTypeArg.typeInfo.isEnum -> PackageUtils.NAV_ARG_ENUM_ARRAY_TYPE
                        firstTypeArg.typeInfo.isParcelable -> PackageUtils.NAV_ARG_PARCELABLE_ARRAY_TYPE
                        else -> null
                    }
                    else -> null
                }

                navArgType?.let {
                    return ParameterNavArgInfo(firstTypeArg.typeInfo, it)
                }
            }
        }

        if (isSerializable) {
            return ParameterNavArgInfo(NAV_ARG_SERIALIZABLE_TYPE)
        }

        if (isKtxSerializable) {
            //TODO -> KTX Serializable NavArg -> Hier muss dann auch auf den Serializer Referenziert werden, also für jeden Typen ein eigenes --> CustomNavArgType wie Enum
        }

        throw IllegalArgumentException("Cannot map the following Parameter: ${type.import.qualifiedName}")
    }

    /*
fun isSame(otherInfo: ParameterTypeInfo): Boolean = qualifiedName == otherInfo.qualifiedName
        && type.typeArguments.size == otherInfo.type.typeArguments.size
        && type.typeArguments.zip(otherInfo.type.typeArguments).all {
    it.first is Typed && it.second is Typed && (it.first as Typed).typeInfo.qualifiedName == (it.second as Typed).typeInfo.qualifiedName
}
 */

}