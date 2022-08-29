package com.welu.composenavdestinations.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.welu.composenavdestinations.annotations.NavDestination

@NavDestination(route = "home")
@Composable
fun StartScreen(
    navToDetail: () -> Unit
) {
    Column {
        Text(text = "boolean")
        Button(onClick = navToDetail) {
            Text(text = "Navigate")
        }
    }
}