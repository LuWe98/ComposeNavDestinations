package com.welu.composenavdestinations.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.welu.composenavdestinations.DetailsVm
import com.welu.composenavdestinations.annotations.NavArgument
import com.welu.composenavdestinations.annotations.NavDestination

@NavDestination
@Composable
fun RandomScreen(
    @NavArgument
    name: String = "Hans",
    valueTest: Any?,
    onCLick: () -> Unit
) {
    LocalContext.current.applicationContext

    Surface(
        modifier = Modifier
            .height(200.dp)
            .width(200.dp)
            .padding(10.dp),
        color = Color.Gray,
        shape = RoundedCornerShape(15.dp)
    ) {
        Column {
            Text(
                text = "$name - $valueTest",
                modifier = Modifier.clickable {
//                    StartScreenNavDestination.sendResult(Random.nextInt())
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "OTHER:",
                modifier = Modifier.clickable {
                    onCLick()
                }
            )
        }

    }
}

@Composable
fun RandomScreen2() {
    val vm = viewModel<DetailsVm>()


}