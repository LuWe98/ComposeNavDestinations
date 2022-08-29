package com.welu.composenavdestinations.utils

import com.welu.composenavdestinations.extensions.asParamTypeInfo
import com.welu.composenavdestinations.extensions.asParameterTypeInfo
import com.welu.composenavdestinations.model.ImportInfo
import com.welu.composenavdestinations.model.ParameterTypeInfo
import kotlin.reflect.KClass

internal object PackageUtils {

    const val PACKAGE_NAME = "com.welu.composenavdestinations"
    const val NAV_ARGS_PACKAGE = "$PACKAGE_NAME.navargs"
    private const val NAV_DESTINATION_SPEC_PACKAGE = "$PACKAGE_NAME.spec"
    val PARCELABLE_IMPORT = ImportInfo("Parcelable","android.os")
    val COMPOSABLE_IMPORT = ImportInfo("Composable","androidx.compose.runtime")
    val NAV_DESTINATION_PLAIN_SPEC_IMPORT= ImportInfo("NavDestinationPlainSpec", NAV_DESTINATION_SPEC_PACKAGE)
    val NAV_DESTINATION_ARG_SPEC_IMPORT = ImportInfo("NavDestinationArgSpec", NAV_DESTINATION_SPEC_PACKAGE)
    val NAV_DESTINATION_ROUTABLE_IMPORT = ImportInfo("Routable", NAV_DESTINATION_SPEC_PACKAGE)
    val NAV_DEEP_LINK_IMPORT = ImportInfo("NavDeepLink","androidx.navigation")
    val NAV_BACK_STACK_ENTRY_IMPORT = ImportInfo("NavBackStackEntry","androidx.navigation")
    val SAVED_STATE_HANDLE_IMPORT = ImportInfo("SavedStateHandle","androidx.lifecycle")
    val NAMED_NAV_ARGUMENT_IMPORT = ImportInfo("NamedNavArgument", "androidx.navigation")
    val NAV_ARGUMENT_IMPORT = ImportInfo("navArgument","com.welu.composenavdestinations.extensions.navigation")

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

    private val VALID_LIST_IMPORT_INFOS = VALID_LIST_QUALIFIERS.map(::ImportInfo)

    val VALID_SET_QUALIFIERS = arrayOf(
        Set::class.qualifiedName!!,
        java.util.Set::class.qualifiedName!!,
        MutableSet::class.qualifiedName!!,
        AbstractSet::class.qualifiedName!!,
        java.util.AbstractSet::class.qualifiedName!!,
        HashSet::class.qualifiedName!!,
        java.util.HashSet::class.qualifiedName!!
    )

    private val VALID_SET_IMPORT_INFOS = VALID_SET_QUALIFIERS.map(::ImportInfo)


    const val CUSTOM_NAV_ARGS_FILE = "CustomNavArguments"
    const val NAV_DESTINATION_SUFFIX = "NavDestination"

    private val NAV_ARG_STRING_TYPE = ImportInfo("NavArgStringType", NAV_ARGS_PACKAGE)
    private val NAV_ARG_STRING_ARRAY_TYPE = ImportInfo("NavArgStringArrayType", NAV_ARGS_PACKAGE)
    private val NAV_ARG_STRING_LIST_TYPE = ImportInfo("NavArgStringListType", NAV_ARGS_PACKAGE)
    private val NAV_ARG_STRING_SET_TYPE = ImportInfo("NavArgStringSetType", NAV_ARGS_PACKAGE)

    private val NAV_ARG_CHAR_TYPE = ImportInfo("NavArgCharType", NAV_ARGS_PACKAGE)
    private val NAV_ARG_PRIMITIVE_CHAR_ARRAY_TYPE = ImportInfo("NavArgPrimitiveCharArrayType", NAV_ARGS_PACKAGE)
    private val NAV_ARG_CHAR_ARRAY_TYPE = ImportInfo("NavArgCharArrayType", NAV_ARGS_PACKAGE)
    private val NAV_ARG_CHAR_LIST_TYPE = ImportInfo("NavArgCharListType", NAV_ARGS_PACKAGE)
    private val NAV_ARG_CHAR_SET_TYPE = ImportInfo("NavArgCharSetType", NAV_ARGS_PACKAGE)

    private val NAV_ARG_LONG_TYPE = ImportInfo("NavArgLongType", NAV_ARGS_PACKAGE)
    private val NAV_ARG_PRIMITIVE_LONG_ARRAY_TYPE = ImportInfo("NavArgPrimitiveLongArrayType", NAV_ARGS_PACKAGE)
    private val NAV_ARG_LONG_ARRAY_TYPE = ImportInfo("NavArgLongArrayType", NAV_ARGS_PACKAGE)
    private val NAV_ARG_LONG_LIST_TYPE = ImportInfo("NavArgLongListType", NAV_ARGS_PACKAGE)
    private val NAV_ARG_LONG_SET_TYPE = ImportInfo("NavArgLongSetType", NAV_ARGS_PACKAGE)

    private val NAV_ARG_INT_TYPE = ImportInfo("NavArgIntType", NAV_ARGS_PACKAGE)
    private val NAV_ARG_PRIMITIVE_INT_ARRAY_TYPE = ImportInfo("NavArgPrimitiveIntArrayType", NAV_ARGS_PACKAGE)
    private val NAV_ARG_INT_ARRAY_TYPE = ImportInfo("NavArgIntArrayType", NAV_ARGS_PACKAGE)
    private val NAV_ARG_INT_LIST_TYPE = ImportInfo("NavArgIntListType", NAV_ARGS_PACKAGE)
    private val NAV_ARG_INT_SET_TYPE = ImportInfo("NavArgIntSetType", NAV_ARGS_PACKAGE)

    private val NAV_ARG_SHORT_TYPE = ImportInfo("NavArgShortType", NAV_ARGS_PACKAGE)
    private val NAV_ARG_PRIMITIVE_SHORT_ARRAY_TYPE = ImportInfo("NavArgPrimitiveShortArrayType", NAV_ARGS_PACKAGE)
    private val NAV_ARG_SHORT_ARRAY_TYPE = ImportInfo("NavArgShortArrayType", NAV_ARGS_PACKAGE)
    private val NAV_ARG_SHORT_LIST_TYPE = ImportInfo("NavArgShortListType", NAV_ARGS_PACKAGE)
    private val NAV_ARG_SHORT_SET_TYPE = ImportInfo("NavArgShortSetType", NAV_ARGS_PACKAGE)

    private val NAV_ARG_BYTE_TYPE = ImportInfo("NavArgByteType", NAV_ARGS_PACKAGE)
    private val NAV_ARG_PRIMITIVE_BYTE_ARRAY_TYPE = ImportInfo("NavArgPrimitiveByteArrayType", NAV_ARGS_PACKAGE)
    private val NAV_ARG_BYTE_ARRAY_TYPE = ImportInfo("NavArgByteArrayType", NAV_ARGS_PACKAGE)
    private val NAV_ARG_BYTE_LIST_TYPE = ImportInfo("NavArgByteListType", NAV_ARGS_PACKAGE)
    private val NAV_ARG_BYTE_SET_TYPE = ImportInfo("NavArgByteSetType", NAV_ARGS_PACKAGE)

    private val NAV_ARG_BOOLEAN_TYPE = ImportInfo("NavArgBooleanType", NAV_ARGS_PACKAGE)
    private val NAV_ARG_PRIMITIVE_BOOLEAN_ARRAY_TYPE = ImportInfo("NavArgPrimitiveBooleanArrayType", NAV_ARGS_PACKAGE)
    private val NAV_ARG_BOOLEAN_ARRAY_TYPE = ImportInfo("NavArgBooleanArrayType", NAV_ARGS_PACKAGE)
    private val NAV_ARG_BOOLEAN_LIST_TYPE = ImportInfo("NavArgBooleanListType", NAV_ARGS_PACKAGE)
    private val NAV_ARG_BOOLEAN_SET_TYPE = ImportInfo("NavArgBooleanSetType", NAV_ARGS_PACKAGE)

    private val NAV_ARG_FLOAT_TYPE = ImportInfo("NavArgFloatType", NAV_ARGS_PACKAGE)
    private val NAV_ARG_PRIMITIVE_FLOAT_ARRAY_TYPE = ImportInfo("NavArgPrimitiveFloatArrayType", NAV_ARGS_PACKAGE)
    private val NAV_ARG_FLOAT_ARRAY_TYPE = ImportInfo("NavArgFloatArrayType", NAV_ARGS_PACKAGE)
    private val NAV_ARG_FLOAT_LIST_TYPE = ImportInfo("NavArgFloatListType", NAV_ARGS_PACKAGE)
    private val NAV_ARG_FLOAT_SET_TYPE = ImportInfo("NavArgFloatSetType", NAV_ARGS_PACKAGE)

    private val NAV_ARG_DOUBLE_TYPE = ImportInfo("NavArgDoubleType", NAV_ARGS_PACKAGE)
    private val NAV_ARG_PRIMITIVE_DOUBLE_ARRAY_TYPE = ImportInfo("NavArgPrimitiveDoubleArrayType", NAV_ARGS_PACKAGE)
    private val NAV_ARG_DOUBLE_ARRAY_TYPE = ImportInfo("NavArgDoubleArrayType", NAV_ARGS_PACKAGE)
    private val NAV_ARG_DOUBLE_LIST_TYPE = ImportInfo("NavArgDoubleListType", NAV_ARGS_PACKAGE)
    private val NAV_ARG_DOUBLE_SET_TYPE = ImportInfo("NavArgDoubleSetType", NAV_ARGS_PACKAGE)

    val NAV_ARG_PARCELABLE_TYPE = ImportInfo("NavArgParcelableType", NAV_ARGS_PACKAGE)
    val NAV_ARG_PARCELABLE_ARRAY_TYPE = ImportInfo("NavArgParcelableArrayType", NAV_ARGS_PACKAGE)
    val NAV_ARG_PARCELABLE_LIST_TYPE = ImportInfo("NavArgParcelableListType", NAV_ARGS_PACKAGE)
    val NAV_ARG_PARCELABLE_SET_TYPE = ImportInfo("NavArgParcelableSetType", NAV_ARGS_PACKAGE)

    val NAV_ARG_ENUM_TYPE = ImportInfo("NavArgEnumType", NAV_ARGS_PACKAGE)
    val NAV_ARG_ENUM_ARRAY_TYPE = ImportInfo("NavArgEnumArrayType", NAV_ARGS_PACKAGE)
    val NAV_ARG_ENUM_LIST_TYPE = ImportInfo("NavArgEnumListType", NAV_ARGS_PACKAGE)
    val NAV_ARG_ENUM_SET_TYPE = ImportInfo("NavArgEnumSetType", NAV_ARGS_PACKAGE)

    val NAV_ARG_SERIALIZABLE_TYPE = ImportInfo("NavArgSerializableType", NAV_ARGS_PACKAGE)


    private val BASIC_NAV_ARG_TYPES = setOf(
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

    private fun findBasicNavArgWith(simpleName: String): ImportInfo? = BASIC_NAV_ARG_TYPES.firstOrNull { it.simpleName == simpleName }

    private const val NAV_ARGS_PREFIX = "NavArg"
    private const val NAV_ARGS_SUFFIX = "Type"
    private const val NAV_ARGS_PRIMITIVE_ARRAY_PREFIX = NAV_ARGS_PREFIX + "Primitive"
    private const val NAV_ARGS_ARRAY_SUFFIX = "Array$NAV_ARGS_SUFFIX"
    private const val NAV_ARGS_LIST_SUFFIX = "List$NAV_ARGS_SUFFIX"
    private const val NAV_ARGS_SET_SUFFIX = "Set$NAV_ARGS_SUFFIX"

    private fun generateBasicNavArgsForClass(
        clazz: KClass<*>,
        primitiveArrayClazz: KClass<*>? = null,
        name: String = clazz.simpleName!!
    ): List<Pair<ParameterTypeInfo, ImportInfo>> {
        val listType = findBasicNavArgWith(NAV_ARGS_PREFIX + name + NAV_ARGS_LIST_SUFFIX)!!
        val setType = findBasicNavArgWith(NAV_ARGS_PREFIX + name + NAV_ARGS_SET_SUFFIX)!!
        val listImports = VALID_LIST_IMPORT_INFOS.map { it.asParamTypeInfo(clazz) to listType }
        val setImports = VALID_SET_IMPORT_INFOS.map { it.asParamTypeInfo(clazz) to setType }

        return mutableListOf<Pair<ParameterTypeInfo, ImportInfo>>().apply {
            add(clazz.asParamTypeInfo() to findBasicNavArgWith(NAV_ARGS_PREFIX + name + NAV_ARGS_SUFFIX)!!)
            add(Array::class.asParamTypeInfo(clazz) to findBasicNavArgWith(NAV_ARGS_PREFIX + name + NAV_ARGS_ARRAY_SUFFIX)!!)
            addAll(listImports)
            addAll(setImports)
            primitiveArrayClazz?.let {
                add(it.asParameterTypeInfo() to findBasicNavArgWith(NAV_ARGS_PRIMITIVE_ARRAY_PREFIX + name + NAV_ARGS_ARRAY_SUFFIX)!!)
            }
        }
    }
}