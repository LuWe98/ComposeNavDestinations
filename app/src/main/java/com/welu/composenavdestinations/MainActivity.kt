package com.welu.composenavdestinations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavGraph
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.welu.composenavdestinations.extensions.navigation.addNavGraph
import com.welu.composenavdestinations.extensions.navigation.navArgument
import com.welu.composenavdestinations.extensions.route
import com.welu.composenavdestinations.navgraphs.DefaultNavGraphSpec
import com.welu.composenavdestinations.screens.tests.FirstDestination
import com.welu.composenavdestinations.ui.theme.ComposeNavDestinationsTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            ComposeNavDestinationsTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    NavigationComp()
                }
            }

//            val navController = rememberNavController()
//            NavHost(navController, "start") {
//                composable("start") {
//                    Button(onClick = { navController.navigate("screen2") }) {
//                        Text(text = "LOL")
//                    }
//                }
//
//                composable("screen2") {
//                    navController.navigate("screen3/123?name=Hans")
//                }
//
//                composable("screen3/{id}?name={name}", arguments = listOf(
//                    navArgument("id") {
//                        type = NavType.StringType
//                    },
//                    navArgument("name") {
//                        type = NavType.StringType
//                        nullable = true
//                    }
//                )) {
//                    it.arguments!!.getString("name")
//                    it.arguments!!.getString("id")
//                }
//            }
        }
    }
}


interface Feature

class FeatureImpl(
    private val feature: Feature
): Feature by feature


@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialNavigationApi::class)
@Composable
private fun NavigationComp() {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberAnimatedNavController(bottomSheetNavigator)

    ModalBottomSheetLayout(bottomSheetNavigator, scrimColor = Color.Black.copy(alpha = 0.5f)) {
        AnimatedNavHost(
            startDestination = FirstDestination.route,
            navController = navController,
            modifier = Modifier.fillMaxSize()
        ) {
            addNavGraph(DefaultNavGraphSpec, navController)
        }
    }
}



//            composable("test1") {
//                println("RECOMPOSITION Test 1")
//
//                Button(onClick = {
//                    navController.navigate("test2")
//                }) {
//                    Text(text = "Click to navigate")
//                }
//            }
//
//            bottomSheet("test2") {
//                println("RECOMPOSITION Test 2")
//
//                Button(onClick = {
//                    navController.navigateUp()
//                }) {
//                    Text(text = "Click back")
//                }
//            }