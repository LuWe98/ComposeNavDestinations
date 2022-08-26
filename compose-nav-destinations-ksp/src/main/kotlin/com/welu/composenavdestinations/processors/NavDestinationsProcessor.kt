package com.welu.composenavdestinations.processors

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.validate
import com.welu.composenavdestinations.extensions.ksp.dependencies
import com.welu.composenavdestinations.extensions.ksp.getComposables
import com.welu.composenavdestinations.extensions.ksp.getNavArguments
import com.welu.composenavdestinations.extensions.ksp.getNavDestinations
import com.welu.composenavdestinations.generation.NavDestinationFileGenerator
import com.welu.composenavdestinations.mapper.NavDestinationMapper
import com.welu.composenavdestinations.utils.Constants
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
        val annotatedNavDestinations = resolver.getNavDestinations()
        if (!annotatedNavDestinations.iterator().hasNext()) return emptyList()

        _debugFile = codeGenerator.createNewFile(
            dependencies = resolver.dependencies,
            packageName = Constants.PACKAGE_NAME,
            fileName = "LoggingFile"
        )

        val annotatedComposables = resolver.getComposables()
        if (annotatedNavDestinations.any { !annotatedComposables.contains(it) }) {
            //TODO -> Nochmal anschauen
            throw IllegalStateException("NavDestination Annotation is only allowed on Composable-Functions")
        }

        val navArguments = resolver.getNavArguments()
        if (!navArguments.all { arg -> annotatedNavDestinations.any { arg.parent == it } }) {
            throw IllegalStateException("NavArgument Annotation is only allowed inside of NavDestination declaration.")
        }

        val mapper = NavDestinationMapper(resolver, logger, navArguments)
        val generator = NavDestinationFileGenerator(resolver, codeGenerator)

        annotatedNavDestinations
            .map(mapper::map)
            .forEach(generator::generate)

        _debugFile?.close()
        _debugFile = null

        return annotatedNavDestinations
            .filterNot(KSFunctionDeclaration::validate)
            .toList()
    }





//    enum class TestEnumHallo {
//        ONE,
//        TWO
//    }
//
//    fun a() {
//        //val array = asTypeInfo<ArrayList<*>>(asTypeInfo<String>())
//        val a = ArrayList::class.asTypeInfo(TestEnumHallo::class.asTypeInfo())
//
//        debugFile.write(a.toString())
//    }
//
//    //TODO -> Man muss schauen, dass die Arguments hier repräsentiert werden
//
//
//    private fun KClass<*>.asTypeInfo(
//        vararg typeArgs: ParameterTypeInfo,
//        isNullable: Boolean = false,
//        isSerializable: Boolean = Serializable::class.java.isAssignableFrom(javaObjectType)
//    ) = ParameterTypeInfo(
//        isNullable = isNullable,
//        type = ParameterType(
//            import = PackageImport(
//                simpleName!!,
//                qualifiedName!!
//            ),
//            typeArguments = typeArgs.map(ParameterTypeArgument::Typed),
//            isEnum = java.isEnum,
//            isSerializable = isSerializable
//        )
//    )

//    private inline fun <reified T : Any?> asTypeInfo(
//        isSerializable: Boolean = false,
//        vararg typeArgs: ParameterTypeInfo
//    ) = ParameterTypeInfo(
//        isNullable = null is T,
//        type = ParameterType(
//            import = PackageImport(
//                T::class.simpleName!!,
//                T::class.qualifiedName!!
//            ),
//            typeArguments = typeArgs.map(ParameterTypeArgument::Typed),
//            isEnum = T::class.java.isEnum,
//            isSerializable = isSerializable
//        )
//    )
//
//    private inline fun <reified T : Any> asTypeInfo(vararg typeArgs: ParameterTypeInfo) = asTypeInfo<T>(
//        isSerializable = Serializable::class.java.isAssignableFrom(T::class.javaObjectType),
//        typeArgs = typeArgs
//    )
}