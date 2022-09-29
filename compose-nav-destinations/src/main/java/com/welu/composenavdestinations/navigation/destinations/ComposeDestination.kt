package com.welu.composenavdestinations.navigation.destinations

import androidx.compose.runtime.Composable
import com.welu.composenavdestinations.navigation.scope.ComposeDestinationScope

sealed interface ComposeDestination<Scope: ComposeDestinationScope> {
    @Suppress("PropertyName")
    val Content : @Composable Scope.() -> Unit
}