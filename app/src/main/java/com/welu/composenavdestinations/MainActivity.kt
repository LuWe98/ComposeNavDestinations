package com.welu.composenavdestinations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.welu.composenavdestinations.screens.tests.FirstDestinationSpec
import com.welu.composenavdestinations.screens.tests.initDestinations
import com.welu.composenavdestinations.ui.theme.ComposeNavDestinationsTheme

// TODO -> https://proandroiddev.com/implementing-your-first-android-lint-rule-6e572383b292
//  LINT checks in einem separaten Modul erstellen, dass bspw. eine Destination Klasse auch die @NavDestination Annotation besitzt und umgekehrt

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
    NavHost(
        startDestination = FirstDestinationSpec.route,
        navController = navController
    ) { initDestinations(navController) }
}

//        navDestination(StartScreenNavDestination) {
//
//            var stateOne: Int? by rememberSaveable { mutableStateOf(0) }
//
//            navController.destinationResultListener<ComplexType?>(this) {
//                stateOne = it?.age
//                println("TRIGGERED")
//            }
//
////            navController.lifecycleResultListener<ComplexType?>(spec) {
////                println("RESULT RECEIVED: $it")
////                stateOne = it?.age
////            }
//
//            println("RECOMPOSITION")
//
//            StartScreen(
//                valueOne = stateOne,
//                valueTwo = stateOne,
//                onRandomButtonClicked = {
//                    //RandomScreenNavDestination.sendResult(Random.nextDouble())
//                    val type = ComplexType(Random.nextInt(), "Dion")
//                    navController.sendDestinationResult(StartScreenNavDestination, type)
//                },
//                navToDetail = {
//                    navController.navigate(RandomScreenNavDestination("BOB"))
//                }
//            )
//        }
//
//        navDestination(DetailScreenNavDestination) {
//            val vm = viewModel<DetailsVm>()
//            DetailScreen("hallo", vm.args)
//        }
//
//        navDestinationDialog(RandomScreenNavDestination) {
//            var state: Double? by rememberSaveable {
//                mutableStateOf(21.2)
//            }
//
//            navController.destinationResultListener<Double>(spec) {
//                state = it
//            }
//
//            RandomScreen(
//                name = args.name,
//                valueTest = state
//            ) {
////                navController.navigate(DetailScreenNavDestination())
//                val type = ComplexType(2, "Dion")
//                navController.sendDestinationResult(StartScreenNavDestination, type)
//            }
//        }
//    }