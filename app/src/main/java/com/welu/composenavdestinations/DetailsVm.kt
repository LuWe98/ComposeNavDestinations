package com.welu.composenavdestinations

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.welu.composenavdestinations.screens.DetailScreenNavDestination

class DetailsVm(
    private val safeStateHandle: SavedStateHandle
): ViewModel() {

    val args by lazy {
        DetailScreenNavDestination.getArgs(safeStateHandle)
    }

}