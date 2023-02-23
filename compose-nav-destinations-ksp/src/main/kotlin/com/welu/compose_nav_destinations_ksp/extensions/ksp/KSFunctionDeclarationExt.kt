package com.welu.compose_nav_destinations_ksp.extensions.ksp

import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import com.google.devtools.ksp.validate

val KSFunctionDeclaration.validParameters get() = parameters.filter(KSValueParameter::validate)
