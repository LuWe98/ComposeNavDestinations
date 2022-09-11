package com.welu.composenavdestinations.mapper

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.*
import com.welu.composenavdestinations.annotations.NavDestinationDefinitionAnnotation
import com.welu.composenavdestinations.exceptions.TypeResolveException
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
                throw UnsupportedTypeException("Used List Subtype: ${declaration.qualifiedName?.asString()} is not supported. Use List, MutableList, AbstractList or ArrayList instead.")
            }
        } ?: false

    private val setType: KSType by lazy { resolver.getStarProjectedTypeWithClass(Set::class) }

    private val KSType?.isValidSet
        get(): Boolean = this?.declaration?.qualifiedName?.asString()?.isOneOf(*PackageUtils.VALID_SET_QUALIFIERS) ?: false

    private val KSType?.isSet
        get() = this?.let(setType::isAssignableFrom)?.also {
            if (it && !isValidSet) {
                throw UnsupportedTypeException("Used Set Subtype: ${declaration.qualifiedName?.asString()} is not supported. Use Set, MutableSet, AbstractSet or HashSet instead")
            }
        } ?: false


    override fun map(declaration: KSClassDeclaration): NavDestinationInfo {

        val typeInfoWithDeclaration: ParameterTypeInfoWithDeclaration? = declaration.extractNavArgsTypeInfo()

        val destinationInfo = NavDestinationInfo(
            destinationImport = ImportInfo(
                simpleName = declaration.simpleName.asString(),
                packageDir = declaration.packageName.asString()
            ),
            destinationSpecImport = ImportInfo(
                simpleName = declaration.simpleName.asString() + PackageUtils.NAV_DESTINATION_SPEC_SUFFIX,
                packageDir = declaration.packageName.asString()
            ),
            route = declaration.getRouteName(),
            navArgsTypeInfo = typeInfoWithDeclaration?.typeInfo
        )

        //Es handelt sich um eine PlainDestination -> Es müssen keine Parameter extrahiert werden
        if (typeInfoWithDeclaration == null) return destinationInfo

        val validParameters: List<KSValueParameter> = typeInfoWithDeclaration
            .classDeclaration
            .primaryConstructor
            ?.validParameters ?: throw IllegalStateException("NavArgs does not have a primary Constructor!")

        //Es handelt sich um eine ArgDefinition, jedoch ohne jegliche Parameter
        if (validParameters.isEmpty()) {
            throw IllegalStateException("NavArgs ha no Parameters, implement PlainDestination instead!")
        }

        val fileContent: KSFileContent = typeInfoWithDeclaration.classDeclaration.getKSFileContent(validParameters)

        val parameters = validParameters.map { mapToParameter(it, fileContent) }

        return destinationInfo.copy(parameters = parameters)
    }


    /**
     * Loads the content of a File for default value extractions -> Is therefore only needed, when default values are present
     *
     * Loaded Files are saved inside a Map for reuse.
     */
    private fun KSDeclaration.getKSFileContent(parameters: List<KSValueParameter>): KSFileContent {
        if (parameters.none(KSValueParameter::hasDefault)) return KSFileContent.EMPTY

        val rootKSFile = rootKSFile
        return ksFileContentMap[rootKSFile] ?: run {
            val fileLines = rootKSFile.fileLines
            //TODO -> Nochmal schauen wegen dem extracten
            return KSFileContent(fileLines, rootKSFile.extractImports(lines = fileLines)).also {
                ksFileContentMap[rootKSFile] = it
            }
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
        val parameterDefValue = valueParameter.extractDefaultValue(resolver, fileContent, qualifiedType)
        //The NavArgInfo for the according NavType<T>
        val parameterNavArgInfo = parameterTypeInfo.extractParameterNavArgInfo()

        return Parameter(
            name = parameterName,
            typeInfo = parameterTypeInfo,
            navArgInfo = parameterNavArgInfo,
            defaultValue = parameterDefValue
        )
    }


    //TODO -> Das vllt noch schöner lösen -> Besseres Checking etc
    private fun KSClassDeclaration.extractNavArgsTypeInfo(): ParameterTypeInfoWithDeclaration? {
        superTypes.firstOrNull()?.let { typeReference ->
            val resolved = typeReference.resolve()

            return when (resolved.declaration.qualifiedName!!.asString()) {
                "com.welu.composenavdestinations.destinations.PlainDestination" -> null
                "com.welu.composenavdestinations.destinations.ArgDestination" -> resolved.extractNavArgsGenericParameterInfo()
                else -> throw IllegalStateException("Destination does not implement the neccessary Interface")
            }
        }

        throw IllegalArgumentException("Annotated Destination has to be a Subtype of a destination")
    }

    private fun KSType.toParameterTypeInfo(): ParameterTypeInfo? = toParameterTypeInfoWithClassDeclaration()?.typeInfo

    private fun KSType.toParameterTypeInfoWithClassDeclaration(): ParameterTypeInfoWithDeclaration? {
        if (declaration.qualifiedName == null) return null

        val classDeclaration: KSClassDeclaration = getTypeAliasClassDeclaration()
        val classDeclarationType: KSType = classDeclaration.asType
        val importInfo = ImportInfo(classDeclaration.qualifiedName?.asString() ?: declaration.qualifiedName!!.asString())
        //simpleName = classDeclaration.simpleName.asString(),

        return ParameterTypeInfoWithDeclaration(
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

    private fun KSType.extractNavArgsGenericParameterInfo(): ParameterTypeInfoWithDeclaration? = arguments
        .first()
        .toResolvedType()
        ?.toParameterTypeInfoWithClassDeclaration()

    private fun KSTypeArgument.toResolvedType(): KSType? {
        val resolvedType = type?.resolve()
        if (resolvedType?.isError == true) throw TypeResolveException("Resolved type contains errors: $resolvedType")
        return resolvedType
    }


    //TODO -> Annotation universal machen und dann instanzen der Annotationen erstellen -> NavDestinationDefinition("", "")
    private fun KSClassDeclaration.getRouteName(): String =
        getAnnotationArgument<String>(NavDestinationDefinitionAnnotation.ROUTE_ARG, NavDestinationDefinitionAnnotation).ifBlank { simpleName.asString() }

//    private fun KSClassDeclaration.getNavArgsClass(): KSClassDeclaration =
//        (getAnnotationArgument<KSType>(NavDestinationAnnotation.NAV_ARGS_ARG, NavDestinationAnnotation).declaration as KSClassDeclaration)

}