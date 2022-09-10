package com.welu.composenavdestinations.screens.tests

import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.welu.composenavdestinations.extensions.navigation.navigate
import com.welu.composenavdestinations.spec.tests.*

//TODO -> Destinations müssen innerhalb eines solchen NavGraphs eingebaut werden, sonst gehts nicht -> Es wird auch ein NavGraphSpec (Bspw. ExampleNavGraphSpec) erstellt, zu welchem man auch navigieren kann, da es Routable ist
@NavGraphDefinition
annotation class ExampleNavGraph(
    val isStart: Boolean = false
)


@NavDestinationDefinition
object FirstDestination : PlainDestination {

    override val content: PlainCompositionScope = {

        navigate(ThirdDestinationSpec)

        StartDestinationComposable()
    }
}

@NavDestinationDefinition
object SecondDestination : ArgDestination<SecondDestination.NavArgs> {

    data class NavArgs(val name: String? = null)

    override val content: ArgCompositionScope<NavArgs> = {
        TestDestinationComposable(name = "")
    }
}


@NavDestinationDefinition
object ThirdDestination : PlainDestination {
    override val content: PlainCompositionScope = {

    }
}



@Composable
fun StartDestinationComposable() {

}

@Composable
fun TestDestinationComposable(
    name: String
) {

}



//--------------------------------------------------------------------------
// GENERATED

fun NavGraphBuilder.initDestinations(navController: NavHostController) {
    composable(FirstDestinationSpec.route) {
        FirstDestinationSpec.destination.content(
            PlainDestinationScope(
                navController = navController,
                backStackEntry = it
            )
        )
    }

    composable(SecondDestinationSpec.route) {
        SecondDestinationSpec.destination.content(
            ArgDestinationScope(
                navController = navController,
                backStackEntry = it,
                lazyArgs = lazy {
                    SecondDestinationSpec.argsFrom(it)
                }
            )
        )
    }

//    navigation("", "") {
//        composable()
//    }
}

object FirstDestinationSpec : PlainDestinationSpec<FirstDestination> {

    override val baseRoute: String = ""

    override val destination get() = FirstDestination

}


object SecondDestinationSpec : ArgDestinationSpec<SecondDestination, SecondDestination.NavArgs> {

    override val route: String = ""

    override val baseRoute: String = ""

    override val destination get() = SecondDestination

    override val arguments: List<NamedNavArgument> = listOf()

    operator fun invoke(name: String) = object : Routable {
        override val parameterizedRoute: String = ""
    }

    override fun argsFrom(entry: NavBackStackEntry) = SecondDestination.NavArgs("")

    override fun argsFrom(savedStateHandle: SavedStateHandle) = SecondDestination.NavArgs("")

}

object ThirdDestinationSpec : PlainDestinationSpec<ThirdDestination> {

    override val baseRoute: String = ""

    override val destination get() = ThirdDestination

}



//TODO -> Wird NUR bei ArgDestination<Arg> generated
operator fun SecondDestination.invoke(name: String) = SecondDestinationSpec(name)

fun SecondDestination.argsFrom(navBackStackEntry: NavBackStackEntry) = SecondDestinationSpec.argsFrom(navBackStackEntry)

fun SecondDestination.argsFrom(savedStateHandle: SavedStateHandle) = SecondDestinationSpec.argsFrom(savedStateHandle)



//TODO -> Wird IMMER generated
val FirstDestination.spec get() = FirstDestinationSpec

val SecondDestination.spec get() = SecondDestinationSpec

val ThirdDestination.spec get() = ThirdDestinationSpec




//TODO -> Um den zugehörigen Spec mit der Destination finden zu können, und zwar generisch
inline fun <reified Arg : Any> ArgDestination<Arg>.findSpec(): ArgDestinationSpec<out ArgDestination<Arg>, Arg> = allSpecs[this] as ArgDestinationSpec<*, Arg>

fun PlainDestination.findSpec(): PlainDestinationSpec<out PlainDestination> = allSpecs[this] as PlainDestinationSpec<*>

fun Destination<out DestinationScope>.findSpec(): DestinationSpec<out Destination<out DestinationScope>> = allSpecs[this]!!

//fun <S : DestinationSpec<*>> Destination.findSpecTyped(): S? = findSpec() as S?

val allSpecs: Map<Destination<out DestinationScope>, DestinationSpec<out Destination<out DestinationScope>>> = mapOf(
    FirstDestination to SecondDestinationSpec,
    SecondDestination to SecondDestinationSpec,
    ThirdDestination to ThirdDestinationSpec
)