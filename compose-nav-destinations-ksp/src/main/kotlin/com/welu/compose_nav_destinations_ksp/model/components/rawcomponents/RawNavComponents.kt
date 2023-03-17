package com.welu.compose_nav_destinations_ksp.model.components.rawcomponents
import com.welu.compose_nav_destinations_ksp.model.components.ComposeDestinationInfo
import com.welu.compose_nav_destinations_ksp.model.components.ComposeNavGraphInfo

/**
 * Represents all raw Components that were annotated.
 * [ComposeNavGraphInfo] and [ComposeDestinationInfo] can then be generated with this information.
 */
data class RawNavComponents(
    val rawNavGraphInfos: List<RawComposeNavGraphInfo>,
    val rawDestinationInfos: List<RawComposeDestinationInfo>
)