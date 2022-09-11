package com.welu.composenavdestinations.destinations

import androidx.compose.runtime.Composable
import com.welu.composenavdestinations.destinations.scope.DestinationScope

sealed interface Destination <Scope: DestinationScope> {
    val content : @Composable Scope.() -> Unit
}