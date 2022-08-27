package com.welu.composenavdestinations.spec

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import com.welu.composenavdestinations.*
import com.welu.composenavdestinations.extensions.navigation.navArgument
import com.welu.composenavdestinations.navargs.*
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

object ExampleDetailSpec : NavDestinationSpec<DetailScreenNavArgs> {

    val parcelableNavType = NavArgParcelableType(ParcelableObject::class)

    val parcelableArrayListNavType = NavArgParcelableListType(ParcelableObject::class)

    val parcelableArrayNavType = NavArgParcelableArrayType(ParcelableObject::class)

    val testEnumNavType = NavArgEnumType(TestEnum::class)

    val testEnumArrayNavType = NavArgEnumArrayType(TestEnum::class)

    val testEnumArrayListNavType = NavArgEnumListType(TestEnum::class)


    operator fun invoke(
        string: String = "Hallo",
        stringArray: Array<String?> = arrayOf("hallo", "tschau", ""),
        stringList: List<String> = listOf("Hallo", "Bernd"),
        stringSet: Set<String>? = hashSetOf("ds"),

        int: Int = 46,
        intArray: IntArray = intArrayOf(21, 22, 23),
        intList: List<Int?> = listOf(21, 221, 2221),

        long: Long = 32,
        longArray: LongArray = longArrayOf(24, 25, 26),
        longList: List<Long?> = listOf(32, 12, 4324),

        float: Float = 21f,
        floatArray: FloatArray = floatArrayOf(1f, 2f),
        floatList: List<Float?> = listOf(212f, 323f, 32f),

        double: Double = 21.43,
        doubleArray: DoubleArray = doubleArrayOf(1.4, 2.3),
        doubleList: ArrayList<Double> = arrayListOf(),

        boolean: Boolean = false,
        booleanArray: BooleanArray = booleanArrayOf(true, true, false),
        booleanList: List<Boolean> = listOf(true, false, true),

        byte: Byte = 2,
        byteArray: ByteArray = byteArrayOf(21),
        byteList: List<Byte> = listOf(12),

        short: Short = 2132,
        shortArray: ShortArray = shortArrayOf(221),
        shortList: List<Short> = listOf(23),

        char: Char = ' ',
        charArray: CharArray = charArrayOf('s', 'a'),
        charList: List<Char> = listOf('a'),

        enum: TestEnum = TestEnum.HALLO,
        enumArray: Array<TestEnum> = TestEnum.values(),
        enumList: List<TestEnum> = TestEnum.values().asList(),

        parcelable: ParcelableObject = ParcelableObject("313", "Hans", 21),
        parcelableArray: Array<ParcelableObject?> = arrayOf(ParcelableObject("1", "Sasi", 23), ParcelableObject("2", "Lucha", 23)),
        parcelableList: List<ParcelableObject?> = listOf(),

        serializable: UUID = UUID.randomUUID(),

        map: HashMap<String, Int> = hashMapOf("hallo" to 2)
    ) = Routable(
        baseRoute +
                "/${NavArgStringType.serializeValue(string)}" +
                "/${NavArgStringArrayType.serializeValue(stringArray)}" +
                "/${NavArgStringListType.serializeValue(stringList)}" +
                "/${NavArgStringSetType.serializeValue(stringSet)}" +

                "/${NavArgIntType.serializeValue(int)}" +
                "/${NavArgIntArrayType.serializeValue(intArray)}" +
                "/${NavArgIntListType.serializeValue(intList)}" +

                "/${NavArgLongType.serializeValue(long)}" +
                "/${NavArgLongArrayType.serializeValue(longArray)}" +
                "/${NavArgLongListType.serializeValue(longList)}" +

                "/${NavArgFloatType.serializeValue(float)}" +
                "/${NavArgFloatArrayType.serializeValue(floatArray)}" +
                "/${NavArgFloatListType.serializeValue(floatList)}" +

                "/${NavArgDoubleType.serializeValue(double)}" +
                "/${NavArgDoubleArrayType.serializeValue(doubleArray)}" +
                "/${NavArgDoubleListType.serializeValue(doubleList)}" +

                "/${NavArgBooleanType.serializeValue(boolean)}" +
                "/${NavArgBooleanArrayType.serializeValue(booleanArray)}" +
                "/${NavArgBooleanListType.serializeValue(booleanList)}" +

                "/${NavArgByteType.serializeValue(byte)}" +
                "/${NavArgByteArrayType.serializeValue(byteArray)}" +
                "/${NavArgByteListType.serializeValue(byteList)}" +

                "/${NavArgShortType.serializeValue(short)}" +
                "/${NavArgShortArrayType.serializeValue(shortArray)}" +
                "/${NavArgShortListType.serializeValue(shortList)}" +

                "/${NavArgCharType.serializeValue(char)}" +
                "/${NavArgCharArrayType.serializeValue(charArray)}" +
                "/${NavArgCharListType.serializeValue(charList)}" +

                "/${testEnumNavType.serializeValue(enum)}" +
                "/${testEnumArrayNavType.serializeValue(enumArray)}" +
                "/${testEnumArrayListNavType.serializeValue(enumList)}" +

                "/${parcelableNavType.serializeValue(parcelable)}" +
                "/${parcelableArrayNavType.serializeValue(parcelableArray)}" +
                "/${parcelableArrayListNavType.serializeValue(parcelableList)}" +

                "/${NavArgSerializableType.serializeValue(serializable)}" +
                "/${NavArgSerializableType.serializeValue(map)}"
    )

    override val baseRoute: String = "details"

    override val route: String
        get() = baseRoute +
                "/{string}" +
                "/{stringArray}" +
                "/{stringList}" +
                "/{stringSet}" +

                "/{int}" +
                "/{intArray}" +
                "/{intList}" +

                "/{long}" +
                "/{longArray}" +
                "/{longList}" +

                "/{float}" +
                "/{floatArray}" +
                "/{floatList}" +

                "/{double}" +
                "/{doubleArray}" +
                "/{doubleList}" +

                "/{boolean}" +
                "/{booleanArray}" +
                "/{booleanList}" +

                "/{byte}" +
                "/{byteArray}" +
                "/{byteList}" +

                "/{short}" +
                "/{shortArray}" +
                "/{shortList}" +

                "/{char}" +
                "/{charArray}" +
                "/{charList}" +

                "/{enum}" +
                "/{enumArray}" +
                "/{enumList}" +

                "/{parcelable}" +
                "/{parcelableArray}" +
                "/{parcelableList}" +

                "/{serializable}" +
                "/{map}"

    override val arguments: List<NamedNavArgument>
        get() = listOf(
            navArgument("string", NavArgStringType),
            navArgument("stringArray", NavArgStringArrayType),
            navArgument("stringList", NavArgStringListType),
            navArgument("stringSet", NavArgStringSetType),

            navArgument("int", NavArgIntType),
            navArgument("intArray", NavArgIntArrayType),
            navArgument("intList", NavArgIntListType),

            navArgument("long", NavArgLongType),
            navArgument("longArray", NavArgLongArrayType),
            navArgument("longList", NavArgLongListType),

            navArgument("float", NavArgFloatType),
            navArgument("floatArray", NavArgFloatArrayType),
            navArgument("floatList", NavArgFloatListType),

            navArgument("double", NavArgDoubleType),
            navArgument("doubleArray", NavArgDoubleArrayType),
            navArgument("doubleList", NavArgDoubleListType),

            navArgument("boolean", NavArgBooleanType),
            navArgument("booleanArray", NavArgBooleanArrayType),
            navArgument("booleanList", NavArgBooleanListType),

            navArgument("byte", NavArgByteType),
            navArgument("byteArray", NavArgByteArrayType),
            navArgument("byteList", NavArgByteListType),

            navArgument("short", NavArgShortType),
            navArgument("shortArray", NavArgShortArrayType),
            navArgument("shortList", NavArgShortListType),

            navArgument("char", NavArgCharType),
            navArgument("charArray", NavArgCharArrayType),
            navArgument("charList", NavArgCharListType),

            navArgument("enum", testEnumNavType),
            navArgument("enumArray", testEnumArrayNavType),
            navArgument("enumList", testEnumArrayListNavType),

            navArgument("parcelable", parcelableNavType),
            navArgument("parcelableArray", parcelableArrayNavType),
            navArgument("parcelableList", parcelableArrayListNavType),

            navArgument("serializable", NavArgSerializableType),
            navArgument("map", NavArgSerializableType)
        )

    override fun getArgs(navBackStackEntry: NavBackStackEntry): DetailScreenNavArgs = navBackStackEntry.run {
        DetailScreenNavArgs(
            string = getTyped("string")!!,
            stringArray = getTyped("stringArray")!!,
            stringList = getTyped("stringList")!!,
            stringSet = getTyped("stringSet")!!,

            int = getTyped("int")!!,
            intArray = getTyped("intArray")!!,
            intList = getTyped("intList")!!,

            long = getTyped("long")!!,
            longArray = getTyped("longArray")!!,
            longList = getTyped("longList")!!,

            float = getTyped("float")!!,
            floatArray = getTyped("floatArray")!!,
            floatList = getTyped("floatList")!!,

            double = getTyped("double")!!,
            doubleArray = getTyped("doubleArray")!!,
            doubleList = getTyped("doubleList")!!,

            boolean = getTyped("boolean")!!,
            booleanArray = getTyped("booleanArray")!!,
            booleanList = getTyped("booleanList")!!,

            byte = getTyped("byte")!!,
            byteArray = getTyped("byteArray")!!,
            byteList = getTyped("byteList")!!,

            short = getTyped("short")!!,
            shortArray = getTyped("shortArray")!!,
            shortList = getTyped("shortList")!!,

            enum = getTyped("enum")!!,
            enumArray = getTyped("enumArray")!!,
            enumList = getTyped("enumList")!!,

            parcelable = getTyped("parcelable")!!,
            parcelableArray = getTyped("parcelableArray")!!,
            parcelableList = getTyped("parcelableList")!!,

            serializable = getTyped("serializable")!!,
            map = getTyped("map")!!
        )
    }

    override fun getArgs(savedStateHandle: SavedStateHandle): DetailScreenNavArgs {
        TODO("Not yet implemented")
    }

}