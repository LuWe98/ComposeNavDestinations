package com.welu.composenavdestinations.navigation.destinations

import com.welu.composenavdestinations.navigation.BottomSheetArgDestinationCompositionScope
import com.welu.composenavdestinations.navigation.scope.BottomSheetArgDestinationScope

interface BottomSheetArgDestination<Arg: Any>: ComposeArgDestination<Arg, BottomSheetArgDestinationScope<Arg>> {

    /**
     * Composables can be written inside of this typealias Scope Definition
     */
    override val Content: BottomSheetArgDestinationCompositionScope<Arg>

}