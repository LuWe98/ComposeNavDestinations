package com.welu.composenavdestinations.extractor

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.*
import com.welu.composenavdestinations.exceptions.UnsupportedTypeException
import com.welu.composenavdestinations.extensions.isOneOf
import com.welu.composenavdestinations.extensions.ksp.*
import com.welu.composenavdestinations.extractor.NavArgTypeInfoExtractor.extractParameterNavArgTypeInfo
import com.welu.composenavdestinations.model.*
import com.welu.composenavdestinations.model.components.NavDestinationType
import com.welu.composenavdestinations.utils.PackageUtils
import java.io.Serializable

class NavArgsInfoExtractor(
    private val resolver: Resolver,
    private val logger: KSPLogger,
    private val defaultValueExtractor: DefaultValueExtractor
) {

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

    /**
     * This method extracts the NavArgsInfo from the given NavArgs ParameterTypeInfoAndDeclaration.
     *
     * This is used if the NavArgs type is already known, so it has not to be resolved again.
     */
    fun extract(parameterTypeInfoAndDeclaration: ParameterTypeInfoAndDeclaration) = extract(
        parameterTypeInfoAndDeclaration.classDeclaration,
        parameterTypeInfoAndDeclaration.typeInfo
    )

    /**
     * This method extracts the NavArgsInfo from the given NavArgsClassDeclaration
     */
    fun extract(
        navArgsClassDeclaration: KSClassDeclaration,
        typeInfo: ParameterTypeInfo? = null
    ): NavArgsInfo {

        val validParameters: List<KSValueParameter> = navArgsClassDeclaration
            .primaryConstructor
            ?.validParameters ?: throw IllegalStateException("NavArgs does not have a primary Constructor!")

        //Es handelt sich um eine ArgDefinition, jedoch ohne jegliche Parameter
        if (validParameters.isEmpty()) {
            throw IllegalStateException("NavArgs ha no Parameters, implement PlainDestination instead!")
        }

        val fileContent: KSFileContent = navArgsClassDeclaration.getKSFileContent(validParameters)

        return NavArgsInfo(
            typeInfo = typeInfo ?: navArgsClassDeclaration.asType.toParameterTypeInfo()!!,
            parameters = validParameters.map { mapToParameter(it, fileContent) }
        )
    }

    /**
     * This method extracts the NavArgsClassDeclaration from from a NavDestinationClassDeclaration
     */
    fun extractNavArgsClassDeclarationWith(
        destinationClassSupertype: KSType,
        destinationType: NavDestinationType
    ): ParameterTypeInfoAndDeclaration? = when(destinationType) {

        NavDestinationType.Destination,
        NavDestinationType.BottomSheetDestination,
        NavDestinationType.DialogDestination -> null

        NavDestinationType.ArgDestination,
        NavDestinationType.BottomSheetArgDestination,
        NavDestinationType.DialogArgDestination -> destinationClassSupertype
            .arguments
            .first()
            .toResolvedType()
            ?.toParameterTypeInfoWithClassDeclaration()
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
        val qualifiedTypeName = resolvedType.declaration.qualifiedName?.asString() ?: resolvedType.declaration.simpleName.asString()
        //Gets the parameter Type Info
        val parameterTypeInfo = resolvedType.toParameterTypeInfo() ?: throw IllegalStateException("Parameter is invalid!")
        //Default Value Check and retrieval
        val parameterDefaultValue = defaultValueExtractor.extract(valueParameter, qualifiedTypeName, fileContent)
        //The NavArgInfo for the according NavType<T>
        val parameterNavArgInfo = parameterTypeInfo.extractParameterNavArgTypeInfo()

        return Parameter(
            name = parameterName,
            typeInfo = parameterTypeInfo,
            navArgTypeInfo = parameterNavArgInfo,
            defaultValue = parameterDefaultValue
        )
    }

    private fun KSType.toParameterTypeInfo(): ParameterTypeInfo? = toParameterTypeInfoWithClassDeclaration()?.typeInfo

    private fun KSType.toParameterTypeInfoWithClassDeclaration(): ParameterTypeInfoAndDeclaration? {
        if (declaration.qualifiedName == null) return null

        val classDeclaration: KSClassDeclaration = getTypeAliasClassDeclaration()
        val classDeclarationType: KSType = classDeclaration.asType

        return ParameterTypeInfoAndDeclaration(
            classDeclaration = classDeclaration,
            typeInfo = ParameterTypeInfo(
                isNullable = isMarkedNullable,
                type = ParameterType(
                    import = classDeclaration.asImportInfo() ?: declaration.asImportInfo()!!,
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

    private fun KSType.extractParameterTypeArguments(): List<ParameterTypeArgument> = arguments.mapNotNull {
        if (it.variance == Variance.STAR) return@mapNotNull ParameterTypeArgument.STAR
        val typeInfo = it.toResolvedType()?.toParameterTypeInfo() ?: return@mapNotNull null
        ParameterTypeArgument(typeInfo, it.variance)
    }

//    private fun KSTypeArgument.extractParameterTypeArgument(): ParameterTypeArgument? {
//        if (variance == Variance.STAR) return ParameterTypeArgument.STAR
//        val typeInfo = toResolvedType()?.toParameterTypeInfo() ?: return null
//        return ParameterTypeArgument(typeInfo, variance)
//    }
}