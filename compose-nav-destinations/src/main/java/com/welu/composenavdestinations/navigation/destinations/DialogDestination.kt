package com.welu.composenavdestinations.navigation.destinations

import androidx.compose.ui.window.DialogProperties
import com.welu.composenavdestinations.navigation.DialogDestinationCompositionScope
import com.welu.composenavdestinations.navigation.scope.DialogDestinationScope

interface DialogDestination: ComposeRoutableDestination<DialogDestinationScope> {

    /**
     * The properties for this [DialogDestination]
     */
    val properties: DialogProperties get() = DialogProperties()

    /**
     * Composables can be written inside of this typealias Scope Definition
     */
    override val Content: DialogDestinationCompositionScope

}