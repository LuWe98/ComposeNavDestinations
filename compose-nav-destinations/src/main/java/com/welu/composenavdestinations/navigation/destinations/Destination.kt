package com.welu.composenavdestinations.navigation.destinations

import androidx.compose.runtime.Composable
import com.welu.composenavdestinations.navigation.scope.DestinationScope

sealed interface Destination <Scope: DestinationScope> {
    val Content : @Composable Scope.() -> Unit
}