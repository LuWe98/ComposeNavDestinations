package com.welu.composenavdestinations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.welu.composenavdestinations.extensions.navigation.addNavComponents
import com.welu.composenavdestinations.extensions.route
import com.welu.composenavdestinations.navgraphs.DefaultNavGraphSpec
import com.welu.composenavdestinations.screens.tests.FirstDestination
import com.welu.composenavdestinations.ui.theme.ComposeNavDestinationsTheme

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

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialNavigationApi::class)
@Composable
private fun NavigationComp() {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberAnimatedNavController(bottomSheetNavigator)

    ModalBottomSheetLayout(bottomSheetNavigator) {
        AnimatedNavHost(
            startDestination = FirstDestination.route,
            navController = navController,
            modifier = Modifier.fillMaxSize()
        ) {
            addNavComponents(DefaultNavGraphSpec, navController)
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