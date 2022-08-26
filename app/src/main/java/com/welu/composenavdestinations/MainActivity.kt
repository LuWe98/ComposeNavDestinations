package com.welu.composenavdestinations

import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.welu.composenavdestinations.annotations.NavArgument
import com.welu.composenavdestinations.annotations.NavDestination
import com.welu.composenavdestinations.extensions.navigation.composable
import com.welu.composenavdestinations.extensions.navigation.navigate
import com.welu.composenavdestinations.spec.ExampleDetailSpec
import com.welu.composenavdestinations.tt.Bar
import com.welu.composenavdestinations.tt.Entity
import com.welu.composenavdestinations.ui.theme.ComposeNavDestinationsTheme
import kotlinx.parcelize.Parcelize

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposeNavDestinationsTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    NavigationComp()
                }
            }
        }
    }
}

@Parcelize
data class ParcelableObject(
    val id: String,
    val name: String,
    val age: Int
) : Parcelable

enum class TestEnum { HALLO, TSCHAU; }


private fun onNavigationClicked(navController: NavController) {
    navController.navigate(ExampleDetailSpec())
}

fun <T> NavBackStackEntry.getTyped(key: String): T? = arguments?.get(key) as T?

@Composable
private fun NavigationComp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "start") {
        composable("start") {
            StartScreen {
                onNavigationClicked(navController)
            }
        }

        composable(ExampleDetailSpec) {
            DetailScreen("hallo", args)
        }
    }
}


@NavDestination(route = "home")
@Composable
fun StartScreen(
    navToDetail: () -> Unit
) {
    Column {
        Text(text = "boolean")
        Button(onClick = navToDetail) {
            Text(text = "Navigate")
        }
    }
}

@NavDestination(
    route = "details",
    navArgs = DetailScreenNavArgs::class
)
@Composable
fun DetailScreen(
    @NavArgument
    s: String = "Test",
    args: DetailScreenNavArgs
) {
    args.apply {
        Column {
            Text(text = "String: $string")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Int: $int")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Long: $long")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Byte: $byte")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Short: $short")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Float: $float")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Boolean: $boolean")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "ENUM: ${enum.name} - Ordinal: ${enum.ordinal}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Parcelable: $parcelable")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "StringArray: ${stringArray.joinToString()}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "IntArray: ${intArray.joinToString()}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "LongArray: ${longArray.joinToString()}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "FloatArray: ${floatArray.joinToString()}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "BooleanArray: ${booleanArray.joinToString()}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "ParcelableArray: ${parcelableArray.joinToString()}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "ParcelableList: ${parcelableArrayList.joinToString()}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "StringList: ${stringArrayList.joinToString()}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "IntList: ${intArrayList.joinToString()}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "LongList: ${longArrayList.joinToString()}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "FloatList: ${floatArrayList.joinToString()}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "BooleanList: ${booleanArrayList.joinToString()}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "EnumList: ${enumArrayList.joinToString()}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "StringSet: ${stringSet?.joinToString() ?: "NULL"}")
        }
    }
}

data class DetailScreenNavArgs(
    val string: String = "Hallo",
    val int: Int = 46,
    val long: Long = 32,
    val float: Float = 21f,
    val boolean: Boolean = false,
    val enum: TestEnum = TestEnum.HALLO,
    val parcelable: ParcelableObject = ParcelableObject("313", "Hans", 21),
    val byte: Byte = 20,
    val short: Short = 323,
    val stringArray: Array<String?> = arrayOf("hallo", "tschau"),
    val intArray: IntArray = intArrayOf(21, 22, 23),
    val longArray: LongArray = longArrayOf(24, 25, 26),
    val floatArray: FloatArray = floatArrayOf(1f, 2f),
    val booleanArray: BooleanArray = booleanArrayOf(true, true, false),
    val parcelableArray: Array<ParcelableObject?> = arrayOf(ParcelableObject("1", "Sasi", 23), ParcelableObject("2", "Lucha", 23)),
    val parcelableArrayList: List<ParcelableObject?> = listOf(ParcelableObject("1", "Sasi", 23)),
    val stringArrayList: List<String> = listOf("Hallo", "Bernd"),
    val intArrayList: List<Int?> = listOf(21, 221, 2221),
    val longArrayList: ArrayList<Long?> = arrayListOf(32, 12, 4324),
    val floatArrayList: List<Float?> = listOf(212f, 323f, 32f),
    val booleanArrayList: List<Boolean> = listOf(true, false, true),
    val enumArrayList: List<TestEnum> = TestEnum.values().asList(),
    val stringSet: Set<String>? = setOf("12", "12")
)