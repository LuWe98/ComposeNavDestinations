package com.welu.composenavdestinations.model

data class Parameter(
    val name: String,
    val typeInfo: ParameterTypeInfo,
    val defaultValue: DefaultValue? = null
) {

    val hasDefaultValue get() = defaultValue != null

    val imports
        get(): List<PackageImport> = typeInfo.type.typeArguments.filterIsInstance<ParameterTypeArgument.Typed>().flatMap {
            it.typeInfo.allChildImports
        }.toMutableList().apply {
            add(typeInfo.type.import)
            defaultValue?.requiredImports?.let(::addAll)
        }

    val fullTypeName get() = typeInfo.definition

    val fullName get() = "$name: $fullTypeName"

    val fullDeclarationName get() = fullName + defaultValue?.let { " = " + it.value }

}