package com.welu.compose_nav_destinations_app.app

import android.os.Parcelable
import android.util.Log
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
import com.welu.composenavdestinations.annotations.ComposeDestination
import com.welu.composenavdestinations.annotations.ComposeNavGraph
import com.welu.composenavdestinations.annotations.DefaultNavGraph
import com.welu.composenavdestinations.extensions.*
import com.welu.composenavdestinations.extensions.navigation.areArgumentsSetCorrectly
import com.welu.composenavdestinations.extensions.navigation.getBackStackEntry
import com.welu.composenavdestinations.extensions.navigation.navigate
import com.welu.composenavdestinations.navgraphs.OtherGraphSpec
import com.welu.composenavdestinations.navigation.ArgDestinationCompositionScope
import com.welu.composenavdestinations.navigation.DestinationCompositionScope
import com.welu.composenavdestinations.navigation.DialogArgDestinationCompositionScope
import com.welu.composenavdestinations.navigation.destinations.ArgDestination
import com.welu.composenavdestinations.navigation.destinations.Destination
import com.welu.composenavdestinations.navigation.destinations.DialogArgDestination
import com.welu.composenavdestinations.result.DestinationResultListener
import com.welu.compose_nav_destinations_app.app.screens.DetailScreen
import com.welu.compose_nav_destinations_app.app.screens.DetailScreenNavArgs
import com.welu.composenavdestinations.extensions.navigation.navigateAndPopUpTo
import com.welu.composenavdestinations.extensions.navigation.popBackStack
import com.welu.composenavdestinations.navigation.spec.DestinationSpec
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
@Parcelize
data class User(
    val id: String,
    val name: String,
    val age: Int
) : Parcelable


@DefaultNavGraph
@ComposeNavGraph(argsClass = DetailScreenNavArgs::class)
annotation class OtherGraph(
    val isStart: Boolean = false
)

@DefaultNavGraph(isStart = true)
@ComposeDestination
object FirstDestination : Destination {

    override val Content: DestinationCompositionScope = {
        var parsedValue: Int? by rememberSaveable { mutableStateOf(null) }

        //Hier muss man nochmal schauen wegen den Results -> Auch ResultTypes einbauen, dass es nicht zu crashes bei Runtime kommen kann
        //ResultTypes wie NavTypes für verschiedene Typen.
        DestinationResultListener<Int> {
            println("RESULT RECEIVED: $it")
            parsedValue = it
        }

        StartDestinationComposable(parsedValue) {
            navigate(SecondDestination(User("123", "Lucha", 23)))
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


@OtherGraph
@ComposeDestination
object Lol : Destination {
    override val Content: DestinationCompositionScope = {
        Text(text = "Hallo")
    }
}

@ComposeDestination
object SecondDestination : ArgDestination<SecondDestination.NavArgs> {

    class NavArgs(val user: User)

    override val Content: ArgDestinationCompositionScope<NavArgs> = {
        TestDestinationComposable(
            user = args.user,
            navigateBack = {
                popBackStack(FirstDestination)
            },
            navigateToThirdScreen = {
                navigateAndPopUpTo(ThirdDestination(), FirstDestination, false)
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

@ComposeDestination
object ThirdDestination : ArgDestination<DetailScreenNavArgs> {

    override val Content: ArgDestinationCompositionScope<DetailScreenNavArgs> = {
        val vm = viewModel<DetailsVm>()

        DetailScreen(args = vm.args) {
            //navigate to nested Graph
            //navController.navigate(OtherGraphSpec())
            Log.d("TTT", "HALLO")
            popBackStack(FirstDestination)

            //navController.navigate("other/123")
        }
    }

}


@OtherGraph(true)
@ComposeDestination
object FourthDestination : DialogArgDestination<FourthDestination.NavArgs> {

    data class NavArgs(val id: String)

    override val Content: DialogArgDestinationCompositionScope<NavArgs> = {

        val parentNavArgs = remember {
            OtherGraphSpec.argsFrom(getBackStackEntry(OtherGraphSpec)!!)
        }

        //TODO -> Das schauen - Was machen wenn die nicht richtig gesetzt wurden?
        // -> Dann mit dem Import bei den DestinationExt
        // -> Dann Lint checks
        println("HALLO: ${areArgumentsSetCorrectly()}")
        //println("TEST: $args")

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray, RoundedCornerShape(15.dp))
                .padding(20.dp)
        ) {
            Text(text = "HELLO FROM FOURTH: $parentNavArgs")
        }
    }
}