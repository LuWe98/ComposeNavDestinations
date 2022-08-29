package com.welu.composenavdestinations.screens

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.welu.composenavdestinations.annotations.NavArgument
import com.welu.composenavdestinations.annotations.NavDestination

@NavDestination
@Composable
fun RandomScreen(
    @NavArgument
    name: String = "Hans"
) {
    Text(text = name)
}