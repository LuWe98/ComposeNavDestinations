package com.welu.compose_nav_destinations_ksp.extractor

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.*
import com.welu.compose_nav_destinations_ksp.extensions.isAnyOf
import com.welu.compose_nav_destinations_ksp.extensions.ksp.asImportInfo
import com.welu.compose_nav_destinations_ksp.extensions.ksp.asType
import com.welu.compose_nav_destinations_ksp.extensions.ksp.extractImports
import com.welu.compose_nav_destinations_ksp.extensions.ksp.getFileLines
import com.welu.compose_nav_destinations_ksp.extensions.ksp.getRootKSFile
import com.welu.compose_nav_destinations_ksp.extensions.ksp.getStarProjectedTypeWithClass
import com.welu.compose_nav_destinations_ksp.extensions.ksp.getTypeAliasClassDeclaration
import com.welu.compose_nav_destinations_ksp.extensions.ksp.getTypeWithClass
import com.welu.compose_nav_destinations_ksp.extensions.ksp.getTypeWithImportInfo
import com.welu.compose_nav_destinations_ksp.extensions.ksp.hasAnnotation
import com.welu.compose_nav_destinations_ksp.extensions.ksp.isEnum
import com.welu.compose_nav_destinations_ksp.extensions.ksp.toResolvedType
import com.welu.compose_nav_destinations_ksp.extensions.ksp.validParameters
import com.welu.compose_nav_destinations_ksp.extractor.NavArgTypeInfoExtractor.extractParameterNavArgType
import com.welu.compose_nav_destinations_ksp.model.KSFileContent
import com.welu.compose_nav_destinations_ksp.model.Parameter
import com.welu.compose_nav_destinations_ksp.model.ParameterType
import com.welu.compose_nav_destinations_ksp.model.ParameterTypeArgument
import com.welu.compose_nav_destinations_ksp.model.ParameterTypeInfo
import com.welu.compose_nav_destinations_ksp.model.ParameterTypeInfoAndDeclaration
import com.welu.compose_nav_destinations_ksp.model.components.ComposeDestinationType
import com.welu.compose_nav_destinations_ksp.model.navargs.NavArgsInfo
import com.welu.compose_nav_destinations_ksp.utils.ImportUtils
import java.io.Serializable

class NavArgsInfoExtractor(
    private val resolver: Resolver,
    private val logger: KSPLogger,
    private val defaultValueExtractor: DefaultValueExtractor = DefaultValueExtractor(resolver)
) {

    private val ksFileContentMap by lazy { mutableMapOf<KSFile, KSFileContent>() }

    private val parcelableType: KSType by lazy { resolver.getTypeWithImportInfo(ImportUtils.ANDROID_PARCELABLE_IMPORT) }

    private val KSType?.isParcelable get() = this?.let(parcelableType::isAssignableFrom) == true

    private val serializableType: KSType by lazy { resolver.getTypeWithClass(Serializable::class) }

    private val KSType?.isSerializable get() = this?.let(serializableType::isAssignableFrom) == true

    private val KSType?.isKotlinSerializable get() = this?.declaration?.hasAnnotation(ImportUtils.KOTLIN_SERIALIZABLE_IMPORT) == true

    private val listType: KSType by lazy { resolver.getStarProjectedTypeWithClass(List::class) }

    private val KSType?.isValidList
        get(): Boolean = this?.declaration?.qualifiedName?.asString()?.isAnyOf(*ImportUtils.VALID_LIST_QUALIFIERS) == true

    private val KSType?.isList
        get() = this?.let(listType::isAssignableFrom)?.also {
            if (it && !isValidList) {
                throw com.welu.compose_nav_destinations_ksp.exceptions.UnsupportedTypeException(
                    "Used List Subtype: ${declaration.qualifiedName?.asString()} is not supported. Use List, MutableList, AbstractList or ArrayList instead."
                )
            }
        } ?: false

    private val setType: KSType by lazy { resolver.getStarProjectedTypeWithClass(Set::class) }

    private val KSType?.isValidSet
        get(): Boolean = this?.declaration?.qualifiedName?.asString()?.isAnyOf(*ImportUtils.VALID_SET_QUALIFIERS) == true

    private val KSType?.isSet
        get() = this?.let(setType::isAssignableFrom)?.also {
            if (it && !isValidSet) {
                throw com.welu.compose_nav_destinations_ksp.exceptions.UnsupportedTypeException(
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
        destinationType: ComposeDestinationType
    ): ParameterTypeInfoAndDeclaration? = when(destinationType) {

        ComposeDestinationType.Destination,
        ComposeDestinationType.BottomSheetDestination,
        ComposeDestinationType.DialogDestination -> null

        ComposeDestinationType.ArgDestination,
        ComposeDestinationType.BottomSheetArgDestination,
        ComposeDestinationType.DialogArgDestination -> destinationClassSupertype
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
    private fun mapToParameter(
        valueParameter: KSValueParameter,
        fileContent: KSFileContent
    ): Parameter {
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
        val parameterNavArgInfo = parameterTypeInfo.extractParameterNavArgType()

        return Parameter(
            name = parameterName,
            typeInfo = parameterTypeInfo,
            navArgType = parameterNavArgInfo,
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
                    isKotlinSerializable = classDeclarationType.isKotlinSerializable,
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
}