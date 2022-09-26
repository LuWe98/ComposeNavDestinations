package com.welu.composenavdestinations.navigation.destinations

import androidx.compose.ui.window.DialogProperties
import com.welu.composenavdestinations.navigation.DialogArgDestinationCompositionScope
import com.welu.composenavdestinations.navigation.scope.DialogArgDestinationScope

interface DialogArgDestination<Arg: Any>: ComposeArgDestination<Arg, DialogArgDestinationScope<Arg>> {

    /**
     * The properties for this [DialogArgDestination]
     */
    val properties: DialogProperties get() = DialogProperties()

    /**
     * Composables can be written inside of this typealias Scope Definition
     */
    override val Content: DialogArgDestinationCompositionScope<Arg>

}