package com.welu.compose_nav_destinations_ksp.model.components.rawcomponents

/**
 * Represents all raw Components that were annotate.
 * [ComposeNavGraphInfo] and [ComposeDestinationInfo] can then be generated with this information.
 */
data class RawNavComponents(
    val rawNavGraphInfos: Sequence<RawComposeNavGraphInfo>,
    val rawDestinationInfos: Sequence<RawComposeDestinationInfo>
)
