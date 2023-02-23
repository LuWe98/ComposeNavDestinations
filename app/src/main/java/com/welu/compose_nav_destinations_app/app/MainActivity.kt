package com.welu.compose_nav_destinations_app.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.welu.compose_nav_destinations_app.app.ui.theme.ComposeNavDestinationsTheme
import com.welu.composenavdestinations.extensions.navigation.addNavGraph
import com.welu.composenavdestinations.navgraphs.DefaultNavGraphSpec

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       // WindowCompat.setDecorFitsSystemWindows(window, false)

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

    ModalBottomSheetLayout(bottomSheetNavigator, scrimColor = Color.Black.copy(alpha = 0.5f)) {
        AnimatedNavHost(
            startDestination = DefaultNavGraphSpec.startComponentSpec.baseRoute,
            navController = navController,
            modifier = Modifier.fillMaxSize()
        ) {
            addNavGraph(DefaultNavGraphSpec, navController)

//            composable("One"){
//                Text(text = "One", modifier = Modifier.clickable { navController.navigate("Two") })
//            }
//
//            composable("Two") {
//                Text(text = "Two")
//            }
        }
    }
}