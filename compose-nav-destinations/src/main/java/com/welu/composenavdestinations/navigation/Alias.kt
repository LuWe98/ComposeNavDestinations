package com.welu.composenavdestinations.navigation

import androidx.compose.runtime.Composable
import com.welu.composenavdestinations.navigation.scope.*

typealias DestinationCompositionScope = @Composable DestinationScope.() -> Unit

typealias ArgDestinationCompositionScope<Arg> = @Composable ArgDestinationScope<Arg>.() -> Unit

typealias DialogDestinationCompositionScope = @Composable DialogDestinationScope.() -> Unit

typealias DialogArgDestinationCompositionScope<Arg> = @Composable DialogArgDestinationScope<Arg>.() -> Unit

typealias BottomSheetDestinationCompositionScope = @Composable BottomSheetDestinationScope.() -> Unit

typealias BottomSheetArgDestinationCompositionScope<Arg> = @Composable BottomSheetArgDestinationScope<Arg>.() -> Unit
