package com.welu.composenavdestinations

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.welu.composenavdestinations.extensions.argsFrom
import com.welu.composenavdestinations.screens.tests.ThirdDestination

class DetailsVm(
    private val safeStateHandle: SavedStateHandle
): ViewModel() {

    val args by lazy {
        ThirdDestination.argsFrom(safeStateHandle)
    }

}