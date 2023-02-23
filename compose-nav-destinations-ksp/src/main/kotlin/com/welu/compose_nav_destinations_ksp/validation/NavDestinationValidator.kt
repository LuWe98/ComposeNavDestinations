package com.welu.compose_nav_destinations_ksp.validation

import com.welu.compose_nav_destinations_ksp.model.components.ComposeDestinationInfo

//TODO -> Validation Classes bauen -> NavDestinationValidator, NavGraphValidator, NavComponentValidator
object NavDestinationValidator {

    fun validate(destinations: Sequence<ComposeDestinationInfo>) {
        val areDuplicateNamesPresent = destinations.groupBy(ComposeDestinationInfo::simpleName).any { it.value.size > 1 }
        if(areDuplicateNamesPresent) {
            throw com.welu.compose_nav_destinations_ksp.exceptions.DuplicateDestinationException
        }

        val areDuplicateRoutesPresent = destinations.groupBy(ComposeDestinationInfo::baseRoute).any { it.value.size > 1 }
        if(areDuplicateRoutesPresent) {
            throw com.welu.compose_nav_destinations_ksp.exceptions.DuplicateRouteException
        }

    }

}