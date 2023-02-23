package com.welu.compose_nav_destinations_ksp.model

import com.google.devtools.ksp.symbol.Variance

data class ParameterTypeArgument(
    val typeInfo: ParameterTypeInfo? = null,
    val variance: Variance = Variance.INVARIANT
) {

    val label get() = variance.label

    companion object {
        val STAR = ParameterTypeArgument(variance = Variance.STAR)
    }
}