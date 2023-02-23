package com.welu.compose_nav_destinations_ksp.model

//TODO -> Eventuell noch ändern/umbauen -> Sind eigentlich zu viele Informationen
data class ParameterDefaultValueDeclaration(
    val name: String,
    val range: IntRange,
    val callingDeclaration: ParameterDefaultValueDeclaration? = null
)
