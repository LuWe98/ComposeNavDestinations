package com.welu.composenavdestinations.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.welu.composenavdestinations.annotations.NavDestination
import com.welu.composenavdestinations.model.ParcelableObject
import com.welu.composenavdestinations.model.TestEnum
import java.util.*

class DetailScreenNavArgs(
    val string: String? = "Hallo",
    val stringArray: Array<String?> = arrayOf("hallo", "tschau"),
    val stringList: List<String> = listOf("Hallo", "Bernd"),
    val stringSet: Set<String>? = hashSetOf("12", "12"),

    val int: Int = 46,
    val intPrimitiveArray: IntArray = intArrayOf(21, 22, 23),
    val intArray: Array<Int> = arrayOf(21, 22, 23),
    val intList: List<Int?> = listOf(21, 221, 2221),

    val long: Long = 32,
    val longPrimitiveArray: LongArray = longArrayOf(24, 25, 26),
    val longArray: Array<Long> = arrayOf(24, 25, 26),
    val longList: ArrayList<Long?> = arrayListOf(32, 12, 4324),

    val float: Float = 21f,
    val floatPrimitiveArray: FloatArray = floatArrayOf(1f, 2f),
    val floatArray: Array<Float> = arrayOf(1f, 2f),
    val floatList: List<Float?> = listOf(212f, 323f, 32f),

    val double: Double = 21.2,
    val doublePrimitiveArray: DoubleArray = doubleArrayOf(12.0),
    val doubleArray: Array<Double> = arrayOf(12.0),
    val doubleList: ArrayList<Double> = arrayListOf(212.3),

    val boolean: Boolean = false,
    val booleanPrimitiveArray: BooleanArray = booleanArrayOf(true, true, false),
    val booleanArray: Array<Boolean> = arrayOf(true, true, false),
    val booleanList: List<Boolean> = listOf(true, false, true),

    val byte: Byte = 20,
    val bytePrimitiveArray: ByteArray = byteArrayOf(21, 23),
    val byteArray: Array<Byte> = arrayOf(21, 23),
    val byteList: List<Byte> = listOf(21),

    val short: Short = 323,
    val shortPrimitiveArray: ShortArray = shortArrayOf(2122),
    val shortArray: Array<Short> = arrayOf(2122),
    val shortList: List<Short> = listOf(32, 3211),

    val char: Char = '2',
    val charPrimitiveArray: CharArray = charArrayOf('s'),
    val charArray: Array<Char> = arrayOf('d'),
    val charList: List<Char> = listOf('a', 'b'),

    val enum: TestEnum = TestEnum.HALLO,
    val enumArray: Array<TestEnum> = TestEnum.values(),
    val enumList: List<TestEnum> = TestEnum.values().asList(),

    val parcelable: ParcelableObject = ParcelableObject("313", "Hans", 21),
    val parcelableArray: Array<ParcelableObject?> = arrayOf(ParcelableObject("1", "Sasi", 23), ParcelableObject("2", "Lucha", 23)),
    val parcelableList: List<ParcelableObject?> = listOf(ParcelableObject("1", "Sasi", 23)),

    val serializable: UUID = UUID.randomUUID(),

    val map: HashMap<String, Int> = hashMapOf("dsd" to 212)
)

@NavDestination(
    route = "details",
    navArgs = DetailScreenNavArgs::class
)
@Composable
fun DetailScreen(
    s: String = "Test",
    args: DetailScreenNavArgs
) {
    args.apply {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

            Text(text = "Strings:", fontSize = 20.sp, color = Color.Cyan)
            Text(text = "String: $string")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "StringArray: ${stringArray.joinToString()}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "StringList: ${stringList.joinToString()}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "StringSet: ${stringSet?.joinToString()}")
            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Ints:", fontSize = 20.sp, color = Color.Cyan)
            Text(text = "Int: $int")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "IntPrimitiveArray: ${intPrimitiveArray.joinToString()}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "IntArray: ${intArray.joinToString()}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "IntList: ${intList.joinToString()}")
            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Longs:", fontSize = 20.sp, color = Color.Cyan)
            Text(text = "Long: $long")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "LongPrimitiveArray: ${longPrimitiveArray.joinToString()}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "LongArray: ${longArray.joinToString()}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "LongList: ${longList.joinToString()}")
            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Floats:", fontSize = 20.sp, color = Color.Cyan)
            Text(text = "Float: $float")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "FloatPrimitiveArray: ${floatPrimitiveArray.joinToString()}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "FloatArray: ${floatArray.joinToString()}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "FloatList: ${floatList.joinToString()}")
            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Doubles:", fontSize = 20.sp, color = Color.Cyan)
            Text(text = "Double: $double")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "DoublePrimitiveArray: ${doublePrimitiveArray.joinToString()}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "DoubleArray: ${doubleArray.joinToString()}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "DoubleList: ${doubleList.joinToString()}")
            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Booleans:", fontSize = 20.sp, color = Color.Cyan)
            Text(text = "Boolean: $boolean")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "BooleanPrimitiveArray: ${booleanPrimitiveArray.joinToString()}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "BooleanArray: ${booleanArray.joinToString()}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "BooleanList: ${booleanList.joinToString()}")
            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Bytes:", fontSize = 20.sp, color = Color.Cyan)
            Text(text = "Byte: $byte")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "BytePrimitiveArray: ${bytePrimitiveArray.joinToString()}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "ByteArray: ${byteArray.joinToString()}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "ByteList: ${byteList.joinToString()}")
            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Shorts:", fontSize = 20.sp, color = Color.Cyan)
            Text(text = "Short: $short")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "ShortPrimitiveArray: ${shortPrimitiveArray.joinToString()}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "ShortArray: ${shortArray.joinToString()}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "ShortList: ${shortList.joinToString()}")
            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Chars:", fontSize = 20.sp, color = Color.Cyan)
            Text(text = "Char: $char")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "CharPrimitiveArray: ${charPrimitiveArray.joinToString()}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "CharArray: ${charArray.joinToString()}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "CharList: ${charList.joinToString()}")
            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Enums:", fontSize = 20.sp, color = Color.Cyan)
            Text(text = "Enum: ${enum.name}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "EnumArray: ${enumArray.joinToString()}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "EnumList: ${enumList.joinToString()}")
            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Parcelables:", fontSize = 20.sp, color = Color.Cyan)
            Text(text = "Parcelable: $parcelable")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "ParcelableArray: ${parcelableArray.joinToString()}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "ParcelableList: ${parcelableList.joinToString()}")
            Spacer(modifier = Modifier.height(25.dp))

            Text(text = "Serializables:", fontSize = 20.sp, color = Color.Cyan)
            Text(text = "Serializable: $serializable")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Map: $map")
            Spacer(modifier = Modifier.height(25.dp))
        }
    }
}