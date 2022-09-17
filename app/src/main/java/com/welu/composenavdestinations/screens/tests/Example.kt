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
import com.welu.composenavdestinations.DetailsVm
import com.welu.composenavdestinations.annotations.DefaultNavGraph
import com.welu.composenavdestinations.annotations.NavDestinationDefinition
import com.welu.composenavdestinations.annotations.NavGraphDefinition
import com.welu.composenavdestinations.extensions.invoke
import com.welu.composenavdestinations.extensions.navigation.navigate
import com.welu.composenavdestinations.extensions.sendDestinationResultTo
import com.welu.composenavdestinations.navgraphs.OtherGraphSpec
import com.welu.composenavdestinations.navigation.ArgCompositionScope
import com.welu.composenavdestinations.navigation.PlainCompositionScope
import com.welu.composenavdestinations.navigation.destinations.ArgDestination
import com.welu.composenavdestinations.navigation.destinations.PlainDestination
import com.welu.composenavdestinations.result.DestinationResultListener
import com.welu.composenavdestinations.screens.DetailScreen
import com.welu.composenavdestinations.screens.DetailScreenNavArgs
import kotlinx.parcelize.Parcelize
import kotlin.random.Random

@Parcelize
data class User(
    val id: String,
    val name: String,
    val age: Int
) : Parcelable


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

@DefaultNavGraph(isStart = true)
@NavDestinationDefinition
object FirstDestination : PlainDestination {
    override val Content: PlainCompositionScope = {
        var parsedValue: Int? by rememberSaveable { mutableStateOf(null) }

        DestinationResultListener<Int> {
            println("RESULT RECEIVED: $it")
            parsedValue = it
        }

        println("RECOMPOSITION FIRST SCREEN")

        StartDestinationComposable(parsedValue) {
            navigate(SecondDestination(User("111", "LOL", 212)))
        }
    }
}

@NavDestinationDefinition
object SecondDestination : ArgDestination<SecondDestination.NavArgs> {

    class NavArgs(
        val user: User? = User("123", "Sasi", 23),
    )

    override val Content: ArgCompositionScope<NavArgs> = {
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

@NavDestinationDefinition("ThirdAdjustedRoute")
object ThirdDestination : ArgDestination<DetailScreenNavArgs> {
    override val Content: ArgCompositionScope<DetailScreenNavArgs> = {
        val vm = viewModel<DetailsVm>()
        DetailScreen(args = vm.args) {
            //navigate to nested Graph
            navController.navigate(OtherGraphSpec.route)
        }
    }
}

@DefaultNavGraph
@NavGraphDefinition
annotation class OtherGraph(
    val isStart: Boolean
)

@OtherGraph(true)
@NavDestinationDefinition
object FourthDestination: PlainDestination {
    override val Content: PlainCompositionScope = {
        Text(text = "HELLO FROM FOURTH")
    }
}