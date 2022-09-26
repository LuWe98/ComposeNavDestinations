package com.welu.composenavdestinations.navigation.destinations

import com.welu.composenavdestinations.navigation.BottomSheetDestinationCompositionScope
import com.welu.composenavdestinations.navigation.scope.BottomSheetDestinationScope

interface BottomSheetDestination: ComposeRoutableDestination<BottomSheetDestinationScope> {

    /**
     * Composables can be written inside of this typealias Scope Definition
     */
    override val Content: BottomSheetDestinationCompositionScope

}