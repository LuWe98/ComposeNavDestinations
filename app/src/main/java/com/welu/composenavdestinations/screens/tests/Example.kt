package com.welu.composenavdestinations.screens.tests

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navigation
import com.welu.composenavdestinations.DetailsVm
import com.welu.composenavdestinations.annotations.DefaultNavGraph
import com.welu.composenavdestinations.annotations.NavDestinationDefinition
import com.welu.composenavdestinations.annotations.NavGraphDefinition
import com.welu.composenavdestinations.extensions.findArgSpec
import com.welu.composenavdestinations.navigation.destinations.ArgDestination
import com.welu.composenavdestinations.navigation.destinations.PlainDestination
import com.welu.composenavdestinations.navigation.ArgCompositionScope
import com.welu.composenavdestinations.navigation.scope.ArgDestinationScope
import com.welu.composenavdestinations.navigation.PlainCompositionScope
import com.welu.composenavdestinations.navigation.scope.PlainDestinationScope
import com.welu.composenavdestinations.extensions.invoke
import com.welu.composenavdestinations.extensions.navigation.navArgument
import com.welu.composenavdestinations.extensions.navigation.navigate
import com.welu.composenavdestinations.extensions.sendDestinationResultTo
import com.welu.composenavdestinations.navigation.destinations.Destination
import com.welu.composenavdestinations.navigation.spec.DestinationSpec
import com.welu.composenavdestinations.navigation.spec.NavGraphSpec
import com.welu.composenavdestinations.navigation.spec.NavigationComponent
import com.welu.composenavdestinations.result.DestinationResultListener
import com.welu.composenavdestinations.screens.DetailScreen
import com.welu.composenavdestinations.screens.DetailScreenNavArgs
import kotlinx.parcelize.Parcelize
import kotlin.random.Random

//TODO -> Destinations müssen innerhalb eines solchen NavGraphs eingebaut werden, sonst gehts nicht
// -> Es wird auch ein NavGraphSpec (Bspw. ExampleNavGraphSpec) erstellt, zu welchem man auch navigieren kann, da es Routable ist

//TODO -> https://medium.com/mobile-app-development-publication/making-custom-lint-for-kotlin-code-8a6c203bf474
// Um eigenes Linting zu erstellen. Damit kann man bspw checken ob eine Flasse die mit @NavDestinationDefinition Annotiert ist auch das Destination Interface implementiert.
// Beispiel kann unten gesehen werden mit -> NoParcelableSupertype, wenn man nicht Parcelable Implementiert.

@Parcelize
data class User(
    val id: String,
    val name: String,
    val age: Int
) : Parcelable

@DefaultNavGraph
@NavGraphDefinition
annotation class FirstNavGraph(
    val isStart: Boolean = false
)

// Mit einer anderen Annotation annotieren, wenn man das als parent nehmen will
//@RootNavGraph(start = true)
//@NavGraph
//annotation class YourNavGraph(
//    val start: Boolean = false
//)

@FirstNavGraph
@NavGraphDefinition(route = "Gr8Route")
annotation class SecondNavGraph(
    val isStart: Boolean = false
)

//@FourthNavGraph(true)
//@NavGraphDefinition
//annotation class ThirdNavGraph(
//    val isStart: Boolean = false
//)
//
//
//@ThirdNavGraph(true)
//@NavGraphDefinition
//annotation class FourthNavGraph(
//    val isStart: Boolean = false
//)

//
//@FourthNavGraph(true)
//@NavGraphDefinition
//annotation class FithNavGraph(
//    val isStart: Boolean = false
//)

@Composable
fun StartDestinationComposable(
    currentValue: Int?,
    onClick: () -> Unit
) {
    Column {
        Text(text = "Current Value: $currentValue")
        Button(onClick = onClick) {
            Text(text = "Navigte to Second")
        }
    }
}


object DefaultNavGraphSpec: NavGraphSpec {

    override val startComponent: NavigationComponent get() = FirstDestinationSpec

    override val parentNavGraphSpec: NavGraphSpec? = null

    override val childNavGraphSpecs: List<NavGraphSpec> = emptyList()

    override val childDestinationSpecs: List<DestinationSpec<out Destination<*>>> get() = listOf(
        FirstDestinationSpec,
        SecondDestinationSpec
    )

    override val baseRoute: String = ""

    override val route: String = ""
}


@Composable
fun TestDestinationComposable(
    user: User?,
    navigateBack: () -> Unit,
    navigateToThirdScreen: () -> Unit,
    sendResult: () -> Unit
) {
    Column {
        Text(text = "Parsed User: $user")
        Button(onClick = navigateBack) {
            Text(text = "Navigate Back")
        }
        Button(onClick = navigateToThirdScreen) {
            Text(text = "Navigate to third Screen")
        }
        Button(onClick = sendResult) {
            Text(text = "Send Result to First Destination")
        }
    }
}

@DefaultNavGraph(isStart = true)
@NavDestinationDefinition
object FirstDestination : PlainDestination {

    override val content: PlainCompositionScope = {
        var parsedValue: Int? by rememberSaveable { mutableStateOf(null) }

        DestinationResultListener<Int> {
            println("RESULT RECEIVED: $it")
            parsedValue = it
        }

        StartDestinationComposable(parsedValue) {
            //navigate(SecondDestination(User("111", "LOL", 212)))
            navController.navigate("comp2/Hans")
        }
    }
}

@FirstNavGraph(true)
@NavDestinationDefinition
object SecondDestination : ArgDestination<SecondDestination.NavArgs> {

    data class NavArgs(
        val user: User? = User("123", "Sasi", 23),
    )

    override val content: ArgCompositionScope<NavArgs> = {
        TestDestinationComposable(
            user = args.user,
            navigateBack = navController::navigateUp,
            navigateToThirdScreen = {
                navigate(ThirdDestination())
            },
            sendResult = {
                sendDestinationResultTo(FirstDestination, Random.nextInt())
            }
        )
    }
}

@SecondNavGraph(true)
@NavDestinationDefinition
object ThirdDestination : ArgDestination<DetailScreenNavArgs> {
    override val content: ArgCompositionScope<DetailScreenNavArgs> = {
        val vm = viewModel<DetailsVm>()
        DetailScreen(args = vm.args)
    }
}


//--------------------------------------------------------------------------
// GENERATED
//
@SuppressLint("UnrememberedGetBackStackEntry")
fun NavGraphBuilder.initDestinations(navController: NavHostController) {
    composable(route = FirstDestinationSpec.route) {
        FirstDestination.content(
            PlainDestinationScope(
                FirstDestinationSpec,
                navController,
                it
            )
        )
    }

    composable(
        route = SecondDestinationSpec.route,
        arguments = SecondDestinationSpec.arguments
    ) {
        SecondDestination.content(
            ArgDestinationScope(
                SecondDestinationSpec,
                navController,
                it,
                lazy { SecondDestinationSpec.argsFrom(it) }
            )
        )
    }

    composable(
        route = ThirdDestinationSpec.route,
        arguments = ThirdDestinationSpec.arguments
    ) {
        ThirdDestination.content(
            ArgDestinationScope(
                ThirdDestinationSpec,
                navController,
                it,
                lazy { ThirdDestinationSpec.argsFrom(it) }
            )
        )
    }

    //TODO -> Schauen wie das genau funktioniert mit den Argumenten bei den Navigations -> Es ist durchaus sinnvoll das auf NavigationGraph zu scopen, damit es jeder nutzen kann
    // ArgsFrom sollte auch funktionieren, man muss halt nur den richtigen BackStackEntry mitgeben
    navigation(
        route = "nav/{age}",
        startDestination = "nav2",
        arguments = listOf(
            navArgument("age", NavType.StringType)
        ),
        deepLinks = emptyList()
    ) {
        navigation(
            route = "nav2",
            startDestination = "comp/{age}"
        ) {
            composable(
                route = "comp/{age}",
                arguments = listOf(
                    navArgument("age", NavType.StringType)
                )
            ) {

                val nav1Entry = remember {
                    navController.getBackStackEntry("nav/{age}")
                }

                val nav2Entry = remember {
                    navController.getBackStackEntry("nav2")
                }

                val ageArgParent1 = nav1Entry.arguments?.getString("age")
                val ageArgParent2 = nav2Entry.arguments?.getString("age")
                val ageArg = it.arguments?.getString("age")

                Column {
                    Text(text = "DESTINATION 1")
                    Text(text = "Parsed Nav1: $ageArgParent1")
                    Text(text = "Parsed Nav2: $ageArgParent2")
                    Text(text = "Parsed Comp1: $ageArg")
                    Text(text = "Entry Nav1: $nav1Entry")
                    Text(text = "Entry Nav2: $nav2Entry")
                    Text(text = "Entry Comp1: $it")
                    Button(onClick = { navController.navigate("comp2/132323")}) {
                        Text(text = "Navigate")
                    }
                }
            }

            composable(
                route = "comp2/{age}",
                arguments = listOf(
                    navArgument("age", NavType.StringType)
                )
            ) {

                val nav1Entry = remember {
                    navController.getBackStackEntry("nav/{age}")
                }

                val nav2Entry = remember {
                    navController.getBackStackEntry("nav2")
                }

                val comp12Entry = remember {
                    navController.getBackStackEntry("nav/{age}")
                }

                val ageArgParent1 = nav1Entry.arguments?.getString("age")
                val ageArgParent2 = nav2Entry.arguments?.getString("age")
                val ageArgComp1 = comp12Entry.arguments?.getString("age")
                val ageArg = it.arguments?.getString("age")

                Column {
                    Text(text = "DESTINATION 2")
                    Text(text = "Parsed Nav1: $ageArgParent1")
                    Text(text = "Parsed Nav2: $ageArgParent2")
                    Text(text = "Parsed Comp1: $ageArgComp1")
                    Text(text = "Parsed Comp2: $ageArg")
                    Text(text = "Entry Nav1: $nav1Entry")
                    Text(text = "Entry Nav2: $nav2Entry")
                    Text(text = "Entry Comp1: $comp12Entry")
                    Text(text = "Entry Comp2: $it")
                }
            }
        }
    }
}

//    navigation("", "") {
//        composable()
//    }
// TODO -> Schauen ob sinnvoll
//val FirstDestination.spec get() = FirstDestinationSpec
//
//val SecondDestination.spec get() = SecondDestinationSpec
//
//val ThirdDestination.spec get() = ThirdDestinationSpec
