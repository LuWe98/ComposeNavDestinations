package com.welu.composenavdestinations.destinations.scope

import androidx.compose.runtime.Composable

typealias ArgCompositionScope<Arg> = @Composable ArgDestinationScope<Arg>.() -> Unit

typealias PlainCompositionScope = @Composable PlainDestinationScope.() -> Unit
