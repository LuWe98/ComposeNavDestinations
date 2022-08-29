package com.welu.composenavdestinations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.welu.composenavdestinations.extensions.navigation.NavDestinationNavHost
import com.welu.composenavdestinations.extensions.navigation.navDestinationComposable
import com.welu.composenavdestinations.extensions.navigation.navDestinationDialogComposable
import com.welu.composenavdestinations.extensions.navigation.navigate
import com.welu.composenavdestinations.screens.*
import com.welu.composenavdestinations.ui.theme.ComposeNavDestinationsTheme

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

@Composable
private fun NavigationComp() {
    val navController = rememberNavController()

    NavDestinationNavHost(navController = navController, startDestination = StartScreenNavDestination) {
        navDestinationComposable(StartScreenNavDestination) {
            StartScreen {
                navController.navigate(DetailScreenNavDestination(string = "SASI LOL"))
            }
        }

        navDestinationComposable(DetailScreenNavDestination) {
            val vm = viewModel<DetailsVm>()
            DetailScreen("hallo", vm.args)
        }

        navDestinationDialogComposable(RandomScreenNavDestination) {
            RandomScreen(name = args.name)
        }
    }
}