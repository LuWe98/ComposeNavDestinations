package com.welu.compose_nav_destinations_app.app

import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.welu.compose_nav_destinations_app.app.ui.theme.ComposeNavDestinationsTheme
import com.welu.composenavdestinations.extensions.init
import com.welu.composenavdestinations.extensions.navigation.addNavGraph
import com.welu.composenavdestinations.navgraphs.DefaultNavGraphSpec
import com.welu.composenavdestinations.navigation.ComposeNavDestinations
import kotlinx.parcelize.Parcelize

class MainActivity : ComponentActivity() {

    @Parcelize
    data class TestParcelable(
        val id: String
    ): Parcelable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        ComposeNavDestinations.init()

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
        }
    }
}