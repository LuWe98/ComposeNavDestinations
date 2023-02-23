package com.welu.compose_nav_destinations_ksp.extensions

import com.welu.compose_nav_destinations_ksp.model.ImportInfo
import com.welu.compose_nav_destinations_ksp.model.ParameterType
import com.welu.compose_nav_destinations_ksp.model.ParameterTypeArgument
import com.welu.compose_nav_destinations_ksp.model.ParameterTypeInfo
import java.io.Serializable
import kotlin.reflect.KClass

fun KClass<*>.asParameterTypeInfo(
    vararg typeArgs: ParameterTypeInfo,
    isNullable: Boolean = false,
    isSerializable: Boolean = Serializable::class.java.isAssignableFrom(javaObjectType)
) = ParameterTypeInfo(
    isNullable = isNullable,
    type = ParameterType(
        import = ImportInfo(qualifiedName!!),
        typeArguments = typeArgs.map(::ParameterTypeArgument),
        isEnum = java.isEnum,
        isSerializable = isSerializable
    )
)

fun KClass<*>.asParamTypeInfo(vararg typeArgs: KClass<*>): ParameterTypeInfo = asBasicParameterTypeInfo(*typeArgs.map(KClass<*>::asParamTypeInfo).toTypedArray())

fun KClass<*>.asBasicParameterTypeInfo(vararg typeArgs: ParameterTypeInfo): ParameterTypeInfo = ParameterTypeInfo(
    ParameterType(
        import = ImportInfo(qualifiedName!!),
        typeArguments = typeArgs.map(::ParameterTypeArgument),
    )
)

fun ImportInfo.asParamTypeInfo(vararg typeArgs: KClass<*>): ParameterTypeInfo = asBasicParameterTypeInfo(*typeArgs.map(KClass<*>::asParamTypeInfo).toTypedArray())

fun ImportInfo.asBasicParameterTypeInfo(vararg typeArgs: ParameterTypeInfo): ParameterTypeInfo = ParameterTypeInfo(
    ParameterType(
        import = this,
        typeArguments = typeArgs.map(::ParameterTypeArgument),
    )
)