package com.welu.composenavdestinations.screens.tests

import android.os.Parcelable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.welu.composenavdestinations.DetailsVm
import com.welu.composenavdestinations.annotations.DefaultNavGraph
import com.welu.composenavdestinations.annotations.NavDestinationDefinition
import com.welu.composenavdestinations.annotations.NavGraphDefinition
import com.welu.composenavdestinations.extensions.invoke
import com.welu.composenavdestinations.extensions.navigation.getBackStackEntry
import com.welu.composenavdestinations.extensions.navigation.navigate
import com.welu.composenavdestinations.extensions.sendDestinationResultTo
import com.welu.composenavdestinations.navgraphs.OtherGraphSpec
import com.welu.composenavdestinations.navigation.ArgDestinationCompositionScope
import com.welu.composenavdestinations.navigation.DestinationCompositionScope
import com.welu.composenavdestinations.navigation.destinations.ArgDestination
import com.welu.composenavdestinations.navigation.destinations.Destination
import com.welu.composenavdestinations.navigation.transitions.NavComponentTransitions
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


@DefaultNavGraph(isStart = true)
@NavDestinationDefinition
object FirstDestination : Destination {

    override val Content: DestinationCompositionScope = {
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

    @Composable
    fun StartDestinationComposable(
        currentValue: Int?,
        onClick: () -> Unit
    ) {
        Column {
            Text(text = "Current Value: $currentValue")
            Button(onClick = onClick) {
                Text(text = "Navigate to Second")
            }
        }
    }
}

@NavDestinationDefinition
object SecondDestination : ArgDestination<SecondDestination.NavArgs> {

    class NavArgs(val user: User? = User("123", "Sasi", 23))

//    @ExperimentalAnimationApi
//    override val transitions = object : NavComponentTransitions {
//        override val enterTransition: EnterTransitionProvider = {
//            when(targetState.composeDestination) {
//                is SecondDestination -> slideIntoContainer(AnimatedContentScope.SlideDirection.Right)
//                else -> null
//            }
//        }
//    }

    override val Content: ArgDestinationCompositionScope<NavArgs> = {
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
}

@NavDestinationDefinition("ThirdAdjustedRoute")
object ThirdDestination : ArgDestination<DetailScreenNavArgs> {

    override val Content: ArgDestinationCompositionScope<DetailScreenNavArgs> = {
        val vm = viewModel<DetailsVm>()
        DetailScreen(args = vm.args) {
            //navigate to nested Graph
            navController.navigate(
                OtherGraphSpec(
                    user = User("212", "HansPeter", 21)
                )
            )
        }
    }

}

@DefaultNavGraph
@NavGraphDefinition(
    argsClass = SecondDestination.NavArgs::class,
    transitionClass = NavComponentTransitions.Default::class
)
annotation class OtherGraph(
    val isStart: Boolean
)

@OtherGraph(true)
@NavDestinationDefinition
object FourthDestination : Destination {

    override val Content: DestinationCompositionScope = {
        val parentNavArgs = remember {
            OtherGraphSpec.argsFrom(getBackStackEntry(OtherGraphSpec)!!)
        }

        Column(modifier = Modifier
            .fillMaxWidth()
            .background(Color.Gray, RoundedCornerShape(15.dp))
            .padding(20.dp)
        ) {
            Text(text = "HELLO FROM FOURTH: ${parentNavArgs.user}")
        }
    }

}