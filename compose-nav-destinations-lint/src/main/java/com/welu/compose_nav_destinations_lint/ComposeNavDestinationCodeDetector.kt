/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.welu.compose_nav_destinations_lint

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import com.intellij.lang.jvm.JvmClassKind
import com.intellij.psi.PsiType
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UElement

//https://android.googlesource.com/platform/tools/base/+/studio-master-dev/lint/libs/lint-checks/src/main/java/com/android/tools/lint/checks
//TODO -> QuickFixes einbauen.

@Suppress("UnstableApiUsage")
class ComposeNavDestinationCodeDetector : Detector(), SourceCodeScanner {

    companion object {
        private const val ANNOTATION_NAV_GRAPH_DEFINITION_QUALIFIED_NAME = "com.android.example.NavGraphDefinition"
        private const val ANNOTATION_NAV_DESTINATION_DEFINITION_QUALIFIED_NAME = "com.android.example.DestinationAnnotation"
        private const val INTERFACE_NAV_DESTINATION = "com.android.example.Destination"
        private const val IS_START_PARAMETER_NAME ="isStart"
    }

    private fun UClass.hasSuperType(qualifiedName: String): Boolean =
        hasSuperType(qualifiedName.substring(qualifiedName.indexOfLast { it == '.' } + 1), qualifiedName)

    private fun UClass.hasSuperType(simpleName: String, qualifiedName: String): Boolean = superTypes.any {
        it.className == simpleName && it.resolve()?.qualifiedName == qualifiedName
    }

    override fun getApplicableUastTypes(): List<Class<out UElement?>> = listOf(UClass::class.java)


    /**
     * Only checks very plainly if the UClass is a Kotlin Object.
     */
    private fun UClass.isObject(context: JavaContext): Boolean {
        if (classKind != JvmClassKind.CLASS) return false
        if (context.evaluator.isOpen(this)) return false
        if (context.evaluator.isAbstract(this)) return false
        if (context.evaluator.isData(this)) return false
        if (!context.evaluator.isFinal(this)) return false
        if (context.evaluator.isCompanion(this)) return false
        if (context.evaluator.isSealed(this)) return false
        if (context.evaluator.isStatic(this)) return false
        return true

        //These Checks work but also not really unfortunatly
//        val location = context.getNameLocation(this)
//        location.start?.let { position ->
//            location.file.bufferedReader().useLines { it.elementAtOrNull(position.line) }?.let { specificLine ->
//                return specificLine.substring(0, position.column).trimEnd().endsWith("object")
//            }
//        }
//        return false
    }

    override fun createUastHandler(context: JavaContext) = object : UElementHandler() {
        override fun visitClass(node: UClass) {
            checkForDestinationInterfaceConnection(node, context)
            checkForInterfaceDestinationConnection(node, context)
            checkFoNavGraphCorrectness(node, context)
            checkForCorrectNavDestinationAnnotationUsage(node, context)
            checkIfComponentNavGraphUsage(node, context)
        }
    }

    private fun checkForDestinationInterfaceConnection(node: UClass, context: JavaContext) {
        //If the UClass is not a class we return
        if (node.classKind != JvmClassKind.CLASS) return

        //If the UClass implements the NavDestination Interface but is not annotated with the NavDestinationDefinition Annotation
        if (!(!node.hasAnnotation(ANNOTATION_NAV_DESTINATION_DEFINITION_QUALIFIED_NAME) && node.hasSuperType(INTERFACE_NAV_DESTINATION))) return

        Incident(context)
            .issue(Issues.ANNOTATION_CHECK_ISSUE)
            .at(node)
            .message("Destination implementations have to be annotated with NavDestinationDefinition")
            .let(context::report)
    }

    private fun checkForInterfaceDestinationConnection(node: UClass, context: JavaContext) {
        //If the UClass is not a class we return
        if (node.classKind != JvmClassKind.CLASS) return

        //If the UClass does not implement the NavDestination Interface but is annotated with the NavDestinationDefinition Annotation
        if (!(node.hasAnnotation(ANNOTATION_NAV_DESTINATION_DEFINITION_QUALIFIED_NAME) && !node.hasSuperType(INTERFACE_NAV_DESTINATION))) return

        Incident(context)
            .issue(Issues.IMPLEMENTATION_CHECK_ISSUE)
            .at(node)
            .message("A Class annotated with NavDestinationDefinition has to implement the Destination Interface.")
            .let(context::report)
    }

    private fun checkFoNavGraphCorrectness(node: UClass, context: JavaContext) {
        //If the UClass is not an Annotation we return
        if (node.classKind != JvmClassKind.ANNOTATION) return

        //Of the UClass does not have the NavGraphDefinition Annotation we return
        if (!node.hasAnnotation(ANNOTATION_NAV_GRAPH_DEFINITION_QUALIFIED_NAME)) return

        //If the required isStart: Boolean Method is present we return
        if (node.methods.any { it.name == IS_START_PARAMETER_NAME && it.returnType == PsiType.BOOLEAN }) return

//        val a = fix()
//            .name("Add **isStart** parameter", true)
//            .replace()
//            .pattern()

        Incident(context)
            .issue(Issues.NAV_GRAPH_ANNOTATION_IS_START_PARAMETER_CHECK_ISSUE)
            .at(node)
            .message("A NavGraph annotation class needs an 'isStart: Boolean' parameter.")
            .let(context::report)
    }

    private fun checkForCorrectNavDestinationAnnotationUsage(node: UClass, context: JavaContext) {
        //If the UClass is not annotated with @NavDestinationDefinition we can return
        val navDestinationAnnotation = node.uAnnotations.firstOrNull {
            it.qualifiedName == ANNOTATION_NAV_DESTINATION_DEFINITION_QUALIFIED_NAME
        } ?: return

        //If tis UClass is a Kotlin Object we can return. These checks are by no means complete.
        if (node.isObject(context)) return

        Incident(context)
            .issue(Issues.NAV_DESTINATION_ANNOTATION_CLASS_TYPE_ISSUE)
            .at(navDestinationAnnotation)
            .message("Only Objects can be annotated with @NavDestinationDefinition.")
            .let(context::report)
    }

    private fun checkIfComponentNavGraphUsage(node: UClass, context: JavaContext) {
        //Gets the ParentNavGraphAnnotations
        val parentNavGraphAnnotations = node.uAnnotations.filter {
            it.resolve()?.hasAnnotation(ANNOTATION_NAV_GRAPH_DEFINITION_QUALIFIED_NAME) == true
        }

        val isNavComponentAnnotationPresent = node.hasAnnotation(ANNOTATION_NAV_GRAPH_DEFINITION_QUALIFIED_NAME)
                || node.hasAnnotation(ANNOTATION_NAV_DESTINATION_DEFINITION_QUALIFIED_NAME)

        if (!isNavComponentAnnotationPresent && parentNavGraphAnnotations.isNotEmpty()) {
            for (parentNavGraphNode in parentNavGraphAnnotations) {
                Incident(context)
                    .issue(Issues.NAV_GRAPH_ANNOTATED_COMPONENT_CHECK)
                    .at(parentNavGraphNode)
                    .message("The annotated class is not a NavComponent.")
                    .let(context::report)
            }
            return
        }

        if (isNavComponentAnnotationPresent && parentNavGraphAnnotations.size > 1) {
            for (parentNavGraphNode in parentNavGraphAnnotations) {
                Incident(context)
                    .issue(Issues.NAV_GRAPH_MULTIPLE_ANNOTATION_CHECK)
                    .at(parentNavGraphNode)
                    .message("A NavComponent can only be part of one NavGraph.")
                    .let(context::report)
            }
        }
    }
}