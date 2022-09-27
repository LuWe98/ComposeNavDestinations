package com.welu.composenavdestinations.extractor

import com.welu.composenavdestinations.model.CustomNavArgType
import com.welu.composenavdestinations.model.ParameterNavTypeInfo
import com.welu.composenavdestinations.model.ParameterTypeInfo
import com.welu.composenavdestinations.utils.PackageUtils.BASIC_NAV_ARGS_MAP


object NavArgTypeInfoExtractor {

    //        BASIC_NAV_ARGS_MAP.firstOrNull { this.isSame(it.first) }?.second?.let {
//            return ParameterNavTypeInfo(it)
//        }


    /**
     * Extracts the NavArgType which is used to parse and serialize this parameter.
     */
    fun ParameterTypeInfo.extractParameterNavArgTypeInfo(): ParameterNavTypeInfo {
        BASIC_NAV_ARGS_MAP.entries.firstOrNull { isSame(it.key) }?.let {
            return ParameterNavTypeInfo(it.value)
        }

        if (isParcelable) {
            return ParameterNavTypeInfo(this, CustomNavArgType.ParcelableType)
        }

        if (isEnum) {
            return ParameterNavTypeInfo(this, CustomNavArgType.EnumType)
        }

        if (type.typeArguments.size == 1) {
            val argTypeInfo = type.typeArguments.first().typeInfo

            if (argTypeInfo != null) {
                when {
                    type.isList -> when {
                        argTypeInfo.isEnum -> CustomNavArgType.EnumListType
                        argTypeInfo.isParcelable -> CustomNavArgType.ParcelableListType
                        else -> null
                    }
                    type.isSet -> when {
                        argTypeInfo.isEnum -> CustomNavArgType.EnumSetType
                        argTypeInfo.isParcelable -> CustomNavArgType.ParcelableSetType
                        else -> null
                    }
                    type.isArray -> when {
                        argTypeInfo.isEnum -> CustomNavArgType.EnumArrayType
                        argTypeInfo.isParcelable -> CustomNavArgType.ParcelableArrayType
                        else -> null
                    }
                    else -> null
                }?.let {
                    return ParameterNavTypeInfo(argTypeInfo, it)
                }
            }
        }

        if (isSerializable) {
            return ParameterNavTypeInfo(CustomNavArgType.SerializableType.importInfo)
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


    private fun ParameterTypeInfo.isSame(otherInfo: ParameterTypeInfo): Boolean = qualifiedName == otherInfo.qualifiedName
            && type.typeArguments.size == otherInfo.type.typeArguments.size
            && type.typeArguments.zip(otherInfo.type.typeArguments).all { it.first.typeInfo?.qualifiedName == it.second.typeInfo?.qualifiedName }

}