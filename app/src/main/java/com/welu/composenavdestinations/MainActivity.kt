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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.bottomSheet
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.welu.composenavdestinations.extensions.navigation.addNavComponents
import com.welu.composenavdestinations.extensions.route
import com.welu.composenavdestinations.navgraphs.DefaultNavGraphSpec
import com.welu.composenavdestinations.screens.tests.FirstDestination
import com.welu.composenavdestinations.ui.theme.ComposeNavDestinationsTheme

//TODO -> Destinations müssen innerhalb eines solchen NavGraphs eingebaut werden, sonst gehts nicht
// -> Es wird auch ein NavGraphSpec (Bspw. ExampleNavGraphSpec) erstellt, zu welchem man auch navigieren kann, da es Routable ist

//TODO -> https://medium.com/mobile-app-development-publication/making-custom-lint-for-kotlin-code-8a6c203bf474
// Um eigenes Linting zu erstellen. Damit kann man bspw checken ob eine Flasse die mit @NavDestinationDefinition Annotiert ist auch das Destination Interface implementiert.
// Beispiel kann unten gesehen werden mit -> NoParcelableSupertype, wenn man nicht Parcelable Implementiert.

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