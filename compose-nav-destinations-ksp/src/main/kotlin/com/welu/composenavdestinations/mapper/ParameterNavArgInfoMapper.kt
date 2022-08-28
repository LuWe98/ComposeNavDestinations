package com.welu.composenavdestinations.mapper

import com.welu.composenavdestinations.model.*
import com.welu.composenavdestinations.utils.PackageUtils.BASIC_NAV_ARGS
import com.welu.composenavdestinations.utils.PackageUtils.NAV_ARG_SERIALIZABLE_TYPE

object ParameterNavArgInfoMapper {

    fun ParameterTypeInfo.extractParameterNavArgInfo(): ParameterNavArgInfo {
        BASIC_NAV_ARGS.firstOrNull { it.first.isSame(this) }?.second?.let {
            return ParameterNavArgInfo(it)
        }

        if (isParcelable) {
            return ParameterNavArgInfo(this, CustomNavArgType.PARCELABLE)
        }

        if (isEnum) {
            return ParameterNavArgInfo(this, CustomNavArgType.ENUM)
        }

        if (type.typeArguments.size == 1) {
            val firstTypeArg = type.typeArguments.first()

            if (firstTypeArg is ParameterTypeArgument.Typed) {
                val navArgType = when {
                    type.isList -> when {
                        firstTypeArg.typeInfo.isEnum -> CustomNavArgType.ENUM_LIST
                        firstTypeArg.typeInfo.isParcelable -> CustomNavArgType.PARCELABLE_LIST
                        else -> null
                    }
                    type.isSet -> when {
                        firstTypeArg.typeInfo.isEnum -> CustomNavArgType.ENUM_SET
                        firstTypeArg.typeInfo.isParcelable -> CustomNavArgType.PARCELABLE_SET
                        else -> null
                    }
                    type.isArray -> when {
                        firstTypeArg.typeInfo.isEnum -> CustomNavArgType.ENUM_ARRAY
                        firstTypeArg.typeInfo.isParcelable ->  CustomNavArgType.PARCELABLE_ARRAY
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
            //TODO -> KTX Serializable NavArg
        }

        throw IllegalArgumentException("Cannot map the following Parameter: ${type.import.qualifiedName}")
    }
}