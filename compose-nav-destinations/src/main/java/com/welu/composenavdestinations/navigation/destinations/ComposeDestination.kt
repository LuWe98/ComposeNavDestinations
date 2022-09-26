package com.welu.composenavdestinations.navigation.destinations

import androidx.compose.runtime.Composable
import com.welu.composenavdestinations.navigation.scope.ComposeDestinationScope

sealed interface ComposeDestination <Scope: ComposeDestinationScope> {
    val Content : @Composable Scope.() -> Unit
}