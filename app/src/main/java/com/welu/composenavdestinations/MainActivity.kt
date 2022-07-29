package com.welu.composenavdestinations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.welu.composenavdestinations.annotations.NavDestination
import com.welu.composenavdestinations.extensions.toLowerCaseTest
import com.welu.composenavdestinations.tt.*
import com.welu.composenavdestinations.ui.theme.ComposeNavDestinationsTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {

    companion object {
        fun getName(int: Int): String {
            return "Name $int"
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeNavDestinationsTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Greeting("Android")
                }
            }
        }
    }

}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeNavDestinationsTheme {
        Greeting("Android")
    }
}

@NavDestination(route = "home")
object HomeScreen

@NavDestination(route = "details")
class DetailsScreen(
    number: Float = .0f.dp.value
    ,
    lol: Boolean = false,
    hallo: String = "Hallo",
    test: Entity = Bar("hallo", MainActivity.getName(Random.nextInt(Int.MAX_VALUE)))
)

//fun String.test() {
//    .length
//}