package com.welu.composenavdestinations.mapper

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.*
import com.welu.composenavdestinations.annotations.NavDestinationDefinitionAnnotation
import com.welu.composenavdestinations.exceptions.UnsupportedTypeException
import com.welu.composenavdestinations.extensions.isOneOf
import com.welu.composenavdestinations.extensions.ksp.*
import com.welu.composenavdestinations.mapper.ParameterDefaultValueExtractor.extractDefaultValue
import com.welu.composenavdestinations.mapper.ParameterNavArgInfoExtractor.extractParameterNavArgInfo
import com.welu.composenavdestinations.model.*
import com.welu.composenavdestinations.utils.PackageUtils
import java.io.Serializable

class MapperNavDestinations(
    private val resolver: Resolver,
    private val logger: KSPLogger
) : AnnotationMapper<KSClassDeclaration, NavDestinationInfo> {

    private val ksFileContentMap by lazy { mutableMapOf<KSFile, KSFileContent>() }

    private val parcelableType: KSType by lazy { resolver.getTypeWithImportInfo(PackageUtils.ANDROID_PARCELABLE_IMPORT) }

    private val KSType?.isParcelable get() = this?.let(parcelableType::isAssignableFrom) ?: false

    private val serializableType: KSType by lazy { resolver.getTypeWithClass(Serializable::class) }

    private val KSType?.isSerializable get() = this?.let(serializableType::isAssignableFrom) ?: false

    private val listType: KSType by lazy { resolver.getStarProjectedTypeWithClass(List::class) }

    private val KSType?.isValidList
        get(): Boolean = this?.declaration?.qualifiedName?.asString()?.isOneOf(*PackageUtils.VALID_LIST_QUALIFIERS) ?: false

    private val KSType?.isList
        get() = this?.let(listType::isAssignableFrom)?.also {
            if (it && !isValidList) {
                throw UnsupportedTypeException(
                    "Used List Subtype: ${declaration.qualifiedName?.asString()} is not supported. Use List, MutableList, AbstractList or ArrayList instead."
                )
            }
        } ?: false

    private val setType: KSType by lazy { resolver.getStarProjectedTypeWithClass(Set::class) }

    private val KSType?.isValidSet
        get(): Boolean = this?.declaration?.qualifiedName?.asString()?.isOneOf(*PackageUtils.VALID_SET_QUALIFIERS) ?: false

    private val KSType?.isSet
        get() = this?.let(setType::isAssignableFrom)?.also {
            if (it && !isValidSet) {
                throw UnsupportedTypeException(
                    "Used Set Subtype: ${declaration.qualifiedName?.asString()} is not supported. Use Set, MutableSet, AbstractSet or HashSet instead"
                )
            }
        } ?: false


    override fun map(declaration: KSClassDeclaration) = NavDestinationInfo(
        route = declaration.getRouteParameter(),
        navArgsInfo = declaration.extractNavArgsInfo(),
        destinationImport = ImportInfo(
            simpleName = declaration.simpleName.asString(),
            packageDir = declaration.packageName.asString()
        ),
        destinationSpecImport = ImportInfo(
            simpleName = declaration.simpleName.asString() + PackageUtils.NAV_DESTINATION_SPEC_SUFFIX,
            packageDir = declaration.packageName.asString()
        )
    )

    private fun KSClassDeclaration.extractNavArgsInfo(): NavArgsInfo? {
        //Es handelt sich um eine PlainDestination -> Es müssen keine Parameter extrahiert werden
        val typeInfoWithDeclaration: ParameterTypeInfoAndDeclaration = extractNavArgsTypeInfo() ?: return null

        val validParameters: List<KSValueParameter> = typeInfoWithDeclaration
            .classDeclaration
            .primaryConstructor
            ?.validParameters ?: throw IllegalStateException("NavArgs does not have a primary Constructor!")

        //Es handelt sich um eine ArgDefinition, jedoch ohne jegliche Parameter
        if (validParameters.isEmpty()) {
            throw IllegalStateException("NavArgs ha no Parameters, implement PlainDestination instead!")
        }

        val fileContent: KSFileContent = typeInfoWithDeclaration.classDeclaration.getKSFileContent(validParameters)

        return NavArgsInfo(
            typeInfo = typeInfoWithDeclaration.typeInfo,
            parameters = validParameters.map { mapToParameter(it, fileContent) }
        )
    }

    /**
     * Loads the content of a File for default value extractions -> Is therefore only needed, when default values are present
     *
     * Loaded Files are saved inside a Map for reuse.
     */
    private fun KSDeclaration.getKSFileContent(parameters: List<KSValueParameter>): KSFileContent {
        if (parameters.none(KSValueParameter::hasDefault)) return KSFileContent.EMPTY

        val rootKSFile = getRootKSFile()

        ksFileContentMap[rootKSFile]?.let { return it }

        val fileLines = rootKSFile.getFileLines()
        //TODO -> Nochmal schauen wegen dem extracten
        return KSFileContent(fileLines, rootKSFile.extractImports(lines = fileLines)).also {
            ksFileContentMap[rootKSFile] = it
        }
    }

    /**
     * Maps the NavArgs parameter to an Parameter representation for Code Generation
     */
    private fun mapToParameter(valueParameter: KSValueParameter, fileContent: KSFileContent): Parameter {
        //Das ist der Name von der Property
        val parameterName = valueParameter.name!!.asString()
        //Das ist der resolved Type
        val resolvedType = valueParameter.type.resolve()
        //Das ist der Import Name
        val qualifiedType = resolvedType.declaration.qualifiedName?.asString() ?: resolvedType.declaration.simpleName.asString()
        //Gets the parameter Type Info
        val parameterTypeInfo = resolvedType.toParameterTypeInfo() ?: throw IllegalStateException("Parameter is invalid!")
        //Default Value Check and retrieval
        val parameterDefaultValue = valueParameter.extractDefaultValue(resolver, fileContent, qualifiedType)
        //The NavArgInfo for the according NavType<T>
        val parameterNavArgInfo = parameterTypeInfo.extractParameterNavArgInfo()

        return Parameter(
            name = parameterName,
            typeInfo = parameterTypeInfo,
            navArgTypeInfo = parameterNavArgInfo,
            defaultValue = parameterDefaultValue
        )
    }

    private fun KSClassDeclaration.extractNavArgsTypeInfo(): ParameterTypeInfoAndDeclaration? {
        val resolvedType = superTypes.firstOrNull()?.resolve() ?: throw IllegalStateException("Destination does not implement the necessary Destination Interface!")

        return when (resolvedType.declaration.qualifiedName!!.asString()) {
            PackageUtils.NAV_PLAIN_DESTINATION_IMPORT.qualifiedName -> null
            PackageUtils.NAV_ARG_DESTINATION_IMPORT.qualifiedName -> resolvedType.extractNavArgsGenericParameterInfo()
            else -> throw IllegalStateException("Destination does not implement the necessary Destination Interface!")
        }
    }

    private fun KSType.toParameterTypeInfo(): ParameterTypeInfo? = toParameterTypeInfoWithClassDeclaration()?.typeInfo

    private fun KSType.toParameterTypeInfoWithClassDeclaration(): ParameterTypeInfoAndDeclaration? {
        if (declaration.qualifiedName == null) return null

        val classDeclaration: KSClassDeclaration = getTypeAliasClassDeclaration()
        val classDeclarationType: KSType = classDeclaration.asType
        val importInfo = ImportInfo(classDeclaration.qualifiedName?.asString() ?: declaration.qualifiedName!!.asString())

        return ParameterTypeInfoAndDeclaration(
            classDeclaration = classDeclaration,
            typeInfo = ParameterTypeInfo(
                isNullable = isMarkedNullable,
                type = ParameterType(
                    import = importInfo,
                    typeArguments = extractParameterTypeArguments(),
                    isEnum = classDeclaration.isEnum,
                    isSerializable = classDeclarationType.isSerializable,
                    isParcelable = classDeclarationType.isParcelable,
                    isList = classDeclarationType.isList,
                    isSet = classDeclarationType.isSet
                )
            )
        )
    }

    private fun KSType.extractParameterTypeArguments(): List<ParameterTypeArgument> = arguments.mapNotNull { it.extractParameterTypeArgument() }

    private fun KSTypeArgument.extractParameterTypeArgument(): ParameterTypeArgument? {
        if (variance == Variance.STAR) return ParameterTypeArgument.STAR
        val typeInfo = toResolvedType()?.toParameterTypeInfo() ?: return null
        return ParameterTypeArgument(typeInfo, variance)
    }

    private fun KSType.extractNavArgsGenericParameterInfo(): ParameterTypeInfoAndDeclaration? = arguments
        .first()
        .toResolvedType()
        ?.toParameterTypeInfoWithClassDeclaration()

    //TODO -> Annotation universal machen und dann instanzen der Annotationen erstellen -> NavDestinationDefinition("", "")
    private fun KSClassDeclaration.getRouteParameter(): String = getAnnotationArgument<String>(
        argName = NavDestinationDefinitionAnnotation.ROUTE_ARG,
        annotation = NavDestinationDefinitionAnnotation
    ).ifBlank { simpleName.asString() }




//    private fun KSClassDeclaration.getNavArgsClass(): KSClassDeclaration =
//        (getAnnotationArgument<KSType>(NavDestinationAnnotation.NAV_ARGS_ARG, NavDestinationAnnotation).declaration as KSClassDeclaration)

    //        //Es handelt sich um eine PlainDestination -> Es müssen keine Parameter extrahiert werden
//        if (typeInfoWithDeclaration == null) return destinationInfo
//
//        val validParameters: List<KSValueParameter> = typeInfoWithDeclaration
//            .classDeclaration
//            .primaryConstructor
//            ?.validParameters ?: throw IllegalStateException("NavArgs does not have a primary Constructor!")
//
//        //Es handelt sich um eine ArgDefinition, jedoch ohne jegliche Parameter
//        if (validParameters.isEmpty()) {
//            throw IllegalStateException("NavArgs ha no Parameters, implement PlainDestination instead!")
//        }
//
//        val fileContent: KSFileContent = typeInfoWithDeclaration.classDeclaration.getKSFileContent(validParameters)
//
//        return destinationInfo.copy(
//            navArgsInfo = NavDestinationNavArgsInfo(
//                typeInfo = typeInfoWithDeclaration.typeInfo,
//                parameters = validParameters.map { mapToParameter(it, fileContent) }
//            )
//        )
}