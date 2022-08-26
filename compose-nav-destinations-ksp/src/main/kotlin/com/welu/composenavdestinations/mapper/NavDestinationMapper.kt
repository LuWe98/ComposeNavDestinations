package com.welu.composenavdestinations.mapper

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.*
import com.welu.composenavdestinations.annotationinfo.NavDestinationAnnotation
import com.welu.composenavdestinations.extensions.ksp.*
import com.welu.composenavdestinations.mapper.DefaultValueExtractor.getDefaultValue
import com.welu.composenavdestinations.model.*
import com.welu.composenavdestinations.utils.StandardLibraries
import java.io.Serializable

class NavDestinationMapper(
    private val resolver: Resolver,
    private val logger: KSPLogger,
    private val navArguments: Sequence<KSValueParameter>
) : AnnotationMapper<KSFunctionDeclaration, NavDestinationInfo> {

    private val ksFileContentMap by lazy { mutableMapOf<KSFile, KSFileContent>() }

    private val parcelableType: KSType by lazy { resolver.getTypeWithClassName(StandardLibraries.PARCELABLE) }

    private val KSType?.isParcelable get() = this?.let(parcelableType::isAssignableFrom) ?: false

    private val serializableType: KSType by lazy { resolver.getTypeWithClass(Serializable::class) }

    private val KSType?.isSerializable get() = this?.let(serializableType::isAssignableFrom) ?: false

    private val listType: KSType by lazy { resolver.getStarProjectedTypeWithClass(List::class) }

    private val KSType?.isList get() = this?.let(listType::isAssignableFrom) ?: false

    private val setType: KSType by lazy { resolver.getStarProjectedTypeWithClass(Set::class) }

    private val KSType?.isSet get() = this?.let(setType::isAssignableFrom) ?: false


    override fun map(declaration: KSFunctionDeclaration): NavDestinationInfo {

        val destinationName = declaration.simpleName.asString() + "NavDestination"

        val routeArg: String = declaration.getRouteName()

        var destinationInfo = NavDestinationInfo(
            name = destinationName,
            route = routeArg,
            functionDeclaration = declaration
        )

        // Die NavArgs Klasse wird verwendet, wenn man eine angibt, ansonsten wird eine generiert mit @NavArgument annotierten Parametern
        val navArgsClass: KSClassDeclaration = declaration.getNavArgsClass()

        val validParameters: List<KSValueParameter>

        if (navArgsClass.qualifiedName?.asString() != Unit::class.qualifiedName) {
            validParameters = navArgsClass.primaryConstructor?.validParameters ?: throw IllegalStateException("")
            destinationInfo = destinationInfo.copy(navArgsClass = navArgsClass)
        } else {
            validParameters = declaration.parameters.filter(navArguments::contains)
        }

        if (validParameters.isEmpty()) return destinationInfo

        val fileContent: KSFileContent = declaration.getKSFileContent(validParameters)

        val parameters = validParameters.map { parameter ->
            mapToParameter(parameter, fileContent)
        }

        return destinationInfo.copy(parameters = parameters)
    }

    private fun KSDeclaration.getKSFileContent(parameters: List<KSValueParameter>): KSFileContent {
        //Checks if any default Value is Present. If yes, we load the related file in order to read those and extract the imports
        if (parameters.none(KSValueParameter::hasDefault)) return KSFileContent()

        //Checkt ob das File mit den Import schon geladen wurden oder nicht. Lädt es nur, wenn es noch nicht reingeladen wurde
        val rootKSFile = rootKSFile
        return ksFileContentMap[rootKSFile] ?: run {
            val fileLines = rootKSFile.fileLines
            return KSFileContent(fileLines, rootKSFile.extractImports(lines = fileLines)).also {
                ksFileContentMap[rootKSFile] = it
            }
        }
    }

    private fun mapToParameter(valueParameter: KSValueParameter, fileContent: KSFileContent): Parameter {
        //Das ist der Name von der Property
        val parameterName = valueParameter.name!!.asString()
        //Das ist der resolved Type
        val resolvedType = valueParameter.type.resolve()
        //Das ist der Import Name
        val qualifiedType = resolvedType.declaration.qualifiedName?.asString() ?: resolvedType.declaration.simpleName.asString()
        //Gets the parameter Type Info
        val parameterTypeInfo = resolvedType.asParameterTypeInfo ?: throw IllegalStateException("Parameter is invalid!")
        //Default Value Check and retrieval
        val parameterDefValue = valueParameter.getDefaultValue(resolver, fileContent, qualifiedType)

        return Parameter(
            name = parameterName,
            typeInfo = parameterTypeInfo,
            defaultValue = parameterDefValue
        )
    }

    private val KSType.asParameterTypeInfo get(): ParameterTypeInfo? {
        if (declaration.qualifiedName == null) return null

        val classDeclaration: KSClassDeclaration = (getTypeAliasDeclaration() ?: declaration) as KSClassDeclaration
        val classDeclarationType: KSType = classDeclaration.asType

        val packageImport = PackageImport(
            simpleName = classDeclaration.simpleName.asString(),
            qualifiedName = classDeclaration.qualifiedName?.asString() ?: declaration.qualifiedName!!.asString()
        )

        return ParameterTypeInfo(
            isNullable = isMarkedNullable,
            type = ParameterType(
                import = packageImport,
                typeArguments = extractedParameterTypeArguments,
                isSerializable = classDeclarationType.isSerializable,
                isParcelable = classDeclarationType.isParcelable,
                isEnum = classDeclaration.isEnum,
                isList = classDeclarationType.isList,
                isSet = classDeclarationType.isSet
            )
        )
    }

    private val KSType.extractedParameterTypeArguments get(): List<ParameterTypeArgument> = arguments.mapNotNull { arg ->
        if (arg.variance == Variance.STAR) return@mapNotNull ParameterTypeArgument.Star

        val resolvedType = arg.type?.resolve() ?: return@mapNotNull null

        // Alternativ einfach null zurückgeben
        if (resolvedType.isError) throw IllegalArgumentException("Resolved type contains errors: $resolvedType")

        ParameterTypeArgument.Typed(
            typeInfo = resolvedType.asParameterTypeInfo ?: return@mapNotNull null,
            varianceLabel = arg.variance.label
        )
    }

    private fun KSFunctionDeclaration.getRouteName() =
        getAnnotationArgument<String>(NavDestinationAnnotation.ROUTE_ARG, NavDestinationAnnotation).ifBlank(simpleName::asString)

    private fun KSFunctionDeclaration.getNavArgsClass(): KSClassDeclaration =
        (getAnnotationArgument<KSType>(NavDestinationAnnotation.NAV_ARGS_ARG, NavDestinationAnnotation).declaration as KSClassDeclaration)

}