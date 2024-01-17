package com.welu.compose_nav_destinations_app.app.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

//@NavDestination
@Composable
fun StartScreen(
    valueOne: Int?,
    valueTwo: Int?,
    navToDetail: () -> Unit
) {
    Column {
        Text(
            text = "boolean - One: $valueOne - Two: $valueTwo",
            modifier = Modifier.clickable {
//                StartScreenNavDestination.sendResult(Random.nextInt())
//                StartScreenNavDestination.sendResult(Random.nextInt(), "2")
            }
        )
        Button(
            onClick = navToDetail
        ) {
            Text(text = "Navigate")
        }
    }
}
