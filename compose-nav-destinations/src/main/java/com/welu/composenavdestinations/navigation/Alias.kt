package com.welu.composenavdestinations.navigation

import androidx.compose.runtime.Composable
import com.welu.composenavdestinations.navigation.scope.ArgDestinationScope
import com.welu.composenavdestinations.navigation.scope.PlainDestinationScope

typealias ArgCompositionScope<Arg> = @Composable ArgDestinationScope<Arg>.() -> Unit

typealias PlainCompositionScope = @Composable PlainDestinationScope.() -> Unit
