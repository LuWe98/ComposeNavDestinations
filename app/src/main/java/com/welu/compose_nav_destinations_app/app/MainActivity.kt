package com.welu.compose_nav_destinations_app.app

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.welu.compose_nav_destinations_app.app.model.TestEnum
import com.welu.compose_nav_destinations_app.app.ui.theme.ComposeNavDestinationsTheme
import com.welu.composenavdestinations.extensions.init
import com.welu.composenavdestinations.extensions.navigation.addNavGraph
import com.welu.composenavdestinations.navargs.NavArgEnumType
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

        SecondDestinationSpec

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

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
private fun NavigationComp() {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberNavController(bottomSheetNavigator)

    ModalBottomSheetLayout(
        bottomSheetNavigator = bottomSheetNavigator,
        scrimColor = Color.Black.copy(alpha = 0.5f)
    ) {
        NavHost(
            startDestination = DefaultNavGraphSpec.startComponentSpec.baseRoute,
            navController = navController,
            modifier = Modifier.fillMaxSize().padding(top = 24.dp)
        ) {
            addNavGraph(DefaultNavGraphSpec, navController)
        }
    }
}