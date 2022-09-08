package com.welu.composenavdestinations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.welu.composenavdestinations.extensions.navigation.NavDestinationNavHost
import com.welu.composenavdestinations.extensions.navigation.navDestination
import com.welu.composenavdestinations.extensions.navigation.navDestinationDialog
import com.welu.composenavdestinations.extensions.navigation.navigate
import com.welu.composenavdestinations.result.*
import com.welu.composenavdestinations.screens.*
import com.welu.composenavdestinations.ui.theme.ComposeNavDestinationsTheme
import com.welu.composenavdestinations.utils.NavDestinationUtils
import kotlin.random.Random

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposeNavDestinationsTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    NavigationComp()
                }
            }
        }
    }
}

@Composable
private fun NavigationComp(
    navController: NavHostController = rememberNavController()
) {

    NavDestinationNavHost(
        startDestination = StartScreenNavDestination,
        navController = navController
    ) {

        navDestination(StartScreenNavDestination) {
            var stateOne: Int? by rememberSaveable { mutableStateOf(0) }

            navController.flowResultListener<ComplexType?>(spec) {
                stateOne = it?.age
                println("TRIGGERED")
            }

//            navController.lifecycleResultListener<ComplexType?>(spec) {
//                println("RESULT RECEIVED: $it")
//                stateOne = it?.age
//            }

            println("RECOMPOSITION")

            StartScreen(
                valueOne = stateOne,
                valueTwo = stateOne,
                onRandomButtonClicked = {
                    //RandomScreenNavDestination.sendResult(Random.nextDouble())
                    val type = ComplexType(Random.nextInt(), "Dion")
                    navController.sendResult(StartScreenNavDestination, type)
                },
                navToDetail = {
                    navController.navigate(RandomScreenNavDestination("BOB"))
                }
            )
        }

        navDestination(DetailScreenNavDestination) {
            val vm = viewModel<DetailsVm>()
            DetailScreen("hallo", vm.args)
        }

        navDestinationDialog(RandomScreenNavDestination) {
            var state: Double? by rememberSaveable {
                mutableStateOf(21.2)
            }

            navController.flowResultListener<Double>(spec) {
                state = it
            }

            RandomScreen(
                name = args.name,
                valueTest = state
            ) {
                //navController.navigate(DetailScreenNavDestination())
                val type = ComplexType(2, "Dion")
                navController.sendResult(StartScreenNavDestination, type)
            }
        }
    }
}

data class ComplexType(
    val age:Int,
    val name: String
)