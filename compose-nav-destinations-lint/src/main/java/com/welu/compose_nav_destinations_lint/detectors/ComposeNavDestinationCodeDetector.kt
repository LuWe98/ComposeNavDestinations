package com.welu.compose_nav_destinations_lint.detectors

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import com.intellij.lang.jvm.JvmClassKind
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiClassType
import com.intellij.psi.PsiType
import com.welu.compose_nav_destinations_lint.Issues
import com.welu.compose_nav_destinations_lint.declarations.AnnotationDeclaration
import com.welu.compose_nav_destinations_lint.declarations.DestinationDeclaration
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UastVisibility

//TODO -> https://medium.com/mobile-app-development-publication/making-custom-lint-for-kotlin-code-8a6c203bf474
// Um eigenes Linting zu erstellen. Damit kann man bspw checken ob eine Flasse die mit @NavDestinationDefinition Annotiert ist auch das Destination Interface implementiert.
// Beispiel kann unten gesehen werden mit -> NoParcelableSupertype, wenn man nicht Parcelable Implementiert.
// TODO -> https://proandroiddev.com/implementing-your-first-android-lint-rule-6e572383b292
//  LINT checks in einem separaten Modul erstellen, dass bspw. eine Destination Klasse auch die @NavDestination Annotation besitzt und umgekehrt
//  https://android.googlesource.com/platform/tools/base/+/studio-master-dev/lint/libs/lint-checks/src/main/java/com/android/tools/lint/checks
//TODO -> QuickFixes einbauen.

@Suppress("UnstableApiUsage")
class ComposeNavDestinationCodeDetector : Detector(), SourceCodeScanner {

    companion object {
        private const val COMPOSE_NAV_GRAPH_IS_START_PARAMETER_NAME = "isStart"
        private const val KOTLIN_OBJECT_INSTANCE_FIELD_NAME = "INSTANCE"
    }

    private fun UClass.hasAnnotation(annotation: AnnotationDeclaration) = hasAnnotation(annotation.qualifiedName)

    private fun PsiClass.hasAnnotation(annotation: AnnotationDeclaration) = hasAnnotation(annotation.qualifiedName)

    private fun UClass.findAnnotation(annotation: AnnotationDeclaration) = findAnnotation(annotation.qualifiedName)

    private fun UClass.isDestinationSubType() = superTypes.any { it.isDestinationSuperType() }

    private fun PsiClassType.isDestinationSuperType(): Boolean = isValidDestinationSuperType() || superTypes.filterIsInstance<PsiClassType>().any {
        it.isDestinationSuperType()
    }

    private fun PsiClassType.isValidDestinationSuperType() = DestinationDeclaration.values().any {
        it.name == className && it.qualifiedName == resolve()?.qualifiedName
    }

    /**
     * Checks if the [UClass] is a Kotlin Object. These checks work with the generated INSTANCE Method of Object types
     */
    private fun UClass.isObject(context: JavaContext): Boolean {
        if (classKind != JvmClassKind.CLASS) return false
        if (context.evaluator.isAbstract(this)) return false
        if (context.evaluator.isOpen(this)) return false
        if (context.evaluator.isData(this)) return false
        if (context.evaluator.isSealed(this)) return false
        if (context.evaluator.isCompanion(this)) return false
        return fields.any {
            it.name.equals(KOTLIN_OBJECT_INSTANCE_FIELD_NAME, false)
                    && it.isStatic
                    && it.visibility == UastVisibility.PUBLIC
                    && it.type.canonicalText == this.qualifiedName
        }
    }



    override fun getApplicableUastTypes(): List<Class<out UElement?>> = listOf(UClass::class.java)

    override fun createUastHandler(context: JavaContext) = object : UElementHandler() {
        override fun visitClass(node: UClass) {
            checkForDestinationInterfaceConnection(node, context)
            checkForInterfaceDestinationConnection(node, context)
            checkForComposeNavGraphIsStartParameter(node, context)
            checkForCorrectComposeDestinationAnnotationUsage(node, context)
            checkIfComposeNavGraphUsage(node, context)
        }
    }

    private fun checkForDestinationInterfaceConnection(node: UClass, context: JavaContext) {
        //If the UClass is not a class we return
        if (node.classKind != JvmClassKind.CLASS) return

        //If the UClass implements a ComposeDestination Interface but is not annotated with @ComposeDestination
        if (node.isDestinationSubType() && !node.hasAnnotation(AnnotationDeclaration.ComposeDestination)) {
            Incident(context)
                .issue(Issues.ANNOTATION_CHECK_ISSUE)
                .at(node)
                .message("[${DestinationDeclaration.ComposeDestination}] implementations have to be annotated with [${AnnotationDeclaration.ComposeDestination}]")
                .let(context::report)
        }
    }

    private fun checkForInterfaceDestinationConnection(node: UClass, context: JavaContext) {
        //If the UClass is not a class we return
        if (node.classKind != JvmClassKind.CLASS) return

        //If the UClass is annotated with the @ComposeDestination but does not implement a ComposeDestination Interface 
        if (node.hasAnnotation(AnnotationDeclaration.ComposeDestination) && !node.isDestinationSubType()) {
            Incident(context)
                .issue(Issues.IMPLEMENTATION_CHECK_ISSUE)
                .at(node)
                .message("A class annotated with [${AnnotationDeclaration.ComposeDestination}] has to implement a [${DestinationDeclaration.ComposeDestination}] Interface.")
                .let(context::report)
        }
    }

    private fun checkForComposeNavGraphIsStartParameter(node: UClass, context: JavaContext) {
        //If the UClass is not an Annotation we return
        if (node.classKind != JvmClassKind.ANNOTATION) return

        //If the UClass does not have the NavGraphDefinition Annotation we return
        if (!node.hasAnnotation(AnnotationDeclaration.ComposeNavGraph)) return

        //If the required isStart: Boolean Method is present we return
        if (node.methods.any { it.name == COMPOSE_NAV_GRAPH_IS_START_PARAMETER_NAME && it.returnType == PsiType.BOOLEAN }) return

        //val a = fix().name("Add **isStart** parameter", true).replace().pattern()

        Incident(context)
            .issue(Issues.NAV_GRAPH_ANNOTATION_IS_START_PARAMETER_CHECK_ISSUE)
            .at(node)
            .message("A [${AnnotationDeclaration.ComposeNavGraph}] annotation class needs an 'isStart: Boolean' parameter.")
            .let(context::report)
    }


    /**
     * Checks if the [UClass] is an Object when it is annotated with @
     */
    private fun checkForCorrectComposeDestinationAnnotationUsage(node: UClass, context: JavaContext) {
        //If the UClass is not annotated with @NavDestinationDefinition we can return
        val navDestinationAnnotation = node.findAnnotation(AnnotationDeclaration.ComposeDestination) ?: return

        //If tis UClass is a Kotlin Object we can return. These checks are by no means complete.
        if (node.isObject(context)) return

        Incident(context)
            .issue(Issues.NAV_DESTINATION_ANNOTATION_CLASS_TYPE_ISSUE)
            .at(navDestinationAnnotation)
            .message("Only Objects can be annotated with [@${AnnotationDeclaration.ComposeDestination}].")
            .let(context::report)
    }

    /**
     * Checks if the [UClass] is a ComposeDestination or ComposeNavGraph and if those components
     * are correctly annotated with custom @ComposeNavGraph annotations.
     *
     * E.g: A ComposeDestination and ComposeNavGraph can only be part of one single other ComposeNavGraph
     */
    private fun checkIfComposeNavGraphUsage(node: UClass, context: JavaContext) {
        //Gets the parentComposeNavGraphs from this UClass node
        val parentComposeNavGraphs = node.uAnnotations.filter {
            it.resolve()?.hasAnnotation(AnnotationDeclaration.ComposeNavGraph) == true
        }

        val isNavComponentAnnotationPresent = node.hasAnnotation(AnnotationDeclaration.ComposeNavGraph) || node.hasAnnotation(AnnotationDeclaration.ComposeDestination)

        if (!isNavComponentAnnotationPresent && parentComposeNavGraphs.isNotEmpty()) {
            for (parentComposeNavGraph in parentComposeNavGraphs) {
                Incident(context)
                    .issue(Issues.NAV_GRAPH_ANNOTATED_COMPONENT_CHECK)
                    .at(parentComposeNavGraph)
                    .message(
                        "Only a [${AnnotationDeclaration.ComposeDestination}] or [${AnnotationDeclaration.ComposeNavGraph}]" +
                                " can be annotated with custom [@${AnnotationDeclaration.ComposeNavGraph}] annotations."
                    ).let(context::report)
            }
            return
        }

        if (isNavComponentAnnotationPresent && parentComposeNavGraphs.size > 1) {
            for (parentComposeNavGraph in parentComposeNavGraphs) {
                Incident(context)
                    .issue(Issues.NAV_GRAPH_MULTIPLE_ANNOTATION_CHECK)
                    .at(parentComposeNavGraph)
                    .message(
                        "A [${AnnotationDeclaration.ComposeDestination}] or [${AnnotationDeclaration.ComposeNavGraph}]" +
                                " can only be part of one [${AnnotationDeclaration.ComposeNavGraph}]"
                    ).let(context::report)
            }
        }
    }
}


//    private fun UClass.hasSuperType(qualifiedName: String): Boolean =
//        hasSuperType(qualifiedName.substring(qualifiedName.indexOfLast { it == '.' } + 1), qualifiedName)
//
//    private fun UClass.hasSuperType(simpleName: String, qualifiedName: String): Boolean = superTypes.any {
//        it.className == simpleName && it.resolve()?.qualifiedName == qualifiedName
//    }
//
//    private fun UClass.hasSuperType(declaration: Declaration): Boolean = superTypes.any {
//        it.className == declaration.name && it.resolve()?.qualifiedName == declaration.qualifiedName
//    }
//
//    private fun UClass.hasSuperTypeDeep(declaration: Declaration): Boolean = superTypes.any {
//        (it.className == declaration.name && it.resolve()?.qualifiedName == declaration.qualifiedName)
//    }
