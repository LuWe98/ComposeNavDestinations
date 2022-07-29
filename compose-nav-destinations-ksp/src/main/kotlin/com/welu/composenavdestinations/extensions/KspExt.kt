package com.welu.composenavdestinations.extensions

import com.google.devtools.ksp.*
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.*
import com.welu.composenavdestinations.annotaioninfo.AnnotationDeclaration
import com.welu.composenavdestinations.extensions.KspExt.IMPORT_REGEX
import com.welu.composenavdestinations.model.PackageImport
import java.io.BufferedReader

object KspExt {
    val IMPORT_REGEX = Regex("(import)\\s+\\w+(\\s*\\.\\s*\\w+)*(\\s*\\.\\s*(\\w+|\\*))?\\s*(as\\s+\\w*)?")
}

// KSClassDeclaration
fun KSClassDeclaration.getAnnotationWith(annotationName: String): KSAnnotation? = annotations.firstOrNull {
    it.shortName.asString() == annotationName
}

fun KSClassDeclaration.getAnnotationWith(annotationDeclaration: AnnotationDeclaration): KSAnnotation? =
    getAnnotationWith(annotationDeclaration.name)

fun KSClassDeclaration.requireAnnotationWith(annotationName: String): KSAnnotation = getAnnotationWith(annotationName) ?: throw IllegalStateException()

fun KSClassDeclaration.requireAnnotationWith(annotationDeclaration: AnnotationDeclaration): KSAnnotation =
    getAnnotationWith(annotationDeclaration) ?: throw IllegalStateException()

val KSClassDeclaration.validProperties get() = getAllProperties().filter(KSPropertyDeclaration::validate)


// KSFunctionDeclaration
val KSFunctionDeclaration.validParameters get() = parameters.filter(KSValueParameter::validate)


// KSAnnotation
fun KSAnnotation.getValueArgument(argName: String): KSValueArgument? = arguments.firstOrNull {
    it.name?.asString() == argName
}

fun KSAnnotation.requireValueArgument(argName: String): KSValueArgument =
    getValueArgument(argName) ?: throw IllegalStateException()


// KSValueArgument
inline fun <reified T> KSValueArgument.valueAs() = value as T


// KSNode
fun <R> KSNode.accept(visitor: KSVisitor<Unit, R>): R = accept(visitor, Unit)

val KSNode.fileLines get() = (location as FileLocation).file.reader.use(BufferedReader::readLines)

fun KSNode.firstNFileLines(lineCount: Int): List<String> = (location as FileLocation).file.reader.use {
    it.lineSequence().take(lineCount).toList()
}

val KSNode.rootKSFile get(): KSFile = containingFile ?: parent?.rootKSFile ?: throw IllegalStateException()


// KSType
val KSType.isNullable get() = nullability == Nullability.NULLABLE


//KSPropertyDeclaration
fun KSPropertyDeclaration.getDefaultValue(): String {
    val fileLocation = location as FileLocation

    return fileLocation.file.readLine(fileLocation.lineNumber)
}


// KSDeclaration
val KSDeclaration.rootKSFile get(): KSFile = containingFile ?: parentDeclaration?.rootKSFile ?: throw IllegalStateException()

val KSDeclaration.lineNumber get(): Int = (location as FileLocation).lineNumber - 1

val KSDeclaration.isAccessible get() = isPublic() || isInternal()

fun Sequence<KSDeclaration>.forEachWithIterator(action: (KSDeclaration) -> Unit) {
    iterator().let {
        while (it.hasNext()) {
            action(it.next())
        }
    }
}


// KSFile
val KSFile.firstDeclaration get(): KSDeclaration? = declarations.firstOrNull()

fun KSFile.extractImports(
    firstDeclarationRow: Int = firstDeclaration?.lineNumber ?: 0,
    lines: List<String> = this.firstNFileLines(firstDeclarationRow)
): List<PackageImport> {

    var joined = ""
    for (index in 0..firstDeclarationRow) {
        joined += lines[index] + " "
    }

    val isInsideStringLookup = BooleanArray(joined.length)
    var isInsideString = false

    for (charIndex in 0..joined.lastIndex) {
        if (joined[charIndex] == '"' && (charIndex == 0 || joined[charIndex - 1] != '\\')) {
            isInsideString = !isInsideString
        }
        isInsideStringLookup[charIndex] = isInsideString
    }

    return IMPORT_REGEX.findAll(joined).filter {
        !isInsideStringLookup[it.range.first]
    }.map(PackageImport::fromPackageImportMatch).toList()
}


// Resolver
@OptIn(KspExperimental::class)
fun Resolver.getValidDeclarationsOfPackage(packageName: String): Sequence<KSDeclaration> {
    return getDeclarationsFromPackage(packageName).filter(KSDeclaration::validate)
}

fun Resolver.isImportNameContainedInPackage(packageName: String, importName: String): Boolean = runCatching {
    getValidDeclarationsOfPackage(packageName).firstOrNull {
        it.simpleName.asString() == importName
    }?.let(KSDeclaration::isAccessible) ?: false
}.getOrDefault(false)


