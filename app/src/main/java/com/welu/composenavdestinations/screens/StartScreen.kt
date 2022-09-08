package com.welu.composenavdestinations.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.welu.composenavdestinations.annotations.NavDestination

@NavDestination(route = "home")
@Composable
fun StartScreen(
    valueOne: Int?,
    valueTwo: Int?,
    navToDetail: () -> Unit,
    onRandomButtonClicked: () -> Unit
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
        Button(
            onClick = onRandomButtonClicked
        ) {
            Text(text = "SetRandom Page Result")
        }
    }
}
