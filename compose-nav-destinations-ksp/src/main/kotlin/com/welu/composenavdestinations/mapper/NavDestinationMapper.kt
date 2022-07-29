package com.welu.composenavdestinations.mapper

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.symbol.ClassKind.CLASS
import com.google.devtools.ksp.symbol.ClassKind.OBJECT
import com.welu.composenavdestinations.DefaultValueExtractor.getDefaultValue
import com.welu.composenavdestinations.annotaioninfo.NavDestinationAnnotation
import com.welu.composenavdestinations.extensions.*
import com.welu.composenavdestinations.model.KSFileContent
import com.welu.composenavdestinations.model.mapper.GeneratedClass
import com.welu.composenavdestinations.model.mapper.ValueParameter

class NavDestinationMapper(
    private val resolver: Resolver,
    private val logger: KSPLogger
): AnnotationMapper<GeneratedClass> {

    private val ksFileContentMap = mutableMapOf<KSFile, KSFileContent>()

    //Alles suspending machen, mit parameterverarbeitung etc
    override fun map(declaration: KSClassDeclaration): GeneratedClass {
         return when (declaration.classKind) {
            OBJECT -> mapToGeneratedObject(declaration)
            CLASS -> mapToGeneratedClass(declaration)
            else -> {
                logger.error("Only Classes and Objects are allowed")
                throw  IllegalStateException()
            }
        }
    }

    private fun mapToGeneratedObject(objectDeclaration: KSClassDeclaration): GeneratedClass {
        val routeArg: String = objectDeclaration.getRouteName()
        logger.info("ANNOTATED OBJECT: $routeArg \n")
        return GeneratedClass(routeArg, objectDeclaration, OBJECT)
    }

    private fun mapToGeneratedClass(classDeclaration: KSClassDeclaration): GeneratedClass {
        val routeArg: String = classDeclaration.getRouteName()
        logger.info("ANNOTATED CLASS: $routeArg \n")

        // ODER: classDeclaration.validProperties -> Wenn man nicht nur die vom Konstrukktor nehmen will
        val validParameters = classDeclaration.primaryConstructor?.validParameters ?: return GeneratedClass(routeArg, classDeclaration, CLASS)
        if (validParameters.isEmpty()) return GeneratedClass(routeArg, classDeclaration, CLASS)

        val fileContent: KSFileContent = getKSFileContent(classDeclaration, validParameters)

        val valueParameters = validParameters.map { parameter ->
            mapToValueParameter(parameter, fileContent)
        }

        valueParameters.forEach {
            logger.info("ArgName - ${it.name}, Type - ${it.type}, IsNullable - ${it.isNullable}, Def Value - ${it.defaultValue?.value ?: "NO DEFAULT VALUE PRESENT"}\n\n")
        }

        return GeneratedClass(routeArg, classDeclaration, CLASS, valueParameters)
    }


    private fun getKSFileContent(
        classDeclaration: KSClassDeclaration,
        constructorParameters: List<KSValueParameter>
    ): KSFileContent {
        //Checks if any default Value is Present. If yes, we load the related file in order to read those and extract the imports
        if (constructorParameters.none(KSValueParameter::hasDefault)) return KSFileContent()

        //Checkt ob das File mit den Import schon geladen wurden oder nicht. Lädt es nur, wenn es noch nicht reingeladen wurde
        val rootKSFile = classDeclaration.rootKSFile
        return ksFileContentMap[rootKSFile] ?: run {
            val fileLines = rootKSFile.fileLines
            return KSFileContent(fileLines, rootKSFile.extractImports(lines = fileLines)).also {
                ksFileContentMap[rootKSFile] = it
            }
        }
    }

    private fun mapToValueParameter(valueParameter: KSValueParameter, fileContent: KSFileContent): ValueParameter {
        //Das ist der Name von der Property
        val argName = valueParameter.name!!.asString()
        //Das ist der reolved Type
        val argResolvedType = valueParameter.type.resolve()
        //Das ist der Typ der Variablen
        val argType = argResolvedType.declaration.simpleName.asString()
        //Das ist der Import Name
        val argQualifiedType = argResolvedType.declaration.qualifiedName!!.asString()
        //Nullability Check
        val isNullable = argResolvedType.isNullable
        //Default Value Check
        val argDefValue = valueParameter.getDefaultValue(
            resolver = resolver,
            fileContent = fileContent,
            argQualifiedType = argQualifiedType
        )

        return ValueParameter(
            name = argName,
            type = argType,
            qualifiedType = argQualifiedType,
            isNullable = isNullable,
            defaultValue = argDefValue
        )
    }

    private inline fun <reified T> KSClassDeclaration.getAnnotationArgument(argName: String): T =
        requireAnnotationWith(NavDestinationAnnotation).requireValueArgument(argName).valueAs()

    private fun KSClassDeclaration.getRouteName(): String {
        return getAnnotationArgument<String>(NavDestinationAnnotation.routeArg).ifBlank { simpleName.asString() }
    }


    /*
    Standard Imports:
    kotlin.*
    kotlin.annotation.*
    kotlin.collections.*
    kotlin.comparisons.*
    kotlin.io.*
    kotlin.ranges.*
    kotlin.sequences.*
    kotlin.text.*
 */
//        runCatching {
//            resolver.getValidDeclarationsOfPackage("kotlin.text").distinctBy { it.simpleName }.forEachWithIterator {
//                fileOutputStream.write(it.simpleName.asString() + "\n")
//            }
//        }
}