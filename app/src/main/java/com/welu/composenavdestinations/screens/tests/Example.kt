package com.welu.composenavdestinations.screens.tests

import android.os.Parcelable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.welu.composenavdestinations.DetailsVm
import com.welu.composenavdestinations.annotations.NavDestinationDefinition
import com.welu.composenavdestinations.annotations.NavGraphDefinition
import com.welu.composenavdestinations.destinations.ArgDestination
import com.welu.composenavdestinations.destinations.PlainDestination
import com.welu.composenavdestinations.destinations.scope.ArgCompositionScope
import com.welu.composenavdestinations.destinations.scope.ArgDestinationScope
import com.welu.composenavdestinations.destinations.scope.PlainCompositionScope
import com.welu.composenavdestinations.destinations.scope.PlainDestinationScope
import com.welu.composenavdestinations.extensions.argsFrom
import com.welu.composenavdestinations.extensions.invoke
import com.welu.composenavdestinations.extensions.navigation.navigate
import com.welu.composenavdestinations.extensions.sendDestinationResultTo
import com.welu.composenavdestinations.result.DestinationResultListener
import com.welu.composenavdestinations.screens.DetailScreen
import com.welu.composenavdestinations.screens.DetailScreenNavArgs
import kotlinx.parcelize.Parcelize
import kotlin.random.Random

//TODO -> Destinations müssen innerhalb eines solchen NavGraphs eingebaut werden, sonst gehts nicht
// -> Es wird auch ein NavGraphSpec (Bspw. ExampleNavGraphSpec) erstellt, zu welchem man auch navigieren kann, da es Routable ist

@Parcelize
data class User(
    val id: String,
    val name: String,
    val age: Int
) : Parcelable


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


@NavGraphDefinition
annotation class SecondNavGraph(
    val isStart: Boolean = false
)


@FirstNavGraph
@NavDestinationDefinition
object FirstDestination : PlainDestination {

    override val content: PlainCompositionScope = {
        var parsedValue: Int? by rememberSaveable { mutableStateOf(null) }

        DestinationResultListener<Int> {
            println("RESULT RECEIVED: $it")
            parsedValue = it
        }

        StartDestinationComposable(parsedValue) {
            navigate(SecondDestination(User("111", "LOL", 212)))
        }
    }
}

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


@NavDestinationDefinition
object ThirdDestination : ArgDestination<DetailScreenNavArgs> {
    override val content: ArgCompositionScope<DetailScreenNavArgs> = {
        val vm = viewModel<DetailsVm>()
        DetailScreen(args = vm.args)
    }
}


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






//--------------------------------------------------------------------------
// GENERATED
//
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
