package com.welu.composenavdestinations.spec

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import com.welu.composenavdestinations.*
import com.welu.composenavdestinations.extensions.navigation.navArgument
import com.welu.composenavdestinations.navargs.*
import java.util.*
import kotlin.collections.HashMap

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
        intPrimitiveArray: IntArray = intArrayOf(21, 22, 23),
        intArray: Array<Int> = arrayOf(2123),
        intList: List<Int?> = listOf(21, 221, 2221),

        long: Long = 32,
        longPrimitiveArray: LongArray = longArrayOf(24, 25, 26),
        longArray: Array<Long> = arrayOf(242587826),
        longList: List<Long?> = listOf(32, 12, 4324),

        float: Float = 21f,
        floatPrimitiveArray: FloatArray = floatArrayOf(1f, 2f),
        floatArray: Array<Float> = arrayOf(1434f),
        floatList: List<Float?> = listOf(212f, 323f, 32f),

        double: Double = 21.43,
        doublePrimitiveArray: DoubleArray = doubleArrayOf(1.4, 2.3),
        doubleArray: Array<Double> = arrayOf(1.476),
        doubleList: ArrayList<Double> = arrayListOf(),

        boolean: Boolean = false,
        booleanPrimitiveArray: BooleanArray = booleanArrayOf(true, true, false),
        booleanArray: Array<Boolean> = arrayOf(true, false),
        booleanList: List<Boolean> = listOf(true, false, true),

        byte: Byte = 2,
        bytePrimitiveArray: ByteArray = byteArrayOf(21),
        byteArray: Array<Byte> = arrayOf(21, 12),
        byteList: List<Byte> = listOf(12),

        short: Short = 2132,
        shortPrimitiveArray: ShortArray = shortArrayOf(221),
        shortArray: Array<Short> = arrayOf(221, 222, 223),
        shortList: List<Short> = listOf(23),

        char: Char = ' ',
        charPrimitiveArray: CharArray = charArrayOf('s', 'a'),
        charArray: Array<Char> = arrayOf('f'),
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
                "/${NavArgPrimitiveIntArrayType.serializeValue(intPrimitiveArray)}" +
                "/${NavArgIntArrayType.serializeValue(intArray)}" +
                "/${NavArgIntListType.serializeValue(intList)}" +

                "/${NavArgLongType.serializeValue(long)}" +
                "/${NavArgPrimitiveLongArrayType.serializeValue(longPrimitiveArray)}" +
                "/${NavArgLongArrayType.serializeValue(longArray)}" +
                "/${NavArgLongListType.serializeValue(longList)}" +

                "/${NavArgFloatType.serializeValue(float)}" +
                "/${NavArgPrimitiveFloatArrayType.serializeValue(floatPrimitiveArray)}" +
                "/${NavArgFloatArrayType.serializeValue(floatArray)}" +
                "/${NavArgFloatListType.serializeValue(floatList)}" +

                "/${NavArgDoubleType.serializeValue(double)}" +
                "/${NavArgPrimitiveDoubleArrayType.serializeValue(doublePrimitiveArray)}" +
                "/${NavArgDoubleArrayType.serializeValue(doubleArray)}" +
                "/${NavArgDoubleListType.serializeValue(doubleList)}" +

                "/${NavArgBooleanType.serializeValue(boolean)}" +
                "/${NavArgPrimitiveBooleanArrayType.serializeValue(booleanPrimitiveArray)}" +
                "/${NavArgBooleanArrayType.serializeValue(booleanArray)}" +
                "/${NavArgBooleanListType.serializeValue(booleanList)}" +

                "/${NavArgByteType.serializeValue(byte)}" +
                "/${NavArgPrimitiveByteArrayType.serializeValue(bytePrimitiveArray)}" +
                "/${NavArgByteArrayType.serializeValue(byteArray)}" +
                "/${NavArgByteListType.serializeValue(byteList)}" +

                "/${NavArgShortType.serializeValue(short)}" +
                "/${NavArgPrimitiveShortArrayType.serializeValue(shortPrimitiveArray)}" +
                "/${NavArgShortArrayType.serializeValue(shortArray)}" +
                "/${NavArgShortListType.serializeValue(shortList)}" +

                "/${NavArgCharType.serializeValue(char)}" +
                "/${NavArgPrimitiveCharArrayType.serializeValue(charPrimitiveArray)}" +
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
                "/{intPrimitiveArray}" +
                "/{intArray}" +
                "/{intList}" +

                "/{long}" +
                "/{longPrimitiveArray}" +
                "/{longArray}" +
                "/{longList}" +

                "/{float}" +
                "/{floatPrimitiveArray}" +
                "/{floatArray}" +
                "/{floatList}" +

                "/{double}" +
                "/{doublePrimitiveArray}" +
                "/{doubleArray}" +
                "/{doubleList}" +

                "/{boolean}" +
                "/{booleanPrimitiveArray}" +
                "/{booleanArray}" +
                "/{booleanList}" +

                "/{byte}" +
                "/{bytePrimitiveArray}" +
                "/{byteArray}" +
                "/{byteList}" +

                "/{short}" +
                "/{shortPrimitiveArray}" +
                "/{shortArray}" +
                "/{shortList}" +

                "/{char}" +
                "/{charPrimitiveArray}" +
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
            navArgument("intPrimitiveArray", NavArgPrimitiveIntArrayType),
            navArgument("intArray", NavArgIntArrayType),
            navArgument("intList", NavArgIntListType),

            navArgument("long", NavArgLongType),
            navArgument("longPrimitiveArray", NavArgPrimitiveLongArrayType),
            navArgument("longArray", NavArgLongArrayType),
            navArgument("longList", NavArgLongListType),

            navArgument("float", NavArgFloatType),
            navArgument("floatPrimitiveArray", NavArgPrimitiveFloatArrayType),
            navArgument("floatArray", NavArgFloatArrayType),
            navArgument("floatList", NavArgFloatListType),

            navArgument("double", NavArgDoubleType),
            navArgument("doublePrimitiveArray", NavArgPrimitiveDoubleArrayType),
            navArgument("doubleArray", NavArgDoubleArrayType),
            navArgument("doubleList", NavArgDoubleListType),

            navArgument("boolean", NavArgBooleanType),
            navArgument("booleanPrimitiveArray", NavArgPrimitiveBooleanArrayType),
            navArgument("booleanArray", NavArgBooleanArrayType),
            navArgument("booleanList", NavArgBooleanListType),

            navArgument("byte", NavArgByteType),
            navArgument("bytePrimitiveArray", NavArgPrimitiveByteArrayType),
            navArgument("byteArray", NavArgByteArrayType),
            navArgument("byteList", NavArgByteListType),

            navArgument("short", NavArgShortType),
            navArgument("shortPrimitiveArray", NavArgPrimitiveShortArrayType),
            navArgument("shortArray", NavArgShortArrayType),
            navArgument("shortList", NavArgShortListType),

            navArgument("char", NavArgCharType),
            navArgument("charPrimitiveArray", NavArgPrimitiveCharArrayType),
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

            char = getTyped("char")!!,
            charPrimitiveArray = getTyped("charPrimitiveArray")!!,
            charArray = getTyped("charArray")!!,
            charList = getTyped("charList")!!,

            int = getTyped("int")!!,
            intPrimitiveArray = getTyped("intPrimitiveArray")!!,
            intArray = getTyped("intArray")!!,
            intList = getTyped("intList")!!,

            long = getTyped("long")!!,
            longPrimitiveArray = getTyped("longPrimitiveArray")!!,
            longArray = getTyped("longArray")!!,
            longList = getTyped("longList")!!,

            float = getTyped("float")!!,
            floatPrimitiveArray = getTyped("floatPrimitiveArray")!!,
            floatArray = getTyped("floatArray")!!,
            floatList = getTyped("floatList")!!,

            double = getTyped("double")!!,
            doublePrimitiveArray = getTyped("doublePrimitiveArray")!!,
            doubleArray = getTyped("doubleArray")!!,
            doubleList = getTyped("doubleList")!!,

            boolean = getTyped("boolean")!!,
            booleanPrimitiveArray = getTyped("booleanPrimitiveArray")!!,
            booleanArray = getTyped("booleanArray")!!,
            booleanList = getTyped("booleanList")!!,

            byte = getTyped("byte")!!,
            bytePrimitiveArray = getTyped("bytePrimitiveArray")!!,
            byteArray = getTyped("byteArray")!!,
            byteList = getTyped("byteList")!!,

            short = getTyped("short")!!,
            shortPrimitiveArray = getTyped("shortPrimitiveArray")!!,
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