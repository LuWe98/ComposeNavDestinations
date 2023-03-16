package com.welu.compose_nav_destinations_ksp.extractor

import com.welu.compose_nav_destinations_ksp.model.ImportInfo
import com.welu.compose_nav_destinations_ksp.model.ParameterTypeInfo
import com.welu.compose_nav_destinations_ksp.model.navargs.BasicParameterNavArgType
import com.welu.compose_nav_destinations_ksp.model.navargs.ComplexParameterNavArgType
import com.welu.compose_nav_destinations_ksp.model.navargs.ParameterNavArgType
import com.welu.compose_nav_destinations_ksp.utils.ImportUtils
import com.welu.compose_nav_destinations_ksp.utils.ImportUtils.BASIC_NAV_ARGS_MAP

object NavArgTypeInfoExtractor {

    /**
     * Extracts the NavArgType which is used to parse and serialize this parameter.
     */
    fun ParameterTypeInfo.extractParameterNavArgType(): ParameterNavArgType {
        BASIC_NAV_ARGS_MAP.entries.firstOrNull { isSame(it.key) }?.let {
            return it.value
        }

        if (isParcelable) {
            return generateCustomNavArgsType(this, ComplexParameterNavArgType::ParcelableType)
        }

        if (isEnum) {
            return generateCustomNavArgsType(this, ComplexParameterNavArgType::EnumType)
        }

        if (isKotlinSerializable) {
            //TODO -> KTX Serializable NavArg -> Hier muss dann auch auf den Serializer Referenziert werden, also für jeden Typen ein eigenes --> CustomNavArgType wie Enum
            return generateCustomNavArgsType(this, ComplexParameterNavArgType::KotlinSerializableType)
        }

        if (type.typeArguments.size == 1) {
            val argTypeInfo = type.typeArguments.first().typeInfo

            if (argTypeInfo != null) {
                when {
                    type.isList -> when {
                        argTypeInfo.isEnum -> generateCustomNavArgsType(argTypeInfo, ComplexParameterNavArgType::EnumListType)
                        argTypeInfo.isParcelable -> generateCustomNavArgsType(argTypeInfo, ComplexParameterNavArgType::ParcelableListType)
                        else -> null
                    }
                    type.isSet -> when {
                        argTypeInfo.isEnum -> generateCustomNavArgsType(argTypeInfo, ComplexParameterNavArgType::EnumSetType)
                        argTypeInfo.isParcelable -> generateCustomNavArgsType(argTypeInfo, ComplexParameterNavArgType::ParcelableSetType)
                        else -> null
                    }
                    type.isArray -> when {
                        argTypeInfo.isEnum -> generateCustomNavArgsType(argTypeInfo, ComplexParameterNavArgType::EnumArrayType)
                        argTypeInfo.isParcelable -> generateCustomNavArgsType(argTypeInfo, ComplexParameterNavArgType::ParcelableArrayType)
                        else -> null
                    }
                    else -> null
                }?.let { customArgType ->
                    return customArgType
                }
            }
        }

        if (isSerializable) {
            return BasicParameterNavArgType.SerializableType
        }

        throw IllegalArgumentException("Cannot map the following Parameter: ${type.import.qualifiedName}")
    }


    //Generiert ein CustonNavArgsType mit wenigen Informationen -> Wird später genutzt, wenn es kein ParameterNavTypeInfo mehr gibt
    private inline fun <reified T: ComplexParameterNavArgType> generateCustomNavArgsType(
        typeInfo: ParameterTypeInfo,
        customNavArgTypeProvider: (ImportInfo, ImportInfo) -> T
    ): T {
        val generatedCustomNavArgTypeImport = ImportInfo(
            simpleName = typeInfo.qualifiedName.replace(".", "_") + "_" + "NavArg" + T::class.simpleName,
            packageDir = ImportUtils.NAV_ARGS_PACKAGE
        )
        return customNavArgTypeProvider(generatedCustomNavArgTypeImport, typeInfo.type.import)
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