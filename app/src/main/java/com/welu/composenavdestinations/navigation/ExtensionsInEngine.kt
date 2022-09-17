package com.welu.composenavdestinations.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.welu.composenavdestinations.navigation.scope.ArgDestinationScope
import com.welu.composenavdestinations.navigation.scope.PlainDestinationScope
import com.welu.composenavdestinations.navigation.spec.ArgDestinationSpec
import com.welu.composenavdestinations.navigation.spec.NavGraphSpec
import com.welu.composenavdestinations.navigation.spec.PlainDestinationSpec



@SuppressLint("UnrememberedGetBackStackEntry")
fun NavGraphBuilder.initDestinations(navController: NavHostController) {
//
//    composable(route = FirstDestinationSpec.route) {
//        FirstDestination.content(
//            PlainDestinationScope(
//                FirstDestinationSpec,
//                navController,
//                it
//            )
//        )
//    }
//
//    composable(
//        route = SecondDestinationSpec.route,
//        arguments = SecondDestinationSpec.arguments
//    ) {
//        SecondDestination.content(
//            ArgDestinationScope(
//                SecondDestinationSpec,
//                navController,
//                it,
//                lazy { SecondDestinationSpec.argsFrom(it) }
//            )
//        )
//    }
//
//    composable(
//        route = ThirdDestinationSpec.route,
//        arguments = ThirdDestinationSpec.arguments
//    ) {
//        ThirdDestination.content(
//            ArgDestinationScope(
//                ThirdDestinationSpec,
//                navController,
//                it,
//                lazy { ThirdDestinationSpec.argsFrom(it) }
//            )
//        )
//    }
//
//    //TODO -> Schauen wie das genau funktioniert mit den Argumenten bei den Navigations -> Es ist durchaus sinnvoll das auf NavigationGraph zu scopen, damit es jeder nutzen kann
//    // ArgsFrom sollte auch funktionieren, man muss halt nur den richtigen BackStackEntry mitgeben
//    navigation(
//        route = "nav/{age}",
//        startDestination = "nav2",
//        arguments = listOf(
//            navArgument("age", NavType.StringType)
//        ),
//        deepLinks = emptyList()
//    ) {
//        navigation(
//            route = "nav2",
//            startDestination = "comp/{age}"
//        ) {
//            composable(
//                route = "comp/{age}",
//                arguments = listOf(
//                    navArgument("age", NavType.StringType)
//                )
//            ) {
//
//                val nav1Entry = remember {
//                    navController.getBackStackEntry("nav/{age}")
//                }
//
//                val nav2Entry = remember {
//                    navController.getBackStackEntry("nav2")
//                }
//
//                val ageArgParent1 = nav1Entry.arguments?.getString("age")
//                val ageArgParent2 = nav2Entry.arguments?.getString("age")
//                val ageArg = it.arguments?.getString("age")
//
//                Column {
//                    Text(text = "DESTINATION 1")
//                    Text(text = "Parsed Nav1: $ageArgParent1")
//                    Text(text = "Parsed Nav2: $ageArgParent2")
//                    Text(text = "Parsed Comp1: $ageArg")
//                    Text(text = "Entry Nav1: $nav1Entry")
//                    Text(text = "Entry Nav2: $nav2Entry")
//                    Text(text = "Entry Comp1: $it")
//                    Button(onClick = { navController.navigate("comp2/132323") }) {
//                        Text(text = "Navigate")
//                    }
//                }
//            }
//
//            composable(
//                route = "comp2/{age}",
//                arguments = listOf(
//                    navArgument("age", NavType.StringType)
//                )
//            ) {
//
//                val nav1Entry = remember {
//                    navController.getBackStackEntry("nav/{age}")
//                }
//
//                val nav2Entry = remember {
//                    navController.getBackStackEntry("nav2")
//                }
//
//                val comp12Entry = remember {
//                    navController.getBackStackEntry("nav/{age}")
//                }
//
//                val ageArgParent1 = nav1Entry.arguments?.getString("age")
//                val ageArgParent2 = nav2Entry.arguments?.getString("age")
//                val ageArgComp1 = comp12Entry.arguments?.getString("age")
//                val ageArg = it.arguments?.getString("age")
//
//                Column {
//                    Text(text = "DESTINATION 2")
//                    Text(text = "Parsed Nav1: $ageArgParent1")
//                    Text(text = "Parsed Nav2: $ageArgParent2")
//                    Text(text = "Parsed Comp1: $ageArgComp1")
//                    Text(text = "Parsed Comp2: $ageArg")
//                    Text(text = "Entry Nav1: $nav1Entry")
//                    Text(text = "Entry Nav2: $nav2Entry")
//                    Text(text = "Entry Comp1: $comp12Entry")
//                    Text(text = "Entry Comp2: $it")
//                }
//            }
//        }
//    }
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
