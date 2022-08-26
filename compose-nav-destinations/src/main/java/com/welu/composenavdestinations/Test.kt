package com.welu.composenavdestinations

import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.welu.composenavdestinations.extensions.requireArg
import com.welu.composenavdestinations.extensions.navigation.NavHost
import com.welu.composenavdestinations.extensions.navigation.navigate
import com.welu.composenavdestinations.spec.NavDestinationPlainSpec
import com.welu.composenavdestinations.spec.NavDestinationSpec
import com.welu.composenavdestinations.spec.Routable


@Composable
fun App() {
    val controller = rememberNavController()

    NavHost(controller, startDestination = HomeScreenNavDestination) {

    }
}



//@NavDestination(route = "details")
class DetailsScreenArgs(
    val id: String? = null
)

//@NavDestination(route = "home")
object HomeScreen



object DetailsScreenNavDestination : NavDestinationSpec<DetailsScreenArgs> {

    operator fun invoke(id: String): Routable = Routable("$route/$id")

    override val route: String get() = "details/{id}"

    override val baseRoute: String get() = "details"

    override fun getArgs(navBackStackEntry: NavBackStackEntry) = DetailsScreenArgs(
        navBackStackEntry.requireArg("id")
    )

    override fun getArgs(savedStateHandle: SavedStateHandle) = DetailsScreenArgs(
        savedStateHandle.requireArg("id")
    )

    override val arguments: List<NamedNavArgument>
        get() = listOf(
            navArgument("id") {
                type = NavType.StringType
                nullable = false
            }
        )

}

object HomeScreenNavDestination : NavDestinationPlainSpec {
    override val route: String get() = "püopüopüopü"
}