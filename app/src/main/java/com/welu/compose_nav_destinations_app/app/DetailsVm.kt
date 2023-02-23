package com.welu.compose_nav_destinations_app.app

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.welu.composenavdestinations.extensions.argsFrom

class DetailsVm(
    private val safeStateHandle: SavedStateHandle
): ViewModel() {

    val args by lazy {
        ThirdDestination.argsFrom(safeStateHandle)
    }

}