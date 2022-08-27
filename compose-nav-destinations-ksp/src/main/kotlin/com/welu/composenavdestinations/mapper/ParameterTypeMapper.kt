package com.welu.composenavdestinations.mapper

import com.welu.composenavdestinations.extensions.writeComment
import com.welu.composenavdestinations.model.PackageImportInfo
import com.welu.composenavdestinations.model.Parameter
import com.welu.composenavdestinations.model.ParameterTypeArgument
import com.welu.composenavdestinations.processors.NavDestinationsProcessor
import com.welu.composenavdestinations.utils.Constants
import kotlin.reflect.KClass

object ParameterTypeMapper {

    val BASIC_NAV_ARG_STRING_TYPE = PackageImportInfo("${Constants.PACKAGE_NAME}.navargs.NavArgStringType")



    fun map(parameter: Parameter) {
        //NavDestinationsProcessor.debugFile.write("parameter: ${parameter.name} \n")

        when {
            parameter.typeInfo.isParcelable -> {
                //TODO -> Parcelable NavArg
                NavDestinationsProcessor.debugFile.writeComment("${ResultType.Parcelable.name}: ${parameter.name} \n")
            }
            parameter.typeInfo.hasNoTypeArguments -> {
                val type = handleUntypedParameter(parameter)
                NavDestinationsProcessor.debugFile.writeComment("${type.name}: ${parameter.name} \n")
            }
            parameter.typeInfo.hasOneTypeArgument -> {
                val type = handleSingleTypeParameter(parameter)
                NavDestinationsProcessor.debugFile.writeComment("${type.name}: ${parameter.name} \n")
            }
            parameter.typeInfo.isSerializable -> {
                //TODO -> Serializable NavArg
                NavDestinationsProcessor.debugFile.writeComment("${ResultType.Serializable.name}: ${parameter.name} \n")
            }
            parameter.typeInfo.isKtxSerializable -> {
                //TODO -> KTX Serializable NavArg
                NavDestinationsProcessor.debugFile.writeComment("KTX SERIALIZABLE: ${parameter.name} \n")
            }
            else -> throw IllegalArgumentException("Cannot map the following Parameter: ${parameter.name}")
        }
    }

    private fun handleUntypedParameter(parameter: Parameter): ResultType {
        return when (parameter.typeInfo.qualifiedName) {
            Boolean::class.qualifiedName -> ResultType.Boolean
            BooleanArray::class.qualifiedName -> ResultType.BooleanArray
            Short::class.qualifiedName -> ResultType.Short
            ShortArray::class.qualifiedName -> ResultType.ShortArray
            Int::class.qualifiedName -> ResultType.Int
            IntArray::class.qualifiedName -> ResultType.IntArray
            Long::class.qualifiedName -> ResultType.Long
            LongArray::class.qualifiedName -> ResultType.LongArray
            Byte::class.qualifiedName -> ResultType.Byte
            ByteArray::class.qualifiedName -> ResultType.ByteArray
            Float::class.qualifiedName -> ResultType.Float
            FloatArray::class.qualifiedName -> ResultType.FloatArray
            Double::class.qualifiedName -> ResultType.Double
            DoubleArray::class.qualifiedName -> ResultType.DoubleArray
            Char::class.qualifiedName -> ResultType.Char
            CharArray::class.qualifiedName -> ResultType.CharArray
            String::class.qualifiedName -> ResultType.String
            else -> when {
                parameter.typeInfo.isEnum -> ResultType.Enum
                parameter.typeInfo.isSerializable -> ResultType.Serializable
                else -> throw IllegalArgumentException("Cannot map the following Parameter: ${parameter.name}")
            }
        }
    }

    private fun handleSingleTypeParameter(parameter: Parameter): ResultType {

        val typeArgument: ParameterTypeArgument = parameter.typeInfo.type.typeArguments.first()

        // Einfach return null
        if (typeArgument == ParameterTypeArgument.Star) {
            if (parameter.typeInfo.isSerializable) {
                // Dann Serializable zuordnen
            } else if (parameter.typeInfo.isKtxSerializable) {
                // Dann KTX Serializable zuordnen
            } else {
                throw IllegalArgumentException("Cannot map the following Parameter: ${parameter.name}")
            }
            //return
        }

        val typedArgument: ParameterTypeArgument.Typed = typeArgument as ParameterTypeArgument.Typed

        return when {
            parameter.typeInfo.type.isList -> handleBasicTypeParameter(typedArgument, List::class)
            parameter.typeInfo.type.isSet -> handleBasicTypeParameter(typedArgument, Set::class)
            parameter.typeInfo.qualifiedName == Array::class.qualifiedName -> handleBasicTypeParameter(typedArgument, Array::class)
            parameter.typeInfo.isSerializable -> ResultType.Serializable
            else -> throw IllegalArgumentException("Cannot map the following Parameter: ${parameter.name}")
        }
    }

    private fun handleBasicTypeParameter(typedArgument: ParameterTypeArgument.Typed, type: KClass<*>): ResultType = when (typedArgument.typeInfo.qualifiedName) {
        Boolean::class.qualifiedName -> ResultType.Boolean.with(type)
        Short::class.qualifiedName -> ResultType.Short.with(type)
        Int::class.qualifiedName -> ResultType.Int.with(type)
        Long::class.qualifiedName -> ResultType.Long.with(type)
        Byte::class.qualifiedName -> ResultType.Byte.with(type)
        Float::class.qualifiedName -> ResultType.Float.with(type)
        Double::class.qualifiedName -> ResultType.Double.with(type)
        Char::class.qualifiedName -> ResultType.Char.with(type)
        String::class.qualifiedName -> ResultType.String.with(type)
        else -> when {
            typedArgument.typeInfo.isParcelable -> ResultType.Parcelable.with(type)
            typedArgument.typeInfo.isEnum -> ResultType.Enum.with(type)
            else -> ResultType.Serializable
        }
    }

    enum class ResultType {
        Boolean, BooleanArray, BooleanList, BooleanSet,
        String, StringArray, StringList, StringSet,
        Char, CharArray, CharList, CharSet,
        Int, IntArray, IntList, IntSet,
        Long, LongArray, LongList, LongSet,
        Short, ShortArray, ShortList, ShortSet,
        Byte, ByteArray, ByteList, ByteSet,
        Float, FloatArray, FloatList, FloatSet,
        Double, DoubleArray, DoubleList, DoubleSet,
        Parcelable, ParcelableArray, ParcelableList, ParcelableSet,
        Enum, EnumArray, EnumList, EnumSet,
        Serializable;

        fun with(other: KClass<*>): ResultType = when (other) {
            Array::class -> when (this) {
                Boolean -> BooleanArray
                Char -> CharArray
                String -> StringArray
                Int -> IntArray
                Short -> ShortArray
                Long -> LongArray
                Byte -> ByteArray
                Float -> FloatArray
                Double -> DoubleArray
                Parcelable -> ParcelableArray
                Enum -> EnumArray
                else -> throw IllegalArgumentException("Cannot Map")
            }
            List::class -> when (this) {
                Boolean -> BooleanList
                Char -> CharList
                String -> StringList
                Int -> IntList
                Short -> ShortList
                Long -> LongList
                Byte -> ByteList
                Float -> FloatList
                Double -> DoubleList
                Parcelable -> ParcelableList
                Enum -> EnumList
                else -> throw IllegalArgumentException("Cannot Map")
            }
            Set::class -> when (this) {
                Boolean -> BooleanSet
                Char -> CharSet
                String -> StringSet
                Int -> IntSet
                Short -> ShortSet
                Long -> LongSet
                Byte -> ByteSet
                Float -> FloatSet
                Double -> DoubleSet
                Parcelable -> ParcelableSet
                Enum -> EnumSet
                else -> throw IllegalArgumentException("Cannot Map")
            }
            else -> this
        }
    }

    //        return when (parameter.typeInfo.qualifiedName) {
//            ArrayList::class.qualifiedName -> handleBasicTypeParameter(parameter, CollectionType.List)
//            HashSet::class.qualifiedName, LinkedHashSet::class.qualifiedName -> handleBasicTypeParameter(parameter, CollectionType.Set)
//            Array::class.qualifiedName -> handleBasicTypeParameter(parameter, CollectionType.Array)
//            else -> when {
//                parameter.typeInfo.isSerializable -> {
//                    if (parameter.typeInfo.type.isList) {
//                        handleBasicTypeParameter(parameter, CollectionType.List)
//                    } else if (parameter.typeInfo.type.isSet) {
//                        handleBasicTypeParameter(parameter, CollectionType.Set)
//                    } else {
//                        ResultType.Serializable
//                    }
//                }
//                else -> throw IllegalArgumentException("Cannot map the following Parameter: ${parameter.name}")
//            }
//        }
}