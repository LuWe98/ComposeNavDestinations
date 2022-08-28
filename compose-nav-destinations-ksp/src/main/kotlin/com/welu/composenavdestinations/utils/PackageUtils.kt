package com.welu.composenavdestinations.utils

import com.welu.composenavdestinations.extensions.asParamTypeInfo
import com.welu.composenavdestinations.extensions.asParameterTypeInfo
import com.welu.composenavdestinations.model.ImportInfo
import com.welu.composenavdestinations.model.ParameterTypeInfo
import kotlin.reflect.KClass

object PackageUtils {

    const val PACKAGE_NAME = "com.welu.composenavdestinations"

    val KOTLIN_DEFAULT_PACKAGES = arrayOf(
        "kotlin",
        "kotlin.annotation",
        "kotlin.collections",
        "kotlin.comparisons",
        "kotlin.io",
        "kotlin.ranges",
        "kotlin.sequences",
        "kotlin.text"
    )

    val VALID_LIST_QUALIFIERS = arrayOf(
        List::class.qualifiedName!!,
        java.util.List::class.qualifiedName!!,
        MutableList::class.qualifiedName!!,
        AbstractList::class.qualifiedName!!,
        java.util.AbstractList::class.qualifiedName!!,
        ArrayList::class.qualifiedName!!,
        java.util.ArrayList::class.qualifiedName!!
    )

    val VALID_LIST_IMPORT_INFOS = VALID_LIST_QUALIFIERS.map(::ImportInfo)

    val VALID_SET_QUALIFIERS = arrayOf(
        Set::class.qualifiedName!!,
        java.util.Set::class.qualifiedName!!,
        MutableSet::class.qualifiedName!!,
        AbstractSet::class.qualifiedName!!,
        java.util.AbstractSet::class.qualifiedName!!,
        HashSet::class.qualifiedName!!,
        java.util.HashSet::class.qualifiedName!!
    )

    val VALID_SET_IMPORT_INFOS = VALID_SET_QUALIFIERS.map(::ImportInfo)


    const val ANNOTATION_NAV_DESTINATION = "$PACKAGE_NAME.annotations.NavDestination"

    const val PARCELABLE_QUALIFIED_NAME = "android.os.Parcelable"

    const val NAV_ARGS_PACKAGE = "$PACKAGE_NAME.navargs"

    const val NAV_ARGS_PREFIX = "NavArg"
    const val NAV_ARGS_SUFFIX = "Type"
    const val NAV_ARGS_PRIMITIVE_ARRAY_PREFIX = NAV_ARGS_PREFIX + "Primitive"
    const val NAV_ARGS_ARRAY_SUFFIX = "Array$NAV_ARGS_SUFFIX"
    const val NAV_ARGS_LIST_SUFFIX = "List$NAV_ARGS_SUFFIX"
    const val NAV_ARGS_SET_SUFFIX = "Set$NAV_ARGS_SUFFIX"

    val NAV_ARG_STRING_TYPE = ImportInfo("$NAV_ARGS_PACKAGE.NavArgStringType")
    val NAV_ARG_STRING_ARRAY_TYPE = ImportInfo("$NAV_ARGS_PACKAGE.NavArgStringArrayType")
    val NAV_ARG_STRING_LIST_TYPE = ImportInfo("$NAV_ARGS_PACKAGE.NavArgStringListType")
    val NAV_ARG_STRING_SET_TYPE = ImportInfo("$NAV_ARGS_PACKAGE.NavArgStringSetType")

    val NAV_ARG_CHAR_TYPE = ImportInfo("$NAV_ARGS_PACKAGE.NavArgCharType")
    val NAV_ARG_PRIMITIVE_CHAR_ARRAY_TYPE = ImportInfo("$NAV_ARGS_PACKAGE.NavArgPrimitiveCharArrayType")
    val NAV_ARG_CHAR_ARRAY_TYPE = ImportInfo("$NAV_ARGS_PACKAGE.NavArgCharArrayType")
    val NAV_ARG_CHAR_LIST_TYPE = ImportInfo("$NAV_ARGS_PACKAGE.NavArgCharListType")
    val NAV_ARG_CHAR_SET_TYPE = ImportInfo("$NAV_ARGS_PACKAGE.NavArgCharSetType")

    val NAV_ARG_LONG_TYPE = ImportInfo("$NAV_ARGS_PACKAGE.NavArgLongType")
    val NAV_ARG_PRIMITIVE_LONG_ARRAY_TYPE = ImportInfo("$NAV_ARGS_PACKAGE.NavArgPrimitiveLongArrayType")
    val NAV_ARG_LONG_ARRAY_TYPE = ImportInfo("$NAV_ARGS_PACKAGE.NavArgLongArrayType")
    val NAV_ARG_LONG_LIST_TYPE = ImportInfo("$NAV_ARGS_PACKAGE.NavArgLongListType")
    val NAV_ARG_LONG_SET_TYPE = ImportInfo("$NAV_ARGS_PACKAGE.NavArgLongSetType")

    val NAV_ARG_INT_TYPE = ImportInfo("$NAV_ARGS_PACKAGE.NavArgIntType")
    val NAV_ARG_PRIMITIVE_INT_ARRAY_TYPE = ImportInfo("$NAV_ARGS_PACKAGE.NavArgPrimitiveIntArrayType")
    val NAV_ARG_INT_ARRAY_TYPE = ImportInfo("$NAV_ARGS_PACKAGE.NavArgIntArrayType")
    val NAV_ARG_INT_LIST_TYPE = ImportInfo("$NAV_ARGS_PACKAGE.NavArgIntListType")
    val NAV_ARG_INT_SET_TYPE = ImportInfo("$NAV_ARGS_PACKAGE.NavArgIntSetType")

    val NAV_ARG_SHORT_TYPE = ImportInfo("$NAV_ARGS_PACKAGE.NavArgShortType")
    val NAV_ARG_PRIMITIVE_SHORT_ARRAY_TYPE = ImportInfo("$NAV_ARGS_PACKAGE.NavArgPrimitiveShortArrayType")
    val NAV_ARG_SHORT_ARRAY_TYPE = ImportInfo("$NAV_ARGS_PACKAGE.NavArgShortArrayType")
    val NAV_ARG_SHORT_LIST_TYPE = ImportInfo("$NAV_ARGS_PACKAGE.NavArgShortListType")
    val NAV_ARG_SHORT_SET_TYPE = ImportInfo("$NAV_ARGS_PACKAGE.NavArgShortSetType")

    val NAV_ARG_BYTE_TYPE = ImportInfo("$NAV_ARGS_PACKAGE.NavArgByteType")
    val NAV_ARG_PRIMITIVE_BYTE_ARRAY_TYPE = ImportInfo("$NAV_ARGS_PACKAGE.NavArgPrimitiveByteArrayType")
    val NAV_ARG_BYTE_ARRAY_TYPE = ImportInfo("$NAV_ARGS_PACKAGE.NavArgByteArrayType")
    val NAV_ARG_BYTE_LIST_TYPE = ImportInfo("$NAV_ARGS_PACKAGE.NavArgByteListType")
    val NAV_ARG_BYTE_SET_TYPE = ImportInfo("$NAV_ARGS_PACKAGE.NavArgByteSetType")

    val NAV_ARG_BOOLEAN_TYPE = ImportInfo("$NAV_ARGS_PACKAGE.NavArgBooleanType")
    val NAV_ARG_PRIMITIVE_BOOLEAN_ARRAY_TYPE = ImportInfo("$NAV_ARGS_PACKAGE.NavArgPrimitiveBooleanArrayType")
    val NAV_ARG_BOOLEAN_ARRAY_TYPE = ImportInfo("$NAV_ARGS_PACKAGE.NavArgBooleanArrayType")
    val NAV_ARG_BOOLEAN_LIST_TYPE = ImportInfo("$NAV_ARGS_PACKAGE.NavArgBooleanListType")
    val NAV_ARG_BOOLEAN_SET_TYPE = ImportInfo("$NAV_ARGS_PACKAGE.NavArgBooleanSetType")

    val NAV_ARG_FLOAT_TYPE = ImportInfo("$NAV_ARGS_PACKAGE.NavArgFloatType")
    val NAV_ARG_PRIMITIVE_FLOAT_ARRAY_TYPE = ImportInfo("$NAV_ARGS_PACKAGE.NavArgPrimitiveFloatArrayType")
    val NAV_ARG_FLOAT_ARRAY_TYPE = ImportInfo("$NAV_ARGS_PACKAGE.NavArgFloatArrayType")
    val NAV_ARG_FLOAT_LIST_TYPE = ImportInfo("$NAV_ARGS_PACKAGE.NavArgFloatListType")
    val NAV_ARG_FLOAT_SET_TYPE = ImportInfo("$NAV_ARGS_PACKAGE.NavArgFloatSetType")

    val NAV_ARG_DOUBLE_TYPE = ImportInfo("$NAV_ARGS_PACKAGE.NavArgDoubleType")
    val NAV_ARG_PRIMITIVE_DOUBLE_ARRAY_TYPE = ImportInfo("$NAV_ARGS_PACKAGE.NavArgPrimitiveDoubleArrayType")
    val NAV_ARG_DOUBLE_ARRAY_TYPE = ImportInfo("$NAV_ARGS_PACKAGE.NavArgDoubleArrayType")
    val NAV_ARG_DOUBLE_LIST_TYPE = ImportInfo("$NAV_ARGS_PACKAGE.NavArgDoubleListType")
    val NAV_ARG_DOUBLE_SET_TYPE = ImportInfo("$NAV_ARGS_PACKAGE.NavArgDoubleSetType")

    val NAV_ARG_SERIALIZABLE_TYPE = ImportInfo("$NAV_ARGS_PACKAGE.NavArgSerializableType")

    val BASIC_NAV_ARG_TYPES = setOf(
        NAV_ARG_STRING_TYPE, NAV_ARG_STRING_ARRAY_TYPE, NAV_ARG_STRING_LIST_TYPE, NAV_ARG_STRING_SET_TYPE,
        NAV_ARG_CHAR_TYPE, NAV_ARG_PRIMITIVE_CHAR_ARRAY_TYPE, NAV_ARG_CHAR_ARRAY_TYPE, NAV_ARG_CHAR_LIST_TYPE, NAV_ARG_CHAR_SET_TYPE,
        NAV_ARG_LONG_TYPE, NAV_ARG_PRIMITIVE_LONG_ARRAY_TYPE, NAV_ARG_LONG_ARRAY_TYPE, NAV_ARG_LONG_LIST_TYPE, NAV_ARG_LONG_SET_TYPE,
        NAV_ARG_INT_TYPE, NAV_ARG_PRIMITIVE_INT_ARRAY_TYPE, NAV_ARG_INT_ARRAY_TYPE, NAV_ARG_INT_LIST_TYPE, NAV_ARG_INT_SET_TYPE,
        NAV_ARG_SHORT_TYPE, NAV_ARG_PRIMITIVE_SHORT_ARRAY_TYPE, NAV_ARG_SHORT_ARRAY_TYPE, NAV_ARG_SHORT_LIST_TYPE, NAV_ARG_SHORT_SET_TYPE,
        NAV_ARG_BYTE_TYPE, NAV_ARG_PRIMITIVE_BYTE_ARRAY_TYPE, NAV_ARG_BYTE_ARRAY_TYPE, NAV_ARG_BYTE_LIST_TYPE, NAV_ARG_BYTE_SET_TYPE,
        NAV_ARG_BOOLEAN_TYPE, NAV_ARG_PRIMITIVE_BOOLEAN_ARRAY_TYPE, NAV_ARG_BOOLEAN_ARRAY_TYPE, NAV_ARG_BOOLEAN_LIST_TYPE, NAV_ARG_BOOLEAN_SET_TYPE,
        NAV_ARG_FLOAT_TYPE, NAV_ARG_PRIMITIVE_FLOAT_ARRAY_TYPE, NAV_ARG_FLOAT_ARRAY_TYPE, NAV_ARG_FLOAT_LIST_TYPE, NAV_ARG_FLOAT_SET_TYPE,
        NAV_ARG_DOUBLE_TYPE, NAV_ARG_PRIMITIVE_DOUBLE_ARRAY_TYPE, NAV_ARG_DOUBLE_ARRAY_TYPE, NAV_ARG_DOUBLE_LIST_TYPE, NAV_ARG_DOUBLE_SET_TYPE
    )

    val BASIC_NAV_ARGS: List<Pair<ParameterTypeInfo, ImportInfo>> = setOf(
        generateBasicNavArgsForClass(String::class),
        generateBasicNavArgsForClass(Char::class, CharArray::class),
        generateBasicNavArgsForClass(Boolean::class, BooleanArray::class),
        generateBasicNavArgsForClass(Byte::class, ByteArray::class),
        generateBasicNavArgsForClass(Short::class, ShortArray::class),
        generateBasicNavArgsForClass(Int::class, IntArray::class),
        generateBasicNavArgsForClass(Long::class, LongArray::class),
        generateBasicNavArgsForClass(Float::class, FloatArray::class),
        generateBasicNavArgsForClass(Double::class, DoubleArray::class),
    ).flatten()

    private fun navArgOfSimpleName(simpleName: String): ImportInfo? = BASIC_NAV_ARG_TYPES.firstOrNull { it.simpleName == simpleName }

    private fun generateBasicNavArgsForClass(
        clazz: KClass<*>,
        primitiveArrayClazz: KClass<*>? = null,
        name: String = clazz.simpleName!!
    ): List<Pair<ParameterTypeInfo, ImportInfo>> {
        val listType = navArgOfSimpleName(NAV_ARGS_PREFIX + name + NAV_ARGS_LIST_SUFFIX)!!
        val setType = navArgOfSimpleName(NAV_ARGS_PREFIX + name + NAV_ARGS_SET_SUFFIX)!!
        val listImports = VALID_LIST_IMPORT_INFOS.map { it.asParamTypeInfo(clazz) to listType }
        val setImports = VALID_SET_IMPORT_INFOS.map { it.asParamTypeInfo(clazz) to setType }

        return mutableListOf<Pair<ParameterTypeInfo, ImportInfo>>().apply {
            add(clazz.asParamTypeInfo() to navArgOfSimpleName(NAV_ARGS_PREFIX + name + NAV_ARGS_SUFFIX)!!)
            addAll(listImports)
            addAll(setImports)
            add(Array::class.asParamTypeInfo(clazz) to navArgOfSimpleName(NAV_ARGS_PREFIX + name + NAV_ARGS_ARRAY_SUFFIX)!!)
            primitiveArrayClazz?.let {
                add(it.asParameterTypeInfo() to navArgOfSimpleName(NAV_ARGS_PRIMITIVE_ARRAY_PREFIX + name + NAV_ARGS_ARRAY_SUFFIX)!!)
            }
        }
    }

    /*
            return mutableListOf(
            clazz.asParamTypeInfo() to navArgOfSimpleName(NAV_ARGS_PREFIX + name + NAV_ARGS_SUFFIX)!!,
            List::class.asParamTypeInfo(clazz) to listType,
            java.util.List::class.asParamTypeInfo(clazz) to listType,
            ArrayList::class.asParamTypeInfo(clazz) to listType,
            java.util.ArrayList::class.asParamTypeInfo(clazz) to listType,
            AbstractList::class.asParamTypeInfo(clazz) to listType,
            java.util.AbstractList::class.asParamTypeInfo(clazz) to listType,
            MutableList::class.asParamTypeInfo(clazz) to listType,
            Set::class.asParamTypeInfo(clazz) to setType,
            java.util.Set::class.asParamTypeInfo(clazz) to setType,
            HashSet::class.asParamTypeInfo(clazz) to setType,
            java.util.HashSet::class.asParamTypeInfo(clazz) to setType,
            AbstractSet::class.asParamTypeInfo(clazz) to setType,
            java.util.AbstractSet::class.asParamTypeInfo(clazz) to setType,
            MutableSet::class.asParamTypeInfo(clazz) to setType,
        ).apply {
            if (includeNullableArray) {
                add(Array::class.asParamTypeInfo(clazz) to navArgOfSimpleName(NAV_ARGS_PREFIX + name + NAV_ARGS_ARRAY_SUFFIX)!!)
            }
            primitiveArrayClazz?.let {
                add(it.asParameterTypeInfo() to navArgOfSimpleName(NAV_ARGS_PREFIX + primitiveArrayName + NAV_ARGS_ARRAY_SUFFIX)!!)
            }
        }
     */
}