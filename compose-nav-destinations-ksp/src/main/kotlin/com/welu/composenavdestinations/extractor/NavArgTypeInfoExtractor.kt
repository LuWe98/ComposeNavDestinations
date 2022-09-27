package com.welu.composenavdestinations.extractor

import com.welu.composenavdestinations.model.*
import com.welu.composenavdestinations.utils.PackageUtils
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
            return ParameterNavTypeInfo(
                typeInfo = this,
                customNavArgType = generateCustomNavArgsType(this, CustomNavArgType::ParcelableType)
            )
        }

        if (isEnum) {
            return ParameterNavTypeInfo(
                typeInfo = this,
                customNavArgType = generateCustomNavArgsType(this, CustomNavArgType::EnumType)
            )
        }

        if (type.typeArguments.size == 1) {
            val argTypeInfo = type.typeArguments.first().typeInfo

            if (argTypeInfo != null) {
                when {
                    type.isList -> when {
                        argTypeInfo.isEnum -> generateCustomNavArgsType(argTypeInfo, CustomNavArgType::EnumListType)
                        argTypeInfo.isParcelable -> generateCustomNavArgsType(argTypeInfo, CustomNavArgType::ParcelableListType)
                        else -> null
                    }
                    type.isSet -> when {
                        argTypeInfo.isEnum -> generateCustomNavArgsType(argTypeInfo, CustomNavArgType::EnumSetType)
                        argTypeInfo.isParcelable -> generateCustomNavArgsType(argTypeInfo, CustomNavArgType::ParcelableSetType)
                        else -> null
                    }
                    type.isArray -> when {
                        argTypeInfo.isEnum -> generateCustomNavArgsType(argTypeInfo, CustomNavArgType::EnumArrayType)
                        argTypeInfo.isParcelable -> generateCustomNavArgsType(argTypeInfo, CustomNavArgType::ParcelableArrayType)
                        else -> null
                    }
                    else -> null
                }?.let { customArgType ->
                    return ParameterNavTypeInfo(
                        typeInfo = argTypeInfo,
                        customNavArgType =  customArgType
                    )
                }
            }
        }

        if (isSerializable) {
            return ParameterNavTypeInfo(BasicNavArgType.SerializableType)
        }

        if (isKtxSerializable) {
            //TODO -> KTX Serializable NavArg -> Hier muss dann auch auf den Serializer Referenziert werden, also für jeden Typen ein eigenes --> CustomNavArgType wie Enum
        }

        throw IllegalArgumentException("Cannot map the following Parameter: ${type.import.qualifiedName}")
    }


    //Generiert ein CustonNavArgsType mit wenigen Informationen -> Wird später genutzt, wenn es kein ParameterNavTypeInfo mehr gibt
    private inline fun <reified T: CustomNavArgType> generateCustomNavArgsType(
        typeInfo: ParameterTypeInfo,
        customNavArgTypeProvider: (ImportInfo, ImportInfo) -> T
    ): T {
        val generatedCustomNavArgTypeImport = ImportInfo(
            simpleName = typeInfo.qualifiedName.replace(".", "_") + "_" + "NavArg" + T::class.simpleName,
            packageDir = PackageUtils.NAV_ARGS_PACKAGE
        )

        return customNavArgTypeProvider(
            generatedCustomNavArgTypeImport,
            typeInfo.type.import
        )
    }

    //TODO -> Noch custom Serializer einbauen, dass man das für die Navigation auch verwenden kann
    /*
    @NavTypeSerializer
    class ColorTypeSerializer : DestinationsNavTypeSerializer<Color> {
        override fun toRouteString(value: Color): String
        override fun fromRouteString(routeStr: String): Color
    }
     */

    private fun ParameterTypeInfo.isSame(otherInfo: ParameterTypeInfo): Boolean = qualifiedName == otherInfo.qualifiedName
            && type.typeArguments.size == otherInfo.type.typeArguments.size
            && type.typeArguments.zip(otherInfo.type.typeArguments).all { it.first.typeInfo?.qualifiedName == it.second.typeInfo?.qualifiedName }

}