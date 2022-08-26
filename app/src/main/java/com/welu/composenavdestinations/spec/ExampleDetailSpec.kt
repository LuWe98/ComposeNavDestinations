package com.welu.composenavdestinations.spec

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import com.welu.composenavdestinations.*
import com.welu.composenavdestinations.extensions.navigation.navArgument
import com.welu.composenavdestinations.navargs.*

object ExampleDetailSpec : NavDestinationSpec<DetailScreenNavArgs> {

    val parcelableNavType = NavArgParcelableType(ParcelableObject::class)

    val parcelableArrayListNavType = NavArgParcelableListType(ParcelableObject::class)

    val parcelableArrayNavType = NavArgParcelableArrayType(ParcelableObject::class)

    val testEnumNavType = NavArgEnumType(TestEnum::class)

    val testEnumArrayListNavType = NavArgEnumListType(TestEnum::class)

    operator fun invoke(
        string: String = "Hallo",
        int: Int = 46,
        long: Long = 32,
        float: Float = 21f,
        boolean: Boolean = false,
        enum: TestEnum = TestEnum.HALLO,
        parcelable: ParcelableObject = ParcelableObject("313", "Hans", 21),
        byte: Byte = 2,
        short: Short = 2132,
        stringArray: Array<String?> = arrayOf("hallo", "tschau", ""),
        intArray: IntArray = intArrayOf(21, 22, 23),
        longArray: LongArray = longArrayOf(24, 25, 26),
        floatArray: FloatArray = floatArrayOf(1f, 2f),
        booleanArray: BooleanArray = booleanArrayOf(true, true, false),
        parcelableArray: Array<ParcelableObject?> = arrayOf(ParcelableObject("1", "Sasi", 23), ParcelableObject("2", "Lucha", 23)),
        parcelableArrayList: List<ParcelableObject?> = listOf(),
        stringArrayList: List<String> = listOf("Hallo", "Bernd"),
        intArrayList: List<Int?> = listOf(21, 221, 2221),
        longArrayList: List<Long?> = listOf(32, 12, 4324),
        floatArrayList: List<Float?> = listOf(212f, 323f, 32f),
        booleanArrayList: List<Boolean> = listOf(true, false, true),
        enumArrayList: List<TestEnum> = TestEnum.values().asList(),
        stringSet: Set<String>? = setOf("12", "12")
    ) = Routable(
        baseRoute +
                "/${NavArgStringType.serializeValue(string)}" +
                "/${NavArgIntType.serializeValue(int)}" +
                "/${NavArgLongType.serializeValue(long)}" +
                "/${NavArgFloatType.serializeValue(float)}" +
                "/${NavArgBooleanType.serializeValue(boolean)}" +
                "/${testEnumNavType.serializeValue(enum)}" +
                "/${parcelableNavType.serializeValue(parcelable)}" +
                "/${NavArgByteType.serializeValue(byte)}" +
                "/${NavArgShortType.serializeValue(short)}" +
                "/${NavArgStringArrayType.serializeValue(stringArray)}" +
                "/${NavArgIntArrayType.serializeValue(intArray)}" +
                "/${NavArgLongArrayType.serializeValue(longArray)}" +
                "/${NavArgFloatArrayType.serializeValue(floatArray)}" +
                "/${NavArgBooleanArrayType.serializeValue(booleanArray)}" +
                "/${parcelableArrayNavType.serializeValue(parcelableArray)}" +
                "/${parcelableArrayListNavType.serializeValue(parcelableArrayList)}" +
                "/${NavArgStringListType.serializeValue(stringArrayList)}" +
                "/${NavArgIntListType.serializeValue(intArrayList)}" +
                "/${NavArgLongListType.serializeValue(longArrayList)}" +
                "/${NavArgFloatListType.serializeValue(floatArrayList)}" +
                "/${NavArgBooleanListType.serializeValue(booleanArrayList)}" +
                "/${testEnumArrayListNavType.serializeValue(enumArrayList)}" +
                "?stringSet=${NavArgStringSetType.serializeValue(stringSet)}"
    )

    override val baseRoute: String = "details"

    override val route: String
        get() = baseRoute +
                "/{string}" +
                "/{int}" +
                "/{long}" +
                "/{float}" +
                "/{boolean}" +
                "/{enum}" +
                "/{parcelable}" +
                "/{byte}" +
                "/{short}" +
                "/{stringArray}" +
                "/{intArray}" +
                "/{longArray}" +
                "/{floatArray}" +
                "/{booleanArray}" +
                "/{parcelableArray}" +
                "/{parcelableList}" +
                "/{stringList}" +
                "/{intList}" +
                "/{longList}" +
                "/{floatList}" +
                "/{booleanList}" +
                "/{enumList}" +
                "?stringSet={stringSet}"

    override val arguments: List<NamedNavArgument>
        get() = listOf(
            navArgument("string", NavArgStringType),
            navArgument("int", NavArgIntType),
            navArgument("long", NavArgLongType),
            navArgument("float", NavArgFloatType),
            navArgument("boolean", NavArgBooleanType),
            navArgument("enum", testEnumNavType),
            navArgument("parcelable", parcelableNavType),
            navArgument("byte", NavArgByteType),
            navArgument("short", NavArgShortType),
            navArgument("stringArray", NavArgStringArrayType),
            navArgument("intArray", NavArgIntArrayType),
            navArgument("longArray", NavArgLongArrayType),
            navArgument("floatArray", NavArgFloatArrayType),
            navArgument("booleanArray", NavArgBooleanArrayType),
            navArgument("parcelableArray", parcelableArrayNavType),
            navArgument("parcelableList", parcelableArrayListNavType),
            navArgument("stringList", NavArgStringListType),
            navArgument("intList", NavArgIntListType),
            navArgument("longList", NavArgLongListType),
            navArgument("floatList", NavArgFloatListType),
            navArgument("booleanList", NavArgBooleanListType),
            navArgument("enumList", testEnumArrayListNavType),
            navArgument("stringSet", NavArgStringSetType, true)
        )


    override fun getArgs(navBackStackEntry: NavBackStackEntry): DetailScreenNavArgs = navBackStackEntry.run {
        DetailScreenNavArgs(
            getTyped("string")!!,
            getTyped("int")!!,
            getTyped("long")!!,
            getTyped("float")!!,
            getTyped("boolean")!!,
            getTyped("enum")!!,
            getTyped("parcelable")!!,
            getTyped("byte")!!,
            getTyped("short")!!,
            getTyped("stringArray")!!,
            getTyped("intArray")!!,
            getTyped("longArray")!!,
            getTyped("floatArray")!!,
            getTyped("booleanArray")!!,
            getTyped("parcelableArray")!!,
            getTyped("parcelableList")!!,
            getTyped("stringList")!!,
            getTyped("intList")!!,
            getTyped("longList")!!,
            getTyped("floatList")!!,
            getTyped("booleanList")!!,
            getTyped("enumList")!!,
            getTyped("stringSet")
        )
    }

    override fun getArgs(savedStateHandle: SavedStateHandle): DetailScreenNavArgs {
        TODO("Not yet implemented")
    }

}