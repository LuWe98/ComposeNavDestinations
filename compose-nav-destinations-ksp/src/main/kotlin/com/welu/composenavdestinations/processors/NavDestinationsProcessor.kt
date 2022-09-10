package com.welu.composenavdestinations.processors

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.validate
import com.welu.composenavdestinations.exceptions.NavArgumentAnnotationException
import com.welu.composenavdestinations.exceptions.NavDestinationAnnotationException
import com.welu.composenavdestinations.exceptions.TypeResolveException
import com.welu.composenavdestinations.extensions.ksp.*
import com.welu.composenavdestinations.extensions.write
import com.welu.composenavdestinations.extensions.writeLine
import com.welu.composenavdestinations.generation.ContentGenerator
import com.welu.composenavdestinations.mapper.NavDestinationMapper
import com.welu.composenavdestinations.model.ImportInfo
import com.welu.composenavdestinations.model.ParameterType
import com.welu.composenavdestinations.model.ParameterTypeArgument
import com.welu.composenavdestinations.model.ParameterTypeInfo
import com.welu.composenavdestinations.utils.PackageUtils
import com.welu.composenavdestinations.utils.PackageUtils.COMPOSABLE_IMPORT
import com.welu.composenavdestinations.validation.NavDestinationValidator
import java.io.OutputStream

class NavDestinationsProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>
) : SymbolProcessor {

    companion object {
        private var _debugFile: OutputStream? = null
        val debugFile get(): OutputStream = _debugFile!!
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {

        _debugFile = codeGenerator.createNewFile(
            dependencies = resolver.dependencies,
            packageName = PackageUtils.PACKAGE_NAME,
            fileName = "LoggingFile"
        )

        val defs = resolver.getNavDestinationDefinitions()
        handleDefs(defs)

        val annotatedNavDestinations = resolver.getNavDestinations()
        if (annotatedNavDestinations.none()) return emptyList()



        if (annotatedNavDestinations.any { !it.isAnnotationPresent(COMPOSABLE_IMPORT) }) {
            throw NavDestinationAnnotationException
        }

        val navArguments = resolver.getNavArguments()

        if (!navArguments.all { arg -> annotatedNavDestinations.any { arg.parent == it } }) {
            throw NavArgumentAnnotationException
        }

        val mapper = NavDestinationMapper(resolver, logger, navArguments)
        val navDestinationInfos = annotatedNavDestinations.map(mapper::map)

        NavDestinationValidator.validate(navDestinationInfos)

        ContentGenerator(resolver, codeGenerator).generate(navDestinationInfos)

        _debugFile?.close()
        _debugFile = null

        return annotatedNavDestinations.filterNot(KSFunctionDeclaration::validate).toList()
    }


    private fun handleDefs(defs: Sequence<KSClassDeclaration>) {
        defs.forEach {
//            it.classKind == ClassKind.OBJECT
            debugFile.writeLine(it.qualifiedName!!.asString(), 1)

            it.superTypes.firstOrNull()?.let { reference ->
                val resolved = reference.resolve()

                when (resolved.declaration.qualifiedName!!.asString()) {
                    "com.welu.composenavdestinations.spec.tests.PlainDestination" -> {
                        debugFile.write("PLAIN DESTINATION", 1)
                        (resolved.declaration as KSClassDeclaration).let { plainDeclaration ->
                            plainDeclaration.primaryConstructor?.let { constructor ->
                                val routeParam = constructor.parameters.first { route ->
                                    route.name!!.asString() == "route"
                                }
                            }
                        }
                    }
                    "com.welu.composenavdestinations.spec.tests.ArgDestination" -> {
                        debugFile.write("ARG DESTINATION", 1)
                        resolved.declaration.typeParameters.first().let { argType ->
                            val b = resolved.extractedParameterTypeArguments.first()
                            // val classDeclaration: KSClassDeclaration = (argType.getTypeAliasDeclaration() ?: argType) as KSClassDeclaration
                            b.typeInfo!!.type.import.let {
                                debugFile.write("ARG TYPE: ${it.qualifiedName}", 1)

                            }
                        }
                    }
                    else -> throw IllegalStateException()
                }

                debugFile.write("\n")

            } ?: throw IllegalArgumentException("Annotated Destination has to be a Subtype of a destination")
        }
    }


    private val KSType.asParameterTypeInfo
        get(): ParameterTypeInfo? {
            if (declaration.qualifiedName == null) return null

            val classDeclaration: KSClassDeclaration = (getTypeAliasDeclaration() ?: declaration) as KSClassDeclaration
            val importInfo = ImportInfo(classDeclaration.qualifiedName?.asString() ?: declaration.qualifiedName!!.asString())
            //simpleName = classDeclaration.simpleName.asString(),

            return ParameterTypeInfo(
                isNullable = isMarkedNullable,
                type = ParameterType(
                    import = importInfo,
                    typeArguments = extractedParameterTypeArguments,
                    isEnum = classDeclaration.isEnum
                )
            )
        }

    private val KSType.extractedParameterTypeArguments
        get(): List<ParameterTypeArgument> = arguments.mapNotNull { arg ->
            if (arg.variance == Variance.STAR) return@mapNotNull ParameterTypeArgument.STAR

            val resolvedType = arg.type?.resolve() ?: return@mapNotNull null

            // Alternativ einfach null zurückgeben
            if (resolvedType.isError) throw TypeResolveException("Resolved type contains errors: $resolvedType")

            ParameterTypeArgument(
                typeInfo = resolvedType.asParameterTypeInfo ?: return@mapNotNull null,
                variance = arg.variance
            )
        }
}