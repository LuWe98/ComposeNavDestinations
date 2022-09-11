package com.welu.composenavdestinations.mapper

import com.welu.composenavdestinations.model.*
import com.welu.composenavdestinations.utils.PackageUtils
import com.welu.composenavdestinations.utils.PackageUtils.BASIC_NAV_ARGS
import com.welu.composenavdestinations.utils.PackageUtils.NAV_ARG_SERIALIZABLE_TYPE

object ParameterNavArgInfoExtractor {

    fun ParameterTypeInfo.extractParameterNavArgInfo(): ParameterNavArgInfo {
        BASIC_NAV_ARGS.firstOrNull { this.isSame(it.first) }?.second?.let {
            return ParameterNavArgInfo(it)
        }

        if (isParcelable) {
            return ParameterNavArgInfo(this, PackageUtils.NAV_ARG_PARCELABLE_TYPE)
        }

        if (isEnum) {
            return ParameterNavArgInfo(this, PackageUtils.NAV_ARG_ENUM_TYPE)
        }

        if (type.typeArguments.size == 1) {
            val argTypeInfo = type.typeArguments.first().typeInfo

            if (argTypeInfo != null) {
                when {
                    type.isList -> when {
                        argTypeInfo.isEnum -> PackageUtils.NAV_ARG_ENUM_LIST_TYPE
                        argTypeInfo.isParcelable -> PackageUtils.NAV_ARG_PARCELABLE_LIST_TYPE
                        else -> null
                    }
                    type.isSet -> when {
                        argTypeInfo.isEnum -> PackageUtils.NAV_ARG_ENUM_SET_TYPE
                        argTypeInfo.isParcelable -> PackageUtils.NAV_ARG_PARCELABLE_SET_TYPE
                        else -> null
                    }
                    type.isArray -> when {
                        argTypeInfo.isEnum -> PackageUtils.NAV_ARG_ENUM_ARRAY_TYPE
                        argTypeInfo.isParcelable -> PackageUtils.NAV_ARG_PARCELABLE_ARRAY_TYPE
                        else -> null
                    }
                    else -> null
                }?.let {
                    return ParameterNavArgInfo(argTypeInfo, it)
                }
            }
        }

        if (isSerializable) {
            return ParameterNavArgInfo(NAV_ARG_SERIALIZABLE_TYPE)
        }

        if (isKtxSerializable) {
            //TODO -> KTX Serializable NavArg -> Hier muss dann auch auf den Serializer Referenziert werden, also für jeden Typen ein eigenes --> CustomNavArgType wie Enum
        }

        //TODO -> Noch custom Serializer einbauen, dass man das für die Navigation auch verwenden kann
        /*
        @NavTypeSerializer
        class ColorTypeSerializer : DestinationsNavTypeSerializer<Color> {
            override fun toRouteString(value: Color): String
            override fun fromRouteString(routeStr: String): Color
        }
         */

        throw IllegalArgumentException("Cannot map the following Parameter: ${type.import.qualifiedName}")
    }


    private fun ParameterTypeInfo.isSame(otherInfo: ParameterTypeInfo): Boolean =
        qualifiedName == otherInfo.qualifiedName
            && type.typeArguments.size == otherInfo.type.typeArguments.size
            && type.typeArguments.zip(otherInfo.type.typeArguments).all { it.first.typeInfo?.qualifiedName == it.second.typeInfo?.qualifiedName }

}