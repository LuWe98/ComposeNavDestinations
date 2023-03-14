package com.welu.compose_nav_destinations_ksp.utils

import com.welu.compose_nav_destinations_ksp.extensions.asParameterTypeInfo
import com.welu.compose_nav_destinations_ksp.model.ImportInfo
import com.welu.compose_nav_destinations_ksp.model.ParameterTypeInfo
import com.welu.compose_nav_destinations_ksp.model.navargs.BasicParameterNavArgType
import com.welu.compose_nav_destinations_ksp.model.navargs.ParameterNavArgType
import kotlin.reflect.KClass

internal object PackageUtils {

    const val NAV_COMPONENT_SPEC_SUFFIX = "Spec"

    const val PACKAGE_NAME = "com.welu.composenavdestinations"
    const val ANNOTATIONS_PACKAGE = "$PACKAGE_NAME.annotations"
    const val NAV_GRAPH_SPEC_PACKAGE = "$PACKAGE_NAME.navgraphs"
    const val NAV_DESTINATION_SPEC_PACKAGE = "$PACKAGE_NAME.destinations"

    const val NAV_ARGS_PACKAGE = "$PACKAGE_NAME.navargs"
    val NAV_DESTINATION_CUSTOM_NAV_ARGS_FILE_IMPORT = ImportInfo("NavDestinationCustomNavArgs", NAV_ARGS_PACKAGE)

    private const val NAV_DESTINATIONS_UTILS_PACKAGE = "$PACKAGE_NAME.utils"
    val NAV_COMPONENT_UTILS_FILE_IMPORT = ImportInfo("NavComponentUtils", NAV_DESTINATIONS_UTILS_PACKAGE)

    const val NAV_DESTINATIONS_EXTENSIONS_PACKAGE = "$PACKAGE_NAME.extensions"
    val NAV_DESTINATION_EXTENSIONS_FILE_IMPORT = ImportInfo("NavDestinationExt", NAV_DESTINATIONS_EXTENSIONS_PACKAGE)
    val NAV_DESTINATION_RESULT_EXTENSIONS_FILE_IMPORT = ImportInfo("NavDestinationResultExt", NAV_DESTINATIONS_EXTENSIONS_PACKAGE)
    val NAV_CONTROLLER_EXTENSIONS_FILE_IMPORT = ImportInfo("NavControllerExt", NAV_DESTINATIONS_EXTENSIONS_PACKAGE)
    val NAV_BACK_STACK_ENTRY_EXTENSIONS_FILE_IMPORT = ImportInfo("NavBackStackEntryExt", NAV_DESTINATIONS_EXTENSIONS_PACKAGE)
    val NAV_COMPOSE_DESTINATION_VAL_ROUTE_EXTENSION = ImportInfo("route", NAV_DESTINATIONS_EXTENSIONS_PACKAGE)
    val COMPOSE_NAV_DESTINATIONS_EXTENSION_FILE_IMPORT = ImportInfo("ComposeNavDestinationsExt", NAV_DESTINATIONS_EXTENSIONS_PACKAGE)


    private const val NAV_DESTINATION_NAVIGATION_PACKAGE = "$PACKAGE_NAME.navigation"
    val ROUTABLE_IMPORT = ImportInfo("Routable", NAV_DESTINATION_NAVIGATION_PACKAGE)
    val COMPOSE_NAV_DESTINATIONS_IMPORT = ImportInfo("ComposeNavDestinations", NAV_DESTINATION_NAVIGATION_PACKAGE)

    private const val NAV_DESTINATION_DESTINATIONS_PACKAGE = "$NAV_DESTINATION_NAVIGATION_PACKAGE.destinations"
    val NAV_COMPOSE_SEALED_DESTINATION_IMPORT = ImportInfo("ComposeDestination", NAV_DESTINATION_DESTINATIONS_PACKAGE)
    val NAV_ROUTABLE_COMPOSE_DESTINATION_IMPORT = ImportInfo("ComposeRoutableDestination", NAV_DESTINATION_DESTINATIONS_PACKAGE)
    val NAV_ARG_COMPOSE_DESTINATION_IMPORT = ImportInfo("ComposeArgDestination", NAV_DESTINATION_DESTINATIONS_PACKAGE)
    val NAV_ARG_DESTINATION_IMPORT = ImportInfo("ArgDestination", NAV_DESTINATION_DESTINATIONS_PACKAGE)
    val NAV_DESTINATION_IMPORT = ImportInfo("Destination", NAV_DESTINATION_DESTINATIONS_PACKAGE)
    val NAV_DIALOG_DESTINATION_IMPORT = ImportInfo("DialogDestination", NAV_DESTINATION_DESTINATIONS_PACKAGE)
    val NAV_DIALOG_ARG_DESTINATION_IMPORT = ImportInfo("DialogArgDestination", NAV_DESTINATION_DESTINATIONS_PACKAGE)
    val NAV_BOTTOM_SHEET_DESTINATION_IMPORT = ImportInfo("BottomSheetDestination", NAV_DESTINATION_DESTINATIONS_PACKAGE)
    val NAV_BOTTOM_SHEET_ARG_DESTINATION_IMPORT = ImportInfo("BottomSheetArgDestination", NAV_DESTINATION_DESTINATIONS_PACKAGE)

    private const val NAV_DESTINATION_RESULT_PACKAGE = "$PACKAGE_NAME.result"
    val NAV_DESTINATION_RESULT_IMPORT = ImportInfo("DestinationResult", NAV_DESTINATION_RESULT_PACKAGE)

    //TODO -> Alle Destinations in einen Folder machen und diese GeneratedDestination implementieren lassen.
    // -> Das gleiche mit NavGraphs -> GeneratedNavGraph
    private const val NAV_COMPONENT_SPEC_PACKAGE = "$NAV_DESTINATION_NAVIGATION_PACKAGE.spec"
    val NAV_COMPONENT_SPEC_IMPORT = ImportInfo("NavComponentSpec", NAV_COMPONENT_SPEC_PACKAGE)
    val NAV_COMPOSE_DESTINATION_SPEC_IMPORT = ImportInfo("ComposeDestinationSpec", NAV_COMPONENT_SPEC_PACKAGE)
    val NAV_ROUTABLE_COMPOSE_DESTINATION_SPEC_IMPORT = ImportInfo("ComposeRoutableDestinationSpec", NAV_COMPONENT_SPEC_PACKAGE)
    val NAV_ARG_COMPOSE_DESTINATION_SPEC_IMPORT = ImportInfo("ComposeArgDestinationSpec", NAV_COMPONENT_SPEC_PACKAGE)
    val NAV_DESTINATION_SPEC_IMPORT= ImportInfo("DestinationSpec", NAV_COMPONENT_SPEC_PACKAGE)
    val NAV_DESTINATION_ARG_SPEC_IMPORT = ImportInfo("ArgDestinationSpec", NAV_COMPONENT_SPEC_PACKAGE)
    val NAV_DIALOG_DESTINATION_SPEC_IMPORT = ImportInfo("DialogDestinationSpec", NAV_COMPONENT_SPEC_PACKAGE)
    val NAV_DIALOG_ARG_DESTINATION_SPEC_IMPORT = ImportInfo("DialogArgDestinationSpec", NAV_COMPONENT_SPEC_PACKAGE)
    val NAV_BOTTOM_SHEET_DESTINATION_SPEC_IMPORT = ImportInfo("BottomSheetDestinationSpec", NAV_COMPONENT_SPEC_PACKAGE)
    val NAV_BOTTOM_SHEET_ARG_DESTINATION_SPEC_IMPORT = ImportInfo("BottomSheetArgDestinationSpec", NAV_COMPONENT_SPEC_PACKAGE)
    val NAV_COMPOSE_GRAPH_SPEC_IMPORT = ImportInfo("ComposeNavGraphSpec", NAV_COMPONENT_SPEC_PACKAGE)
    val NAV_GRAPH_SPEC_IMPORT = ImportInfo("NavGraphSpec", NAV_COMPONENT_SPEC_PACKAGE)
    val NAV_GRAPH_SPEC_ARG_IMPORT = ImportInfo("ArgNavGraphSpec", NAV_COMPONENT_SPEC_PACKAGE)

    private const val NAV_DESTINATION_SCOPE_PACKAGE = "$NAV_DESTINATION_NAVIGATION_PACKAGE.scope"
    val NAV_COMPOSE_DESTINATION_SCOPE_IMPORT = ImportInfo("ComposeDestinationScope", NAV_DESTINATION_SCOPE_PACKAGE)
    val NAV_DESTINATION_SCOPE_IMPORT= ImportInfo("DestinationScope", NAV_DESTINATION_SCOPE_PACKAGE)
    val NAV_DESTINATION_ARG_SCOPE_IMPORT = ImportInfo("ArgDestinationScope", NAV_DESTINATION_SCOPE_PACKAGE)

    private const val NAV_DESTINATIONS_RESULT_PACKAGE = "$PACKAGE_NAME.result"
    val NAV_DESTINATION_SEND_DESTINATION_RESULT_FUNCTION_IMPORT = ImportInfo("sendDestinationResultTo", NAV_DESTINATIONS_RESULT_PACKAGE)
    val NAV_DESTINATION_RESULT_LISTENER_IMPORT = ImportInfo("DestinationResultListener",NAV_DESTINATIONS_RESULT_PACKAGE)
    val NAV_DESTINATION_LIFECYCLE_RESULT_LISTENER_IMPORT = ImportInfo("LifecycleDestinationResultListener",NAV_DESTINATIONS_RESULT_PACKAGE)



    val ANDROID_PARCELABLE_IMPORT = ImportInfo("Parcelable","android.os")
    val ANDROID_NAVIGATION_DEEP_LINK_IMPORT = ImportInfo("NavDeepLink","androidx.navigation")
    val ANDROID_NAVIGATION_NAV_BACK_STACK_ENTRY_IMPORT = ImportInfo("NavBackStackEntry","androidx.navigation")
    val ANDROID_NAVIGATION_NAMED_NAV_ARGUMENT_IMPORT = ImportInfo("NamedNavArgument", "androidx.navigation")
    val ANDROID_NAVIGATION_NAV_CONTROLLER_IMPORT = ImportInfo("NavController", "androidx.navigation")
    val ANDROID_NAVIGATION_NAV_OPTIONS_BUILDER_IMPORT = ImportInfo("NavOptionsBuilder", "androidx.navigation")
    val ANDROID_NAVIGATION_NAV_OPTIONS_IMPORT = ImportInfo("NavOptions", "androidx.navigation")
    val ANDROID_NAVIGATION_NAVIGATOR_IMPORT = ImportInfo("Navigator", "androidx.navigation")
    val ANDROID_COMPOSABLE_IMPORT = ImportInfo("Composable","androidx.compose.runtime")
    val ANDROID_LIVECYCLE_IMPORT = ImportInfo("Lifecycle","androidx.lifecycle")
    val SAVED_STATE_HANDLE_IMPORT = ImportInfo("SavedStateHandle","androidx.lifecycle")
    val NAV_ARGUMENT_IMPORT = ImportInfo("navArgument","com.welu.composenavdestinations.extensions.navigation")
    val KOTLIN_SERIALIZABLE_IMPORT = ImportInfo("Serializable", "kotlinx.serialization")

    val KOTLIN_DEFAULT_PACKAGE_DIRECTORIES = arrayOf(
        "kotlin",
        "kotlin.annotation",
        "kotlin.collections",
        "kotlin.comparisons",
        "kotlin.io",
        "kotlin.ranges",
        "kotlin.sequences",
        "kotlin.text"
    )


    //TODO -> Das unten noch auslagern

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

    val MAP_QUALIFIERS = arrayOf(
        HashMap::class.qualifiedName!!,
        java.util.HashMap::class.qualifiedName!!,
        AbstractMap::class.qualifiedName!!,
        java.util.AbstractMap::class.qualifiedName!!,
        LinkedHashMap::class.qualifiedName!!,
        java.util.LinkedHashMap::class.qualifiedName!!
    )

    val BASIC_NAV_ARGS_MAP: Map<ParameterTypeInfo, ParameterNavArgType> = setOf(
        generateBasicNavArgsForClass(String::class),
        generateBasicNavArgsForClass(Char::class, CharArray::class),
        generateBasicNavArgsForClass(Boolean::class, BooleanArray::class),
        generateBasicNavArgsForClass(Byte::class, ByteArray::class),
        generateBasicNavArgsForClass(Short::class, ShortArray::class),
        generateBasicNavArgsForClass(Int::class, IntArray::class),
        generateBasicNavArgsForClass(Long::class, LongArray::class),
        generateBasicNavArgsForClass(Float::class, FloatArray::class),
        generateBasicNavArgsForClass(Double::class, DoubleArray::class),
    ).flatten().toMap()

    private const val NAV_ARGS_PRIMITIVE_ARRAY_PREFIX = "Primitive"
    private const val NAV_ARGS_SUFFIX = "Type"
    private const val NAV_ARGS_ARRAY_SUFFIX = "ArrayType"
    private const val NAV_ARGS_LIST_SUFFIX = "ListType"
    private const val NAV_ARGS_SET_SUFFIX = "SetType"

    private fun generateBasicNavArgsForClass(
        clazz: KClass<*>,
        primitiveArrayClazz: KClass<*>? = null,
        name: String = clazz.simpleName!!
    ): List<Pair<ParameterTypeInfo, ParameterNavArgType>> {
        val listType = BasicParameterNavArgType.valueOf(name + NAV_ARGS_LIST_SUFFIX)
        val setType = BasicParameterNavArgType.valueOf(name + NAV_ARGS_SET_SUFFIX)
        val listImports = VALID_LIST_IMPORT_INFOS.map { it.asParameterTypeInfo(clazz) to listType }
        val setImports = VALID_SET_IMPORT_INFOS.map { it.asParameterTypeInfo(clazz) to setType }

        return mutableListOf<Pair<ParameterTypeInfo, ParameterNavArgType>>(
            clazz.asParameterTypeInfo() to BasicParameterNavArgType.valueOf(name + NAV_ARGS_SUFFIX),
            Array::class.asParameterTypeInfo(clazz) to BasicParameterNavArgType.valueOf(name + NAV_ARGS_ARRAY_SUFFIX)
        ).apply {
            addAll(listImports)
            addAll(setImports)
            primitiveArrayClazz?.let {
                add(it.asParameterTypeInfo() to BasicParameterNavArgType.valueOf(NAV_ARGS_PRIMITIVE_ARRAY_PREFIX + name + NAV_ARGS_ARRAY_SUFFIX))
            }
        }
    }
}