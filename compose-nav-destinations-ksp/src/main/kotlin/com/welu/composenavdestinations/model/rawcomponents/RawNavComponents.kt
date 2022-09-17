package com.welu.composenavdestinations.model.rawcomponents

import com.welu.composenavdestinations.model.components.NavGraphInfo
import com.welu.composenavdestinations.model.components.NavDestinationInfo

/**
 * Represents all raw Components of the App. [NavGraphInfo] and [NavDestinationInfo] can then be generated with this information.
 */
data class RawNavComponents(
    val rawNavGraphInfos: Sequence<RawNavGraphInfo>,
    val rawNavDestinationInfos: Sequence<RawNavDestinationInfo>
)
